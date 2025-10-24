package ch.mabaka.birdbox.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BirdboxWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(BirdboxWebsiteApplication.class, args);
	}

}