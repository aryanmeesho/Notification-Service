package com.meesho.notificationservice;

import com.meesho.notificationservice.entity.enums.ErrorCodes;
import com.meesho.notificationservice.exceptions.ServiceNotAvailableException;
import com.meesho.notificationservice.utils.constants.AppConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.meesho.notificationservice", "com.meesho.instrumentation"})
@EnableCaching
public class NotificationserviceApplication {
	private static final Logger log = LoggerFactory.getLogger(NotificationserviceApplication.class);

	public static void main(String[] args){
		try {
			SpringApplication.run(NotificationserviceApplication.class, args);
			log.info("Notification Service running...");
		}
		catch (Exception exc){
			log.error("Notification Service is not running, Error : " + exc.getMessage());
		}
	}
}
