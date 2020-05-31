package com.tennetcn.free.authority.service.impl;


import com.tennetcn.free.authority.dao.IGroupDao;
import com.tennetcn.free.authority.service.IGroupService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.tennetcn.free.data.dao.base.impl.SuperService;
import com.tennetcn.free.authority.data.entity.model.Group;
import com.tennetcn.free.authority.data.entity.viewmodel.GroupSearch;
import com.tennetcn.free.core.message.data.PagerModel;
import java.util.List;


/**
 * @author      auto build code by think
 * @email       chfree001@gmail.com
 * @createtime  2020-05-31 12:50:03
 * @comment     权限组
 */

@Component
public class GroupServiceImpl extends SuperService<Group> implements IGroupService {
    @Autowired
    IGroupDao groupDao;

    @Override
    public int queryCountBySearch(GroupSearch search) {
        return groupDao.queryCountBySearch(search);
    }

    @Override
    public List<Group> queryListBySearch(GroupSearch search, PagerModel pagerModel) {
        return groupDao.queryListBySearch(search,pagerModel);
    }

}