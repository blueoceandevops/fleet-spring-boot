package com.fleet.shiro.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/get")
    public String get() {
        return "欢迎进入，admin用户！";
    }
}
