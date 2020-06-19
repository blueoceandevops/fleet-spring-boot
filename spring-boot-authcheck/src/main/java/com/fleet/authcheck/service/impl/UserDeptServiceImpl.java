package com.fleet.authcheck.service.impl;

import com.fleet.authcheck.dao.BaseDao;
import com.fleet.authcheck.dao.PermitDao;
import com.fleet.authcheck.entity.Permit;
import com.fleet.authcheck.service.PermitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service
public class UserDeptServiceImpl extends BaseServiceImpl<Permit> implements PermitService {

    @Resource
    private PermitDao permitDao;

    @Override
    public BaseDao<Permit> baseDao() {
        return permitDao;
    }
}
