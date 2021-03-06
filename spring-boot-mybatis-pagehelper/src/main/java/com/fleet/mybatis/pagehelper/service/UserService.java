package com.fleet.mybatis.pagehelper.service;

import com.fleet.mybatis.pagehelper.entity.Page;
import com.fleet.mybatis.pagehelper.entity.PageUtil;
import com.fleet.mybatis.pagehelper.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    Boolean insert(User user);

    Boolean delete(User user);

    Boolean update(User user);

    User get(User user);

    List<User> list(Map<String, Object> map);

    PageUtil<User> listPage(Page page);
}
