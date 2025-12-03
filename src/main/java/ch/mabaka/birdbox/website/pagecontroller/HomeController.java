package ch.mabaka.birdbox.website.pagecontroller;

import ch.mabaka.birdbox.website.logic.temperature.CurrentTemperatureBean;
import ch.mabaka.birdbox.website.logic.temperature.SensorResponse;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@EnableCaching
public class HomeController {
  private static final String DATE_TIME_FORMAT = "dd.MM.yyyy, HH:mm:ss";
  
  private final WebClient webClient = WebClient.create();
  
  @Autowired
  private CurrentTemperatureBean currentTemperatureBean;
  
  @Value("${birdbox.stream.url}")
  private String streamUrl;
  
  @Value("${birdbox.stream.status.url}")
  private String streamStatusUrl;
  
  @Value("${birdbox.stream.notavailable.image.url}")
  private String streamNotAvailableImageUrl;
  
  @Cacheable(value = {"streamStatus"}, key = "'status'", unless = "#result == null")
  private boolean isStreamAvailable() {
    return ((Boolean)this.webClient.get()
      .uri(this.streamStatusUrl, new Object[0])
      .retrieve()
      .bodyToMono(Map.class)
      .map(response -> (Boolean)response.get("stream-available"))
      .block()).booleanValue();
  }
  
  @GetMapping({"/"})
  public String index(Model model) {
    String currentStreamUrl = isStreamAvailable() ? this.streamUrl : this.streamNotAvailableImageUrl;
    model.addAttribute("streamUrl", currentStreamUrl);
    SensorResponse sensorResponse = this.currentTemperatureBean.getLatestSensorResponse();
    if (sensorResponse == null) {
      model.addAttribute("currentTemperature", "N/A");
      model.addAttribute("currentHumidity", "N/A");
      model.addAttribute("lastUpdateTime", "N/A");
    } else {
      model.addAttribute("currentTemperature", String.format("%.1f", new Object[] { Double.valueOf(sensorResponse.getTemperature()) }));
      model.addAttribute("currentHumidity", String.format("%.1f", new Object[] { Double.valueOf(sensorResponse.getHumidity()) }));
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");
      String formattedTime = sensorResponse.getReadTimestamp().atZone(ZoneId.systemDefault()).format(formatter);
      model.addAttribute("lastUpdateTime", formattedTime);
    } 
    return "index";
  }
}
