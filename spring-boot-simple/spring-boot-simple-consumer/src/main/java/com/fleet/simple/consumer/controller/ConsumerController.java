package com.fleet.simple.consumer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class ConsumerController {

    @Resource
    RestTemplate restTemplate;

    @RequestMapping("hello")
    public String get() {
        return restTemplate.getForObject("http://172.30.18.97:8080/hello", String.class);
    }
}
