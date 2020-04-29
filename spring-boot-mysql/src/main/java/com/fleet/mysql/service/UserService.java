package com.fleet.mysql.service;

import com.fleet.mysql.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface UserService {

    int insert(User user);

    int delete(Long id);

    int update(User user);

    User get(Long id);

    List<Map<String, Object>> list();
}
