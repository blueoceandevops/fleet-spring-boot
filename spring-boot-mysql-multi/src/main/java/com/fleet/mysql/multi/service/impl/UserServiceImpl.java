package com.fleet.mysql.multi.service.impl;

import com.fleet.mysql.multi.dao.master.MasterUserDao;
import com.fleet.mysql.multi.dao.slave.SlaveUserDao;
import com.fleet.mysql.multi.entity.User;
import com.fleet.mysql.multi.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private MasterUserDao masterUserDao;

    @Resource
    private SlaveUserDao slaveUserDao;

    @Override
    public int insert(User user) {
        return masterUserDao.insert(user);
    }

    @Override
    public int delete(Long id) {
        return masterUserDao.delete(id);
    }

    @Override
    public int update(User user) {
        return masterUserDao.update(user);
    }

    @Override
    public User get(Long id) {
        return slaveUserDao.get(id);
    }

    @Override
    public List<User> list(Map<String, Object> map) {
        return slaveUserDao.list(map);
    }
}
