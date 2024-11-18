package com.example.weatherpredictor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class WeatherpredictorApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherpredictorApplication.class, args);
		log.debug("Application Begins");
		log.info("Application Begins");
		log.warn("Application Begins");
		log.error("Application Begins");
	}

}
