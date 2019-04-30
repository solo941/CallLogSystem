package com.lc.ssm.service.impl;

import com.lc.ssm.dao.BaseDao;
import com.lc.ssm.domain.User;
import com.lc.ssm.service.BaseService;

import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    private BaseDao<T> dao;

    public BaseDao<T> getDao() {
        return dao;
    }

    public void setDao(BaseDao<T> dao) {
        this.dao = dao;
    }

    public void insert(T t) {
        dao.insert(t);
    }

    public void update(T t) {
        dao.update(t);
    }

    public void delete(Integer id) {
        dao.delete(id);
    }

    public T selectOne(Integer id) {
        return dao.selectOne(id);
    }

    public List<T> selectAll() {
        return dao.selectAll();
    }

    public List<T> selectPage(int offset, int len) {
        return dao.selectPage(offset,len);
    }

    public int selectCount() {
        return dao.selectCount();
    }
}
