package ch.mabaka.birdbox.website.logic.temperature;

public class SensorResponse {
    private double temperature_celsius;
    private double humidity;

    public double getTemperature_celsius() {
        return temperature_celsius;
    }

    public void setTemperature_celsius(double temperature_celsius) {
        this.temperature_celsius = temperature_celsius;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}
