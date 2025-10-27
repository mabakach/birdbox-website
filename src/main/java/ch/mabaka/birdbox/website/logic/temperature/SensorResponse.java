package ch.mabaka.birdbox.website.logic.temperature;

import java.time.Instant;

public class SensorResponse {
    private double temperature_celsius;
    private double humidity;
    private Instant readTimestamp;

    public double getTemperature_celsius() {
        return temperature_celsius;
    }

    public void setTemperature_celsius(final double temperature_celsius) {
        this.temperature_celsius = temperature_celsius;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(final double humidity) {
        this.humidity = humidity;
    }
    
    public Instant getReadTimestamp() {
		return readTimestamp;
	}
    
    public void setReadTimestamp(final Instant readTimestamp) {
		this.readTimestamp = readTimestamp;
	}
}
