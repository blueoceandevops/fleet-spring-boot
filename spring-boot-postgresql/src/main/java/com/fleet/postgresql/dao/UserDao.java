package com.fleet.postgresql.dao;

import com.fleet.postgresql.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    Integer insert(User user);

    User get(Long id);
}
