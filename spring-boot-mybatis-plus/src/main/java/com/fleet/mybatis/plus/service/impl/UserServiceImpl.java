package com.fleet.mybatis.plus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fleet.mybatis.plus.dao.UserDao;
import com.fleet.mybatis.plus.entity.User;
import com.fleet.mybatis.plus.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Override
    public Page<User> listPage(Page<User> page, Map<String, Object> map) {
        page.setRecords(baseMapper.list(page, map));
        return page;
    }
}
