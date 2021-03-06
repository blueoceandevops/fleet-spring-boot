package com.fleet.mso.controller.user;

import com.fleet.mso.entity.User;
import com.fleet.mso.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping("/insert")
    public Boolean insert(@RequestBody User user) {
        return userService.insert(user);
    }

    @RequestMapping("/delete")
    public Boolean delete(@RequestBody User user) {
        return userService.delete(user);
    }

    @RequestMapping("/update")
    public Boolean update(@RequestBody User user) {
        return userService.update(user);
    }

    @RequestMapping("/get")
    public User get(@RequestBody User user) {
        return userService.get(user);
    }

    @RequestMapping("/list")
    public List<User> list(@RequestParam Map<String, Object> map) {
        return userService.list(map);
    }
}
