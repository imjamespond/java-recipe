package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@RestController
public class Hello {
    //static String uuid = UUID.randomUUID().toString();

    @RequestMapping("/")
    public String home() throws UnknownHostException {
        return "Hello Docker World, from: "+ InetAddress.getLocalHost().getHostName();
    }

    public static void main(String[] args) {
        SpringApplication.run(Hello.class, args);
    }
}