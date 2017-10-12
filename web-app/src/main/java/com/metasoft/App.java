package com.metasoft;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.metasoft.boot.AppServletInitializer;
import com.metasoft.model.Constant;

public class App {
    public static void main(String[] args) {
    	ConfigurableApplicationContext ctx = 
    			SpringApplication.run(AppServletInitializer.class, args);
    	String contextPath = ctx.getEnvironment().getProperty("server.servlet.context-path");
    	Constant.ContextPath = contextPath;
    }
}