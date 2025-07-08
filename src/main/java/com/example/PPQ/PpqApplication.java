package com.example.PPQ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableCaching
public class PpqApplication {

	public static void main(String[] args) {
		SpringApplication.run(PpqApplication.class, args);
	}

}
