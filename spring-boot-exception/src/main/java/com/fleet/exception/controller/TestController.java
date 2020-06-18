package com.fleet.exception.controller;

import com.fleet.exception.handler.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/get")
    public String get() {
        return "hello！！！";
    }

    @RequestMapping("/error")
    public String error() {
        throw new BaseException();
    }
}
