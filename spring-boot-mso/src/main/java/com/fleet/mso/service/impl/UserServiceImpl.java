package com.fleet.mso.service.impl;

import com.fleet.mso.dao.UserDao;
import com.fleet.mso.entity.User;
import com.fleet.mso.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public Boolean insert(User user) {
        return userDao.insert(user) != 0;
    }

    @Override
    public Boolean delete(User user) {
        return userDao.delete(user) != 0;
    }

    @Override
    public Boolean update(User user) {
        return userDao.update(user) != 0;
    }

    @Override
    public User get(User user) {
        return userDao.get(user);
    }

    @Override
    public List<User> list(Map<String, Object> map) {
        return userDao.list(map);
    }
}
