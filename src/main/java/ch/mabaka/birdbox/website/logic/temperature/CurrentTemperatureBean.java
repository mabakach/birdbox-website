package ch.mabaka.birdbox.website.logic.temperature;

import org.springframework.stereotype.Component;

/**
 * A Spring component that holds the latest sensor response for temperature and
 * humidity.
 */
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
