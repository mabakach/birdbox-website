package ch.mabaka.birdbox.website.persistence.entities;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "temperature_meassurement")
public class TemperatureMeassurementEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Timestamp measurementTimestamp;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double humidity;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getMeasurementTimestamp() {
		return measurementTimestamp;
	}
	
	public void setMeasurementTimestamp(final Timestamp measurementTimestamp) {
		this.measurementTimestamp = measurementTimestamp;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(final Double temperature) {
		this.temperature = temperature;
	}

	public Double getHumidity() {
		return humidity;
	}

	public void setHumidity(final Double humidity) {
		this.humidity = humidity;
	}
}
