package com.fleet.authcheck.service;

import com.fleet.authcheck.entity.User;

public interface UserService extends BaseService<User> {

    Boolean hasRoles(Integer userId, String[] roles);

    Boolean hasPermits(Integer userId, String[] permits);
}
