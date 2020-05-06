package com.fleet.mongodb.service;

import com.fleet.mongodb.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends MongoRepository<User, Long> {

    public User findByName(String name);

}
