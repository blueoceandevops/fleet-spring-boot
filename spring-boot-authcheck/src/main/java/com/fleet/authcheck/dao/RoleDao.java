package com.fleet.authcheck.dao;

import com.fleet.authcheck.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoleDao extends BaseDao<Role> {
}
