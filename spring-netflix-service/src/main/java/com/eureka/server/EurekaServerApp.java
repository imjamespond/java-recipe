package com.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.core.env.AbstractEnvironment;

/**
 *
 * @author Gunnar Hillert
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp {

	public static void main(String[] args) throws Exception {
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "server"); 
		SpringApplication.run(EurekaServerApp.class, args);
	}

}