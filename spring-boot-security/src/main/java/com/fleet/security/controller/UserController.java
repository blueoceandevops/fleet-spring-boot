package com.fleet.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

//    @PreAuthorize("hasAuthority('USER:GET')")
    @RequestMapping("/get")
    public String get() {
        return "欢迎进入，user用户！";
    }
}
