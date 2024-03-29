package com.cditer.free.authority.logical.dao.impl;

import com.cditer.free.authority.logical.dao.IUserRoleDao;
import com.cditer.free.authority.data.entity.model.Role;
import com.cditer.free.authority.data.entity.model.UserRole;
import com.cditer.free.data.dao.base.ISqlExpression;
import com.cditer.free.data.dao.base.impl.SuperDao;
import com.cditer.free.data.utils.SqlExpressionFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-11 23:14
 * @comment
 */

@Component
public class UserRoleDaoImpl extends SuperDao<UserRole> implements IUserRoleDao {
    @Override
    public boolean deleteByUserId(String userId) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.delete()
                     .from(UserRole.class)
                     .andEq("user_id",userId);

        return delete(sqlExpression) >= 0;
    }

    @Override
    public List<Role> queryListByUserId(String userId) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.select("role.id,role.role_name")
                     .from(UserRole.class,"ur")
                     .leftJoin(Role.class,"role").on("ur.role_id","role.id")
                     .andEq("ur.user_id",userId);

        return queryList(sqlExpression,Role.class);
    }
}
