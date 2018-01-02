package com.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("dummy-client")
public interface StoreClient {
    @RequestMapping(method = RequestMethod.GET, value = "/stores")
    List<String> getStores();

    @RequestMapping(method = RequestMethod.POST, value = "/stores/{storeId}", consumes = "application/json")
    String update(@PathVariable("storeId") Long storeId, String store);
}