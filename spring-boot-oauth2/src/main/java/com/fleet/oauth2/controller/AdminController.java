package com.fleet.oauth2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

//    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/get")
    public String get() {
        return "欢迎进入，admin用户！";
    }
}
