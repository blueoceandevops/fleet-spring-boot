package com.fleet.cache.caffeine.service;

import com.fleet.cache.caffeine.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User insert(User user);

    int delete(Long id);

    int update(User user);

    User get(Long id);

    List<User> list(Map<String, Object> map);
}
