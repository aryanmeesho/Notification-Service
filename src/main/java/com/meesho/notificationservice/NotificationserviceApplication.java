package com.meesho.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NotificationserviceApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationserviceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(NotificationserviceApplication.class, args);
		LOGGER.info("Notification Service running...");
	}

}
