package com.cditer.free.param.logical.service.impl;

import com.cditer.free.param.data.entity.model.ParamSetting;
import com.cditer.free.param.data.entity.viewmodel.ParamSettingSearch;
import com.cditer.free.param.logical.dao.IParamSettingDao;
import com.cditer.free.param.logical.service.IParamSettingService;
import com.cditer.free.core.message.data.PagerModel;
import com.cditer.free.data.dao.base.impl.SuperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * @author      auto build code by think
 * @email       chfree001@gmail.com
 * @createtime  2020-08-26 11:30:21
 * @comment     参数配置表
 */

@Component
public class ParamSettingServiceImpl extends SuperService<ParamSetting> implements IParamSettingService {
    @Autowired
    IParamSettingDao paramSettingDao;

    @Override
    public int queryCountBySearch(ParamSettingSearch search) {
        return paramSettingDao.queryCountBySearch(search);
    }

    @Override
    public List<ParamSetting> queryListBySearch(ParamSettingSearch search, PagerModel pagerModel) {
        return paramSettingDao.queryListBySearch(search,pagerModel);
    }

    @Override
    public ParamSetting queryModelByName(String name) {
        ParamSettingSearch search = new ParamSettingSearch();
        search.setName(name);

        return paramSettingDao.queryModelBySearch(search);
    }

    @Override
    public String queryStrValue(String name) {
        ParamSetting paramSetting = queryModelByName(name);
        if(paramSetting==null){
            return null;
        }
        return paramSetting.getParamValue();
    }

    @Override
    public String queryStrValue(String name, String defaultValue) {
        ParamSetting paramSetting = queryModelByName(name);
        if(paramSetting==null){
            return defaultValue;
        }
        if(StringUtils.isEmpty(paramSetting.getParamValue())){
            return defaultValue;
        }
        return paramSetting.getParamValue();
    }

    @Override
    public int queryIntValue(String name) {
        return queryIntValue(name,0);
    }

    @Override
    public int queryIntValue(String name, int defaultValue) {
        ParamSetting paramSetting = queryModelByName(name);
        if(paramSetting==null){
            return defaultValue;
        }
        return paramSetting.getIntValue(defaultValue);
    }

    @Override
    public double queryDoubleValue(String name) {
        return queryDoubleValue(name,0);
    }

    @Override
    public double queryDoubleValue(String name, double defaultValue) {
        ParamSetting paramSetting = queryModelByName(name);
        if(paramSetting==null){
            return defaultValue;
        }
        return paramSetting.getDoubleValue(defaultValue);
    }

    @Override
    public float queryFloatValue(String name) {
        return queryFloatValue(name,0);
    }

    @Override
    public float queryFloatValue(String name, float defaultValue) {
        ParamSetting paramSetting = queryModelByName(name);
        if(paramSetting==null){
            return defaultValue;
        }
        return paramSetting.getFloatValue(defaultValue);
    }

}