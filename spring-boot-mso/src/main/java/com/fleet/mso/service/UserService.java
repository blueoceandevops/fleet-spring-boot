package com.fleet.mso.service;

import com.fleet.mso.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    Boolean insert(User user);

    Boolean delete(User user);

    Boolean update(User user);

    User get(User user);

    List<User> list(Map<String, Object> map);
}
