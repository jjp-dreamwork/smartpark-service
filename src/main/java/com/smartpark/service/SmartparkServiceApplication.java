package com.smartpark.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class SmartparkServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartparkServiceApplication.class, args);
	}

}
