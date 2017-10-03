package com.metasoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metasoft.MultibinderApplication;

@Controller
public class SampleController{

	@Value("${application.message:Hello World}")
	private String message = "Hello World";
	
	@Autowired
	MultibinderApplication app;

    @RequestMapping("/")
    @ResponseBody
    String home() {
    	app.send();
		return message;
	}
 
}
