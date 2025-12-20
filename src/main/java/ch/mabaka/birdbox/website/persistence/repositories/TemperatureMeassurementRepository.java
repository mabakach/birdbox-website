package ch.mabaka.birdbox.website.persistence.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ch.mabaka.birdbox.website.persistence.dto.AggregatedMeasurementDTO;
import ch.mabaka.birdbox.website.persistence.entities.TemperatureMeassurementEntity;

/**
 * Repository interface for managing TemperatureMeassurementEntity entities.
 */
@Repository
public interface TemperatureMeassurementRepository extends JpaRepository<TemperatureMeassurementEntity, Long> {

	static final long ONE_DAY_MS = 24 * 60 * 60 * 1000;

	@Query("SELECT t FROM TemperatureMeassurementEntity t WHERE t.measurementTimestamp >= :startDate ORDER BY t.measurementTimestamp")
	List<TemperatureMeassurementEntity> findMeasurementsAfter(Timestamp startDate);

	default List<TemperatureMeassurementEntity> findLastDayMeasurements() {
		Timestamp oneDayAgo = new Timestamp(System.currentTimeMillis() - ONE_DAY_MS);
		return findMeasurementsAfter(oneDayAgo);
	}

	default List<TemperatureMeassurementEntity> findLastWeekMeasurements() {
		Timestamp sevenDaysAgo = new Timestamp(System.currentTimeMillis() - 7 * ONE_DAY_MS);
		return findMeasurementsAfter(sevenDaysAgo);
	}

	default List<TemperatureMeassurementEntity> findLastMonthMeasurements() {
		Timestamp thirtyDaysAgo = new Timestamp(System.currentTimeMillis() - 30L * ONE_DAY_MS);
		return findMeasurementsAfter(thirtyDaysAgo);
	}

	default List<TemperatureMeassurementEntity> findLastQuarterMeasurements() {
		Timestamp ninetyDaysAgo = new Timestamp(System.currentTimeMillis() - 90L * ONE_DAY_MS);
		return findMeasurementsAfter(ninetyDaysAgo);
	}

	Optional<TemperatureMeassurementEntity> findFirstByOrderByMeasurementTimestampDesc();

	@Query(value = "SELECT DATE_FORMAT(measurement_timestamp, '%Y-%m-%d %H:00:00') AS timestamp, AVG(temperature) AS avg_temperature, AVG(humidity) AS avg_humidity "
			+ "FROM temperature_meassurement " + "WHERE measurement_timestamp >= :startDate "
			+ "GROUP BY DATE_FORMAT(measurement_timestamp, '%Y-%m-%d %H:00:00') "
			+ "ORDER BY timestamp", nativeQuery = true)
	List<Object[]> findAggregatedByHourAfter(@Param("startDate") java.sql.Timestamp startDate);

	@Query(value = "SELECT DATE(measurement_timestamp) AS timestamp, AVG(temperature) AS avg_temperature, AVG(humidity) AS avg_humidity "
			+ "FROM temperature_meassurement " + "WHERE measurement_timestamp >= :startDate "
			+ "GROUP BY DATE(measurement_timestamp) " + "ORDER BY timestamp", nativeQuery = true)
	List<Object[]> findAggregatedByDayAfter(@Param("startDate") java.sql.Timestamp startDate);

	default List<AggregatedMeasurementDTO> findLastWeekAggregated() {
		java.sql.Timestamp sevenDaysAgo = new java.sql.Timestamp(System.currentTimeMillis() - 7 * ONE_DAY_MS);
		return mapToAggregatedDTO(findAggregatedByHourAfter(sevenDaysAgo));
	}

	default List<AggregatedMeasurementDTO> findLastMonthAggregated() {
		java.sql.Timestamp thirtyDaysAgo = new java.sql.Timestamp(System.currentTimeMillis() - 30L * ONE_DAY_MS);
		return mapToAggregatedDTO(findAggregatedByHourAfter(thirtyDaysAgo));
	}

	default List<AggregatedMeasurementDTO> findLastQuarterAggregated() {
		java.sql.Timestamp ninetyDaysAgo = new java.sql.Timestamp(System.currentTimeMillis() - 90L * ONE_DAY_MS);
		return mapToAggregatedDTO(findAggregatedByDayAfter(ninetyDaysAgo));
	}

	private static List<AggregatedMeasurementDTO> mapToAggregatedDTO(List<Object[]> results) {
		List<AggregatedMeasurementDTO> dtos = new java.util.ArrayList<>();
		for (Object[] row : results) {
			Object tsObj = row[0];
			java.sql.Timestamp ts;
			if (tsObj instanceof java.sql.Timestamp) {
				ts = (java.sql.Timestamp) tsObj;
			} else if (tsObj instanceof java.sql.Date) {
				ts = new java.sql.Timestamp(((java.sql.Date) tsObj).getTime());
			} else if (tsObj instanceof String) {
				String s = (String) tsObj;
				if (s.length() == 10) {
					ts = java.sql.Timestamp.valueOf(s + " 00:00:00");
				} else {
					ts = java.sql.Timestamp.valueOf(s);
				}
			} else {
				throw new IllegalArgumentException("Unknown timestamp type: " + tsObj.getClass());
			}
			dtos.add(new AggregatedMeasurementDTO(ts, (Double) row[1], (Double) row[2]));
		}
		return dtos;
	}

}