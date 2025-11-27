package ch.mabaka.birdbox.website.logic.temperature;
import ch.mabaka.birdbox.website.logic.temperature.SensorResponse;
import org.springframework.stereotype.Component;

@Component
public class CurrentTemperatureBean {
  private SensorResponse latestSensorResponse;
  
  public synchronized void setLatestSensorResponse(SensorResponse latestSensorResponse) {
    this.latestSensorResponse = latestSensorResponse;
  }
  
  public synchronized SensorResponse getLatestSensorResponse() {
    return this.latestSensorResponse;
  }
}
