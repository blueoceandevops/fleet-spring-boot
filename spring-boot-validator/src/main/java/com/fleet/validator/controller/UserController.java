package com.fleet.validator.controller;

import com.fleet.validator.entity.User;
import com.fleet.validator.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserRepository userRepository;

    @RequestMapping("/insert")
    public void insert(@RequestBody @Valid User user) {
        userRepository.save(user);
    }

    @RequestMapping("/list")
    public Iterable<User> list() {
        return userRepository.findAll();
    }
}
