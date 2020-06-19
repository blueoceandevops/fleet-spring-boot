package com.fleet.authcheck.dao;

import com.fleet.authcheck.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserRoleDao extends BaseDao<UserRole> {

    List<String> roleList(Integer userId);

    List<String> permitList(Integer userId);
}
