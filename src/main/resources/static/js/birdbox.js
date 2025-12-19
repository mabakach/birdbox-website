// birdbox.js - Common JavaScript for Birdbox pages

// --- index.html specific functions ---
function formatDateWithLeadingZeros(date) {
    const d = new Date(date);
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const year = d.getFullYear();
    const hours = String(d.getHours()).padStart(2, '0');
    const minutes = String(d.getMinutes()).padStart(2, '0');
    const seconds = String(d.getSeconds()).padStart(2, '0');
    return `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;
}

var stompClient = null;
var socket = null;
var reconnectTimeout = null;
var reconnectDelay = 3000; // ms

function connectSensorWebSocket() {
    if (stompClient && stompClient.connected) {
        return;
    }
    socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/SensorResponse', function (message) {
            var data = JSON.parse(message.body);
            var temperature = data.temperature !== undefined ? data.temperature : '-';
            var humidity = data.humidity !== undefined ? data.humidity : '-';
            var timestamp = data.readTimestamp ? formatDateWithLeadingZeros(data.readTimestamp) : '-';
            if (document.getElementById('tempValue')) document.getElementById('tempValue').textContent = temperature;
            if (document.getElementById('humidityValue')) document.getElementById('humidityValue').textContent = humidity;
            if (document.getElementById('lastUpdateValue')) document.getElementById('lastUpdateValue').textContent = timestamp;
        });
    }, function (error) {
        scheduleReconnect();
    });
    if (socket) {
        socket.onclose = function() {
            scheduleReconnect();
        };
    }
}
function scheduleReconnect() {
    if (reconnectTimeout) return;
    reconnectTimeout = setTimeout(function() {
        reconnectTimeout = null;
        connectSensorWebSocket();
    }, reconnectDelay);
}
// Handle page visibility changes
function birdboxVisibilityHandler() {
    if (!document.hidden) {
        reloadLivestreamImg();
        connectSensorWebSocket();
    }
}
// Livestream image reload logic
function reloadLivestreamImg() {
    var img = document.getElementById('livestreamImg');
    if (img) {
        var src = img.getAttribute('th:src') || img.src;
        var url = src.split('?')[0] + '?_ts=' + new Date().getTime();
        img.src = url;
    }
}
function handleStreamError() {
    setTimeout(reloadLivestreamImg, 2000);
}
function birdboxInit() {
    if (typeof connectSensorWebSocket === 'function') connectSensorWebSocket();
    if (typeof birdboxVisibilityHandler === 'function') {
        document.addEventListener('visibilitychange', birdboxVisibilityHandler);
    }
}
