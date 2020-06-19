package com.fleet.authcheck.service.impl;

import com.fleet.authcheck.dao.BaseDao;
import com.fleet.authcheck.dao.RolePermitDao;
import com.fleet.authcheck.entity.RolePermit;
import com.fleet.authcheck.service.RolePermitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service
public class RolePermitServiceImpl extends BaseServiceImpl<RolePermit> implements RolePermitService {

    @Resource
    private RolePermitDao rolePermitDao;

    @Override
    public BaseDao<RolePermit> baseDao() {
        return rolePermitDao;
    }
}
