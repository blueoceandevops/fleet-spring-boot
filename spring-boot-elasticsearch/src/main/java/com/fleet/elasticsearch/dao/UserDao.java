package com.fleet.elasticsearch.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.fleet.elasticsearch.entity.User;

@Repository
public interface UserDao extends ElasticsearchRepository<User, Long> {
}
