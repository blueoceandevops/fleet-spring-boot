package com.fleet.simple.provider.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {

    @RequestMapping("hello")
    public String get() {
        return "hello！！！";
    }
}
