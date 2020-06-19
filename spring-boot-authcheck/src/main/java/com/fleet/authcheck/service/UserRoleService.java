package com.fleet.authcheck.service;

import com.fleet.authcheck.entity.UserRole;

import java.util.List;

public interface UserRoleService extends BaseService<UserRole> {

    List<String> roleList(Integer userId);

    List<String> permitList(Integer userId);
}
