package com.fleet.provider.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @RequestMapping("hello")
    public String get() {
        return "hello！！！";
    }
}
