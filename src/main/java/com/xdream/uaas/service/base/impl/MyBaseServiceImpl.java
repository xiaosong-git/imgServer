package com.xdream.uaas.service.base.impl;

import com.xdream.uaas.dao.base.IMyBaseDao;
import com.xdream.uaas.model.compose.PageModel;
import com.xdream.uaas.service.base.IMyBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
@Service("baseService")
public class MyBaseServiceImpl implements IMyBaseService {

    @Autowired
    private IMyBaseDao baseDao;


    public Map<String, Object> findById(String tableName, Integer id) {
        return baseDao.findById(tableName,id);
    }

    public int updateStatus(String tableName, Integer status, Integer id) {

        return baseDao.updateStatus(tableName,status,id);
    }

    public int update(String tableName, Map<String, Object> params) {
        return baseDao.update(tableName,params);
    }
    public int deleteOrUpdate(String sql) {
        return baseDao.deleteOrUpdate(sql);
    }

    public int delete(String tableName, Integer id) {
        return baseDao.delete(tableName,id);
    }

    public int save(String tableName, Map<String, Object> params) {
        return baseDao.save(tableName, params);
    }

    public PageModel findPage(String coloumSql, String fromSql, Integer pageNum, Integer pageSize) {
        return baseDao.findPage(coloumSql,fromSql,pageNum,pageSize);
    }

    public PageModel findPage(Integer offset, Integer pageSize, String coloumSql, String fromSql) {
        return baseDao.findPage(offset, pageSize, coloumSql, fromSql);
    }

    public List findList(String coloumSql, String fromSql) {
        return baseDao.findList(coloumSql,fromSql);
    }

    @Override
    public Map<String, Object> findFirstBySql(String sql) {
        return baseDao.findFirstBySql(sql);
    }
}