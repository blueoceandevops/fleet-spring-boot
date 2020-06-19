package com.fleet.authcheck.service.impl;

import com.fleet.authcheck.dao.BaseDao;
import com.fleet.authcheck.dao.UserRoleDao;
import com.fleet.authcheck.entity.UserRole;
import com.fleet.authcheck.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public BaseDao<UserRole> baseDao() {
        return userRoleDao;
    }

    @Override
    public List<String> roleList(Integer userId) {
        return userRoleDao.roleList(userId);
    }

    @Override
    public List<String> permitList(Integer userId) {
        return userRoleDao.permitList(userId);
    }
}
