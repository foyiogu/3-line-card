package com.unionbankng.future.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

	Logger logger = Logger.getLogger(EurekaServerApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("Africa/Lagos"));   // Set WAT timezone
		logger.log(Level.FINER,"Spring boot application running in UTC timezone :"+new Date());   // show timezone on startup
	}

}
