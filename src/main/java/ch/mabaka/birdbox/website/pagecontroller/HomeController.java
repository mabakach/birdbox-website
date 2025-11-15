package ch.mabaka.birdbox.website.pagecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import ch.mabaka.birdbox.website.logic.temperature.CurrentTemperatureBean;
import ch.mabaka.birdbox.website.logic.temperature.SensorResponse;

import org.springframework.cache.annotation.EnableCaching;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
@EnableCaching
public class HomeController {

    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy, HH:mm:ss";

	private final WebClient webClient;
    
    @Autowired
    private CurrentTemperatureBean currentTemperatureBean;

    @Value("${birdbox.stream.url}")
    private String streamUrl;

    @Value("${birdbox.stream.status.url}")
    private String streamStatusUrl;

    @Value("${birdbox.stream.notavailable.image.url}")
    private String streamNotAvailableImageUrl;

    public HomeController() {
        this.webClient = WebClient.create();
    }

    @Cacheable(value = "streamStatus", key = "'status'", unless = "#result == null")
    private boolean isStreamAvailable() {
        return webClient.get()
                .uri(streamStatusUrl)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Boolean) response.get("stream-available"))
                .block();
    }

    @GetMapping("/")
    public String index(final Model model) {
        String currentStreamUrl = isStreamAvailable() ? streamUrl : streamNotAvailableImageUrl;
        model.addAttribute("streamUrl", currentStreamUrl);
        
        final SensorResponse sensorResponse = currentTemperatureBean.getLatestSensorResponse();
        if (sensorResponse == null) {
			model.addAttribute("currentTemperature", "N/A");
			model.addAttribute("currentHumidity", "N/A");
			model.addAttribute("lastUpdateTime", "N/A");
		} else {		
			model.addAttribute("currentTemperature", String.format("%.1f", sensorResponse.getTemperature_celsius()));
			model.addAttribute("currentHumidity", String.format("%.1f", sensorResponse.getHumidity()));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
            String formattedTime = sensorResponse.getReadTimestamp().atZone(ZoneId.systemDefault()).format(formatter);
            model.addAttribute("lastUpdateTime", formattedTime);
		}
        
        return "index";
    }
}