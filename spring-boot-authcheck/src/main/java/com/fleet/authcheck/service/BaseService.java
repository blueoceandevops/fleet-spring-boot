package com.fleet.authcheck.service;

import com.fleet.authcheck.page.entity.Page;
import com.fleet.authcheck.page.PageUtil;

import java.util.List;
import java.util.Map;

public interface BaseService<T> {

    Boolean insert(T t);

    Boolean delete(T t);

    Boolean update(T t);

    T get(T t);

    List<T> list(Map<String, Object> map);

    PageUtil<T> listPage(Page page);
}
