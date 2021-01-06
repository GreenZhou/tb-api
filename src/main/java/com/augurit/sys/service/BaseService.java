package com.augurit.sys.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseService <M extends BaseMapper<T>,T> {
    @Autowired
    M mapper;

    public T getById(Long id) {
        return mapper.selectById(id);
    }

    public int save(T entity) {
        return mapper.insert(entity);
    }

    public int deleteById(Long id) {
        return mapper.deleteById(id);
    }

    public void delete(Long[] ids) {
        for(Long id : ids){
            mapper.deleteById(id);
        }
    }

    public int update(T entity) {
        return mapper.updateById(entity);
    }
}