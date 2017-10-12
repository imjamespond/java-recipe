package com.metasoft.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SampleController{

	@Value("${application.message:Hello World}")
	private String message = "Hello World";
	

    @RequestMapping("/send")
    public String send() {
		return "test send";
	}


    @RequestMapping("/")
    String home() {
		return message;
	}
 
}
