package com.fleet.postgresql.service;

import com.fleet.postgresql.entity.User;

public interface UserService {

    int insert(User user);

    User get(Long id);
}
