package ch.mabaka.birdbox.website.persistence.converter;

import ch.mabaka.birdbox.website.logic.temperature.SensorResponse;
import ch.mabaka.birdbox.website.persistence.entities.TemperatureMeassurementEntity;
import java.sql.Timestamp;

/**
 * Converter class to transform between SensorResponse and TemperatureMeassurementEntity.
 */
public class SensorResponseToTemperatureMessurementEntityConverter {
	
	/**
	 * Converts a SensorResponse object to a TemperatureMeassurementEntity object.
	 *
	 * @param response the SensorResponse to convert
	 * @return the converted TemperatureMeassurementEntity
	 */
	public static TemperatureMeassurementEntity convert(final SensorResponse response) {
		final TemperatureMeassurementEntity entity = new TemperatureMeassurementEntity();
		entity.setMeasurementTimestamp(
				(response.getReadTimestamp() != null) ? Timestamp.from(response.getReadTimestamp()) : null);
		entity.setTemperature(Double.valueOf(response.getTemperature()));
		entity.setHumidity(Double.valueOf(response.getHumidity()));
		return entity;
	}
	
	/**
	 * Converts a TemperatureMeassurementEntity object to a SensorResponse object.
	 *
	 * @param entity the TemperatureMeassurementEntity to convert
	 * @return the converted SensorResponse
	 */
	public static SensorResponse convert(final TemperatureMeassurementEntity entity) {
		final SensorResponse response = new SensorResponse();
		if (entity.getMeasurementTimestamp() != null) {
			response.setReadTimestamp(entity.getMeasurementTimestamp().toInstant());
		}
		if (entity.getTemperature() != null) {
			response.setTemperature(entity.getTemperature().doubleValue());
		}
		if (entity.getHumidity() != null) {
			response.setHumidity(entity.getHumidity().doubleValue());
		}
		return response;
	}
}
