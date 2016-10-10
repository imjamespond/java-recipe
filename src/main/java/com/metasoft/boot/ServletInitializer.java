package com.metasoft.boot;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletInitializer extends SpringBootServletInitializer {
	@Bean
	public FilterRegistrationBean siteMeshFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new SiteMeshFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}
}
