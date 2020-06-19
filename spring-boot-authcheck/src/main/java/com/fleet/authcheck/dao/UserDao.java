package com.fleet.authcheck.dao;

import com.fleet.authcheck.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao extends BaseDao<User> {
}
