package com.fleet.validator.controller;

import com.fleet.validator.entity.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/insert")
    public void insert(@RequestBody @Validated User user) {
    }

    @RequestMapping("/delete")
    public void delete(Long id) {
    }

    @RequestMapping("/update")
    public void update(@RequestBody User user) {
    }

    @RequestMapping("/get")
    public User get(Long id) {
        return null;
    }

    @RequestMapping("/list")
    public List<User> list(Map<String, Object> map) {
        return new ArrayList<>();
    }
}
