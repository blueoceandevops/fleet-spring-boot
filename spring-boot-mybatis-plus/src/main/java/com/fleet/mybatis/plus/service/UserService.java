package com.fleet.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fleet.mybatis.plus.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {

    Page<User> listPage(Page<User> page, Map<String, Object> map);
}
