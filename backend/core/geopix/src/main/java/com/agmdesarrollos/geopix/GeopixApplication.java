package com.agmdesarrollos.geopix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GeopixApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeopixApplication.class, args);
	}

}
