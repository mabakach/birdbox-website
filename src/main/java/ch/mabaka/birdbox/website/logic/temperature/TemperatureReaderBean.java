package ch.mabaka.birdbox.website.logic.temperature;

import ch.mabaka.birdbox.website.logic.temperature.SensorResponse;
import ch.mabaka.birdbox.website.persistence.converter.SensorResponseToTemperatureMessurementEntityConverter;
import ch.mabaka.birdbox.website.persistence.entities.TemperatureMeassurementEntity;
import ch.mabaka.birdbox.website.persistence.repositories.TemperatureMeassurementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;

/**
 * Spring component that periodically reads temperature and humidity data from the Birdbox sensor API.
 * <p>
 * The sensor URL and timeout are configurable via application.properties. Every minute, the bean fetches
 * the latest sensor data, logs the values, maps them to a TemperatureMeassurementEntity, and persists them
 * using TemperatureMeassurementRepository. Timeout and error handling ensure robust operation.
 */
@Component
public class TemperatureReaderBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemperatureReaderBean.class);

    /**
     * The URL of the Birdbox sensor API endpoint.
     * Configured via 'birdbox.sensor.url' in application.properties.
     */
    @Value("${birdbox.sensor.url}")
    private String sensorUrl;

    /**
     * Timeout for HTTP requests to the sensor API, in milliseconds.
     * Configured via 'birdbox.sensor.timeout' in application.properties.
     */
    @Value("${birdbox.sensor.timeout}")
    private int sensorTimeout;

    /**
	 * Bean to hold the current temperature and humidity readings.
	 */
    @Autowired
    CurrentTemperatureBean currentTemperatureBean;
    
    /**
     * Repository for persisting temperature and humidity measurements.
     */
    @Autowired
    private TemperatureMeassurementRepository repository;

    /**
     * RestTemplate configured with custom timeouts for sensor API requests.
     */
    private final RestTemplate restTemplate;

    /**
     * Constructs the TemperatureReaderBean and configures the RestTemplate with timeouts.
     */
    public TemperatureReaderBean() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(sensorTimeout);
        factory.setReadTimeout(sensorTimeout);
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * Scheduled method that runs every 60 seconds to read temperature and humidity from the sensor API.
     * <p>
     * On success, logs and persists the measurement. Handles timeouts and other errors gracefully.
     */
    @Scheduled(fixedRate = 60000)
    public void readTemperature() {
        try {
            final SensorResponse response = restTemplate.getForObject(sensorUrl, SensorResponse.class);
            if (response != null) {
            	final long readTimeMillis = System.currentTimeMillis();
            	response.setReadTimestamp(java.time.Instant.ofEpochMilli(readTimeMillis));
                currentTemperatureBean.setLatestSensorResponse(response);
            	final TemperatureMeassurementEntity entity = SensorResponseToTemperatureMessurementEntityConverter.convert(response);
                repository.save(entity);
                LOGGER.info("Read temperature: {}°C, humidity: {}%", entity.getTemperature(), entity.getHumidity());
            } else {
                LOGGER.warn("Sensor response is null");
            }
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                LOGGER.error("Timeout while reading temperature from sensor (>{}ms)", sensorTimeout);
            } else {
                LOGGER.error("Resource access error while reading temperature from sensor", e);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to read temperature from sensor", e);
            LOGGER.debug("Error details: ", e);
            
        }
    }
}