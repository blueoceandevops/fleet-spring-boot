package com.fleet.mysql.service.impl;

import com.fleet.mysql.entity.User;
import com.fleet.mysql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(User user) {
        String sql = "insert into `user`(`name`) value (?)";
        return jdbcTemplate.update(sql, user.getName());
    }

    @Override
    public int delete(Long id) {
        String sql = "delete from `user` where `id` = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int update(User user) {
        String sql = "update `user` set `name` = ? where `id` = ?";
        return jdbcTemplate.update(sql, user.getName(), user.getId());
    }

    @Override
    public User get(Long id) {
        String sql = "select `id`, `name` from `user` where `id` = ?";
        return jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                return user;
            }
        }, id);
    }

    @Override
    public List<Map<String, Object>> list() {
        String sql = "select `id`, `name` from `user`";
        return jdbcTemplate.queryForList(sql);
    }
}
