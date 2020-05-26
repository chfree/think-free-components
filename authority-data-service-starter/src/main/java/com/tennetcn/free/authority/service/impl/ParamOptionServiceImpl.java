package com.tennetcn.free.authority.service.impl;

import com.tennetcn.free.authority.dao.IParamOptionDao;
import com.tennetcn.free.authority.data.entity.model.ParamOption;
import com.tennetcn.free.authority.data.entity.viewmodel.ParamOptionSearch;
import com.tennetcn.free.authority.data.entity.viewmodel.ParamOptionView;
import com.tennetcn.free.authority.service.IParamOptionService;
import com.tennetcn.free.core.message.data.PagerModel;
import com.tennetcn.free.data.dao.base.impl.SuperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-11 23:13
 * @comment
 */

@Component
public class ParamOptionServiceImpl extends SuperService<ParamOption> implements IParamOptionService {

    @Autowired
    IParamOptionDao paramOptionDao;

    @Override
    public int queryCountBySearch(ParamOptionSearch search) {
        return paramOptionDao.queryCountBySearch(search);
    }

    @Override
    public List<ParamOption> queryListBySearch(ParamOptionSearch search, PagerModel pagerModel) {
        return paramOptionDao.queryListBySearch(search,pagerModel);
    }

    @Override
    public List<ParamOption> queryListByDefineName(String defineName) {
        return paramOptionDao.queryListByDefineName(defineName);
    }

    @Override
    public List<ParamOptionView> queryListByDefineNames(List<String> defineNames) {
        return paramOptionDao.queryListByDefineNames(defineNames);
    }

    @Override
    public Map<Object, List<ParamOption>> queryListGroupByDefineNames(List<String> defineNames) {
        if(defineNames == null || defineNames.size()<=0){
            return null;
        }

        List<ParamOptionView> paramOptionViews = queryListByDefineNames(defineNames);
        // 进行分组
        Map<Object, List<ParamOption>> resultMap = new HashMap<>();
        for (ParamOptionView paramOptionView : paramOptionViews) {
            resultMap.computeIfAbsent(paramOptionView.getDefineName(), k -> new ArrayList<>()).add(paramOptionView);
        }

        return resultMap;
    }

    @Override
    public ParamOption queryModel(ParamOptionSearch search) {
        return paramOptionDao.queryModel(search);
    }

    @Override
    public ParamOption queryModel(String defineName, String text) {
        ParamOptionSearch search = new ParamOptionSearch();
        search.setDefineName(defineName);
        search.setText(text);

        return queryModel(search);
    }
}
