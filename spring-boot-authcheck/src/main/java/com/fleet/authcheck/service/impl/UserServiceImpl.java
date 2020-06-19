package com.fleet.authcheck.service.impl;

import com.fleet.authcheck.dao.BaseDao;
import com.fleet.authcheck.dao.UserDao;
import com.fleet.authcheck.entity.User;
import com.fleet.authcheck.service.UserRoleService;
import com.fleet.authcheck.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private UserRoleService userRoleService;

    @Override
    public BaseDao<User> baseDao() {
        return userDao;
    }

    @Override
    public Boolean hasRoles(Integer userId, String[] roles) {
        if (roles != null && roles.length != 0) {
            List<String> roleList = userRoleService.roleList(userId);
            if (roleList != null && roleList.size() != 0) {
                roleList.retainAll(Arrays.asList(roles));
                if (roleList.size() != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean hasPermits(Integer userId, String[] permits) {
        if (permits != null && permits.length != 0) {
            List<String> permitList = userRoleService.permitList(userId);
            if (permitList != null && permitList.size() != 0) {
                permitList.retainAll(Arrays.asList(permits));
                if (permitList.size() != 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
