package com.metasoft.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan({"com.metasoft.service","com.metasoft.service.dao",
	"com.metasoft.controller","com.metasoft.boot"})
@EnableTransactionManagement
public class AppServletInitializer extends SpringBootServletInitializer {
	
	@Bean
	public FilterRegistrationBean siteMeshFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new SiteMeshFilter());
		filterRegistrationBean.addUrlPatterns("/home","/login","/error");
		return filterRegistrationBean;
	}
	
    @Bean
    public HttpMessageConverters gsonConverters() {
        HttpMessageConverter<?> gson = new GsonHttpMessageConverter();
        return new HttpMessageConverters(gson);
    }
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AppServletInitializer.class);
	}

}