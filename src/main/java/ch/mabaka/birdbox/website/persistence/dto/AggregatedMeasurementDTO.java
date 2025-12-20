package ch.mabaka.birdbox.website.persistence.dto;

import java.sql.Timestamp;

public class AggregatedMeasurementDTO {
	private Timestamp timestamp;
	private Double avgTemperature;
	private Double avgHumidity;

	public AggregatedMeasurementDTO(Timestamp timestamp, Double avgTemperature, Double avgHumidity) {
		this.timestamp = timestamp;
		this.avgTemperature = avgTemperature;
		this.avgHumidity = avgHumidity;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public Double getAvgTemperature() {
		return avgTemperature;
	}

	public Double getAvgHumidity() {
		return avgHumidity;
	}
}