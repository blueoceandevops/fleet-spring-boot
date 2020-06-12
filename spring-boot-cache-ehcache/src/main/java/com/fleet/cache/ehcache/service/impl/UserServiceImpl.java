package com.fleet.cache.ehcache.service.impl;

import com.fleet.cache.ehcache.dao.UserDao;
import com.fleet.cache.ehcache.service.UserService;
import com.fleet.cache.ehcache.entity.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = {"users"})
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public int insert(User user) {
        return userDao.insert(user);
    }

    @Override
    @CacheEvict(key = "#id")
    public int delete(Long id) {
        return userDao.delete(id);
    }

    @Override
    @CacheEvict(key = "#user.id")
    public int update(User user) {
        return userDao.update(user);
    }

    @Override
    @Cacheable(key = "#id")
    public User get(Long id) {
        return userDao.get(id);
    }

    @Override
    public List<User> list(Map<String, Object> map) {
        return userDao.list(map);
    }
}
