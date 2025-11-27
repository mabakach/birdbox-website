package ch.mabaka.birdbox.website.logic.temperature;

import java.time.Instant;

public class SensorResponse {
  private double temperature_celsius;
  
  private double humidity;
  
  private Instant readTimestamp;
  
  public double getTemperature_celsius() {
    return this.temperature_celsius;
  }
  
  public void setTemperature_celsius(double temperature_celsius) {
    this.temperature_celsius = temperature_celsius;
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
