package com.zuul;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.core.env.AbstractEnvironment;

@EnableEurekaClient
@EnableAutoConfiguration
@EnableZuulProxy
public class ZuulApp {
	public static void main(String[] args) {
    	System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "zuul"); 
        new SpringApplicationBuilder(ZuulApp.class).web(true).run(args);
    }
}
