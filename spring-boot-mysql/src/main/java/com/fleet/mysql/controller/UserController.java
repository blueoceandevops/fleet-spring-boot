package com.fleet.mysql.controller;

import com.fleet.mysql.entity.User;
import com.fleet.mysql.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/insert")
    public void save(User user) {
        userService.insert(user);
    }

    @RequestMapping("/delete")
    public void delete(Long id) {
        userService.delete(id);
    }

    @RequestMapping("/update")
    public void update(User user) {
        userService.update(user);
    }

    @RequestMapping("/get")
    public void get(Long id) {
        userService.get(id);
    }

    @RequestMapping("/list")
    public List<Map<String, Object>> list() {
        return userService.list();
    }
}
