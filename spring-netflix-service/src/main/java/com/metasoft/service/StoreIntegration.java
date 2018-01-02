package com.metasoft.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StoreIntegration {

    @HystrixCommand(fallbackMethod = "defaultStores")
    public Object getStores(Map<String, Object> parameters) throws Exception {
        //do stuff that might fail
        //throw new Exception("oops");
        return "good";
    }

    public Object defaultStores(Map<String, Object> parameters) {
        return "foobar";
    }
}