package com.fleet.cxf.service.impl;

import com.fleet.cxf.entity.User;
import com.fleet.cxf.service.UserService;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.util.HashMap;
import java.util.Map;

@Component
@WebService(serviceName = "userService", targetNamespace = "http://service.cxf.fleet.com")
public class UserServiceImpl implements UserService {

    private Map<Long, User> users = new HashMap<>();

    public UserServiceImpl() {
        User user = new User();
        user.setId(1L);
        user.setName("test1");
        users.put(user.getId(), user);

        user = new User();
        user.setId(2L);
        user.setName("test2");
        users.put(user.getId(), user);

        user = new User();
        user.setId(3L);
        user.setName("test3");
        users.put(user.getId(), user);
    }

    @Override
    public User get(Long id) {
        return users.get(id);
    }

    @Override
    public String getName(Long id) {
        return users.get(id).getName();
    }
}
