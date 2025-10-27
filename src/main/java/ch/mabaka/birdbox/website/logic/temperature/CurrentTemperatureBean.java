package ch.mabaka.birdbox.website.logic.temperature;

import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class CurrentTemperatureBean {
    private SensorResponse latestSensorResponse;
    
    public synchronized void setLatestSensorResponse(final SensorResponse latestSensorResponse) {
        this.latestSensorResponse = latestSensorResponse;
    }

    public synchronized SensorResponse getLatestSensorResponse() {
        return latestSensorResponse;
    }

}
