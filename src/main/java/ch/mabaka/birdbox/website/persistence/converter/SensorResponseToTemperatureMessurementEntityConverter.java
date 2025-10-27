package ch.mabaka.birdbox.website.persistence.converter;

import ch.mabaka.birdbox.website.logic.temperature.SensorResponse;
import ch.mabaka.birdbox.website.persistence.entities.TemperatureMeassurementEntity;
import java.sql.Timestamp;

public class SensorResponseToTemperatureMessurementEntityConverter {
    public static TemperatureMeassurementEntity convert(SensorResponse response) {
        TemperatureMeassurementEntity entity = new TemperatureMeassurementEntity();
        entity.setMeasurementTimestamp(response.getReadTimestamp() != null ? Timestamp.from(response.getReadTimestamp()) : null);
        entity.setTemperature(response.getTemperature_celsius());
        entity.setHumidity(response.getHumidity());
        return entity;
    }
}
