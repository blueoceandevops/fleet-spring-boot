package com.fleet.ehcache.service.impl;

import com.fleet.ehcache.dao.UserDao;
import com.fleet.ehcache.entity.User;
import com.fleet.ehcache.service.UserService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    @CachePut(value = "users", key = "#user.id")
    public int insert(User user) {
        return userDao.insert(user);
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public int delete(Long id) {
        return userDao.delete(id);
    }

    @Override
    @CacheEvict(value = "users", key = "#user.id")
    public int update(User user) {
        return userDao.update(user);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public User get(Long id) {
        return userDao.get(id);
    }

    @Override
    public List<User> list(Map<String, Object> map) {
        return userDao.list(map);
    }
}
