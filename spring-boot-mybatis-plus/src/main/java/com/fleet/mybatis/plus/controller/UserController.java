package com.fleet.mybatis.plus.controller;

import com.fleet.mybatis.plus.entity.User;
import com.fleet.mybatis.plus.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/list")
    public List<User> list() {
        return userService.list();
    }
}
