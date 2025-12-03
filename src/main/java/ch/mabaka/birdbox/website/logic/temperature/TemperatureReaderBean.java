package ch.mabaka.birdbox.website.logic.temperature;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import ch.mabaka.birdbox.website.persistence.converter.SensorResponseToTemperatureMessurementEntityConverter;
import ch.mabaka.birdbox.website.persistence.entities.TemperatureMeassurementEntity;
import ch.mabaka.birdbox.website.persistence.repositories.TemperatureMeassurementRepository;
import jakarta.annotation.PostConstruct;

/**
 * A Spring component that periodically reads temperature and humidity data from a sensor,
 * updates the current temperature bean, saves the data to the repository, and sends updates via WebSocket.
 */
@Component
public class TemperatureReaderBean {
  private static final Logger LOGGER = LoggerFactory.getLogger(ch.mabaka.birdbox.website.logic.temperature.TemperatureReaderBean.class);
  
  @Value("${birdbox.sensor.url}")
  private String sensorUrl;
  
  @Value("${birdbox.sensor.timeout}")
  private int sensorTimeout;
  
  @Autowired
  CurrentTemperatureBean currentTemperatureBean;
  
  @Autowired
  private TemperatureMeassurementRepository repository;
  
  private final RestTemplate restTemplate;
  
  @Autowired
  private SimpMessagingTemplate messagingTemplate;
  
  public TemperatureReaderBean() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(this.sensorTimeout);
    factory.setReadTimeout(this.sensorTimeout);
    this.restTemplate = new RestTemplate((ClientHttpRequestFactory)factory);
  }
  
  /**
   * Initializes the TemperatureReaderBean by logging its configuration and
   * populating the current temperature bean with the latest measurement from the repository.
   */
  @PostConstruct
  public void init() {
	  	LOGGER.info("TemperatureReaderBean initialized, sensor URL: {}, timeout: {}ms", this.sensorUrl, Integer.valueOf(this.sensorTimeout));
	  	// Initial read to populate data immediately after startup
	  	repository.findLatestMeasurement().ifPresent(entity -> {
	  		currentTemperatureBean.setLatestSensorResponse(
	  				SensorResponseToTemperatureMessurementEntityConverter.convert(entity)
	  		);
	  	});
  }
  
  /**
   * Scheduled method that reads temperature and humidity data from the sensor every minute,
   * updates the current temperature bean, saves the data to the repository,
   * and sends updates via WebSocket.
   */
  @Scheduled(fixedRate = 60000L)
  public void readTemperature() {
    try {
      SensorResponse response = (SensorResponse)this.restTemplate.getForObject(this.sensorUrl, SensorResponse.class, new Object[0]);
      if (response != null) {
        this.messagingTemplate.convertAndSend("/topic/SensorResponse", response);
        currentTemperatureBean.setLatestSensorResponse(response);
        TemperatureMeassurementEntity entity = SensorResponseToTemperatureMessurementEntityConverter.convert(response);
        this.repository.save(entity);
        LOGGER.info("Read temperature: {} humidity: {}%", entity.getTemperature(), entity.getHumidity());
      } else {
        LOGGER.warn("Sensor response is null");
      } 
    } catch (ResourceAccessException e) {
      if (e.getCause() instanceof java.net.SocketTimeoutException) {
        LOGGER.error("Timeout while reading temperature from sensor (>{}ms)", Integer.valueOf(this.sensorTimeout));
      } else {
        LOGGER.error("Resource access error while reading temperature from sensor", (Throwable)e);
      } 
    } catch (Exception e) {
      LOGGER.error("Failed to read temperature from sensor", e);
      LOGGER.debug("Error details: ", e);
    } 
  }
}