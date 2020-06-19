package com.fleet.authcheck.service.impl;

import com.fleet.authcheck.dao.BaseDao;
import com.fleet.authcheck.page.entity.Page;
import com.fleet.authcheck.page.PageUtil;
import com.fleet.authcheck.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    public abstract BaseDao<T> baseDao();

    @Override
    public Boolean insert(T t) {
        return baseDao().insert(t) != 0;
    }

    @Override
    public Boolean delete(T t) {
        return baseDao().delete(t) != 0;
    }

    @Override
    public Boolean update(T t) {
        return baseDao().update(t) != 0;
    }

    @Override
    public T get(T t) {
        return baseDao().get(t);
    }

    @Override
    public List<T> list(Map<String, Object> map) {
        return baseDao().list(map);
    }

    @Override
    public PageUtil<T> listPage(Page page) {
        PageUtil<T> pageUtil = new PageUtil<>();
        List<T> list = baseDao().list(page);
        pageUtil.setList(list);
        pageUtil.setPage(page);
        return pageUtil;
    }
}
