package com.tennetcn.free.authority.dao;

import com.tennetcn.free.authority.model.Role;
import com.tennetcn.free.authority.viewmodel.RoleSearch;
import com.tennetcn.free.core.message.PagerModel;
import com.tennetcn.free.data.dao.base.ISuperDao;

import java.util.List;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-11 23:05
 * @comment
 */

public interface IRoleDao extends ISuperDao<Role> {
    int queryCountBySearch(RoleSearch search);

    List<Role> queryListBySearch(RoleSearch search, PagerModel pagerModel);

    List<String> queryListByUserId(String userId);

    List<Role> queryListRoleByUserId(String userId);
}
