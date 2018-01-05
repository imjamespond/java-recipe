package com.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("test-client")
public interface SparkClient {
    @RequestMapping(method = RequestMethod.GET, value = "/db-test")
    void dbTest();

}