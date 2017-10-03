package com.metasoft.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metasoft.ProductProcessor;

@RestController
public class SampleController{

	@Value("${application.message:Hello World}")
	private String message = "Hello World";
	
	@Autowired
	ProductProcessor processor;

    @RequestMapping("/send")
    public String send() {
    	processor.outputProductAdd().send(MessageBuilder.withPayload(
    			new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(new Date())).build());
		return "test send";
	}


    @RequestMapping("/")
    String home() {
		return message;
	}
 
}
