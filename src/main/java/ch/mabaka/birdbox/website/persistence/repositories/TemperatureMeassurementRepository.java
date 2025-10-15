package ch.mabaka.birdbox.website.persistence.repositories;

import ch.mabaka.birdbox.website.persistence.entities.TemperatureMeassurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

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
}
