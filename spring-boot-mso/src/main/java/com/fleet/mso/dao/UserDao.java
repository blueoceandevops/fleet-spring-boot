package com.fleet.mso.dao;

import com.fleet.mso.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserDao {

    int insert(User user);

    int delete(User user);

    int update(User user);

    User get(User user);

    List<User> list(Map<String, Object> map);
}
