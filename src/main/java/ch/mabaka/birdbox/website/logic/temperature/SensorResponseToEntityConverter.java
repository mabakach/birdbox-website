package ch.mabaka.birdbox.website.logic.temperature;

import ch.mabaka.birdbox.website.persistence.entities.TemperatureMeassurementEntity;
import java.sql.Timestamp;

public class SensorResponseToEntityConverter {
    public static TemperatureMeassurementEntity convert(SensorResponse response) {
        TemperatureMeassurementEntity entity = new TemperatureMeassurementEntity();
        entity.setMeasurementTimestamp(new Timestamp(System.currentTimeMillis()));
        entity.setTemperature(response.getTemperature_celsius());
        entity.setHumidity(response.getHumidity());
        return entity;
    }
}
