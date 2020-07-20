/*
 * This file is generated by jOOQ.
 */
package com.fleet.jooq.generator.tables.daos;


import com.fleet.jooq.generator.tables.User;
import com.fleet.jooq.generator.tables.records.UserRecord;

import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.jooq.types.UInteger;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.12"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserDao extends DAOImpl<UserRecord, com.fleet.jooq.generator.tables.pojos.User, UInteger> {

    /**
     * Create a new UserDao without any configuration
     */
    public UserDao() {
        super(User.USER, com.fleet.jooq.generator.tables.pojos.User.class);
    }

    /**
     * Create a new UserDao with an attached configuration
     */
    public UserDao(Configuration configuration) {
        super(User.USER, com.fleet.jooq.generator.tables.pojos.User.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected UInteger getId(com.fleet.jooq.generator.tables.pojos.User object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.fleet.jooq.generator.tables.pojos.User> fetchById(UInteger... values) {
        return fetch(User.USER.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.fleet.jooq.generator.tables.pojos.User fetchOneById(UInteger value) {
        return fetchOne(User.USER.ID, value);
    }

    /**
     * Fetch records that have <code>name IN (values)</code>
     */
    public List<com.fleet.jooq.generator.tables.pojos.User> fetchByName(String... values) {
        return fetch(User.USER.NAME, values);
    }
}