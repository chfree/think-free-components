package com.tennetcn.free.authority.dao;

import com.tennetcn.free.authority.model.Role;
import com.tennetcn.free.authority.model.UserRole;
import com.tennetcn.free.data.dao.base.ISuperDao;

import java.util.List;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-11 23:06
 * @comment
 */

public interface IUserRoleDao extends ISuperDao<UserRole> {
    boolean deleteByUserId(String userId);

    List<Role> queryListByUserId(String userId);
}