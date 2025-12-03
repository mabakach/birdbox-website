package ch.mabaka.birdbox.website.logic.temperature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorResponse {
  @JsonProperty("temperature")
  private double temperature;
  @JsonProperty("humidity")
  private double humidity;
  @JsonProperty("readTimestamp")
  private Instant readTimestamp;

  public double getTemperature() {
    return this.temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public double getHumidity() {
    return this.humidity;
  }

  public void setHumidity(double humidity) {
    this.humidity = humidity;
  }

  public Instant getReadTimestamp() {
    return this.readTimestamp;
  }

  public void setReadTimestamp(Instant readTimestamp) {
    this.readTimestamp = readTimestamp;
  }
}