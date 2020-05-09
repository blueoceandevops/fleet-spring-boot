package com.fleet.oauth2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/order/{id}")
    public String getOrder(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("isAuthenticated:{" + authentication.isAuthenticated() + "},name:{" + authentication.getName() + "}");
        return "order id : " + id;
    }
}
