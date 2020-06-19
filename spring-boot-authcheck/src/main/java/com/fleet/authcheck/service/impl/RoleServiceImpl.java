package com.fleet.authcheck.service.impl;

import com.fleet.authcheck.dao.BaseDao;
import com.fleet.authcheck.dao.RoleDao;
import com.fleet.authcheck.entity.Role;
import com.fleet.authcheck.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Override
    public BaseDao<Role> baseDao() {
        return roleDao;
    }
}
