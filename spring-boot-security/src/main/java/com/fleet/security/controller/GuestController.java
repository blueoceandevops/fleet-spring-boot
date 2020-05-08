package com.fleet.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest")
public class GuestController {

    @RequestMapping("/get")
    public String get() {
        return "欢迎进入，游客";
    }
}
