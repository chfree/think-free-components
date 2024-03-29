package com.cditer.free.authority.logical.service.impl;

import com.cditer.free.authority.logical.dao.IUserRoleDao;
import com.cditer.free.authority.data.entity.model.Role;
import com.cditer.free.authority.data.entity.model.UserRole;
import com.cditer.free.authority.logical.service.IUserRoleService;
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
public class UserRoleServiceImpl extends SuperService<UserRole> implements IUserRoleService {

    @Autowired
    IUserRoleDao userRoleDao;

    @Override
    public boolean saveUserRoles(String userId,List<UserRole> userRoleList) {
        if(!deleteByUserId(userId)){
            return false;
        }
        if(userRoleList==null||userRoleList.size()<=0){
            return true;
        }
        return insertListEx(userRoleList) == userRoleList.size();
    }

    @Override
    public boolean deleteByUserId(String userId) {
        return userRoleDao.deleteByUserId(userId);
    }

    @Override
    public List<Role> queryListByUserId(String userId) {
        return userRoleDao.queryListByUserId(userId);
    }
}
