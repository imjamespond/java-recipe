package com.metasoft.controller;

import com.metasoft.service.StoreIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TestController {

    @RequestMapping("/")
    public String home() {
        return "Hello world";
    }

    @RequestMapping("/stores")
    List<String> getStores() throws Exception {
        //throw new Exception("oops");
        return Arrays.asList("foo","bar");
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/service-url")
    public String serviceUrl() {
        List<ServiceInstance> list = discoveryClient.getInstances("DUMMY-CLIENT");
        if (list != null && list.size() > 0 ) {
            return list.get(0).getUri().toString();
        }
        return null;
    }

    @Autowired
    StoreIntegration store;

    @RequestMapping("/test-store")
    public Object testStore() throws Exception {
        return store.getStores(null);
    }

}
