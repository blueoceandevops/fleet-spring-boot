package com.fleet.authcheck.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao<T> {

    Integer insert(T t);

    Integer delete(T t);

    Integer update(T t);

    T get(T t);

    List<T> list(Map<String, Object> map);
}
