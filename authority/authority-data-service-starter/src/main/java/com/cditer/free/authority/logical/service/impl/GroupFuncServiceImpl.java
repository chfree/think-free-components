package com.cditer.free.authority.logical.service.impl;

import cn.hutool.core.util.IdUtil;
import com.cditer.free.authority.data.entity.viewmodel.GroupFuncSearch;
import com.cditer.free.authority.logical.dao.IGroupFuncDao;
import com.cditer.free.authority.data.entity.model.GroupFunc;
import com.cditer.free.authority.logical.service.IGroupFuncService;
import com.cditer.free.data.dao.base.impl.SuperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-11 23:14
 * @comment
 */

@Component
public class GroupFuncServiceImpl extends SuperService<GroupFunc> implements IGroupFuncService {

    @Autowired
    IGroupFuncDao groupFuncDao;

    @Override
    public boolean saveGroupFuncs(String groupId, List<GroupFunc> groupFuncs) {
        if(!deleteByGroupId(groupId)){
            return false;
        }
        if(groupFuncs==null||groupFuncs.size()<=0){
            return true;
        }
        groupFuncs.forEach(groupFunc -> {
            groupFunc.setId(IdUtil.randomUUID());
            groupFunc.setGroupId(groupId);
        });

        return insertListEx(groupFuncs)==groupFuncs.size();
    }

    @Override
    public boolean deleteByGroupId(String groupId) {
        return groupFuncDao.deleteByGroupId(groupId);
    }

    @Override
    public List<GroupFunc> queryListBySearch(GroupFuncSearch search) {
        return groupFuncDao.queryListBySearch(search);
    }
}
