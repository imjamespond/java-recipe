package com.metasoft.boot;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletInitializer extends SpringBootServletInitializer {
	ServletInitializer(){
		System.out.println("ServletInitializer");
	}
}