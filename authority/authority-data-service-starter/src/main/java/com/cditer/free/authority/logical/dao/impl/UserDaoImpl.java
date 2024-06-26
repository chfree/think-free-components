package com.cditer.free.authority.logical.dao.impl;

import com.cditer.free.authority.data.entity.model.Department;
import com.cditer.free.authority.data.entity.model.Role;
import com.cditer.free.authority.data.entity.model.User;
import com.cditer.free.authority.data.entity.model.UserRole;
import com.cditer.free.authority.data.entity.viewmodel.UserSearch;
import com.cditer.free.authority.data.entity.viewmodel.UserView;
import com.cditer.free.authority.logical.dao.IUserDao;
import com.cditer.free.core.enums.OrderEnum;
import com.cditer.free.core.message.data.PagerModel;
import com.cditer.free.data.dao.base.ISqlExpression;
import com.cditer.free.data.dao.base.impl.SuperDao;
import com.cditer.free.data.utils.SqlExpressionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-11 22:59
 * @comment
 */

@Component
public class UserDaoImpl extends SuperDao<User> implements IUserDao {

    @Override
    public int queryCountBySearch(UserSearch search) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectCount("distinct user.id").from(User.class, "user").setMainTableAlias("user");

        if(!CollectionUtils.isEmpty(search.getRoleMarkList())){
            sqlExpression.leftJoin(UserRole.class, "userRole").on("user.id","userRole.user_id");
            sqlExpression.leftJoin(Role.class, "role").on("userRole.role_id","role.id");
        }

        appendExpression(sqlExpression, search);

        return queryCount(sqlExpression);
    }

    @Override
    public User queryModelBySearch(UserSearch search) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(User.class);

        appendExpression(sqlExpression, search);

        return queryModel(sqlExpression);
    }

    @Override
    public List<User> queryListBySearch(UserSearch search, PagerModel pagerModel) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(User.class);

        appendExpression(sqlExpression, search);

        return queryList(sqlExpression, pagerModel);
    }

    @Override
    public List<UserView> queryViewListBySearch(UserSearch search, PagerModel pagerModel) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectDistinctAllFrom(User.class, "user")
                .setMainTableAlias("user")
                .appendSelect("dept.full_name as departmentName")
                .leftJoin(Department.class, "dept")
                .on("user.department_id", "dept.id")
                .addOrder("user.create_date", OrderEnum.DESC);

        if(!CollectionUtils.isEmpty(search.getRoleMarkList())){
            sqlExpression.leftJoin(UserRole.class, "userRole").on("user.id","userRole.user_id");
            sqlExpression.leftJoin(Role.class, "role").on("userRole.role_id","role.id");
        }

        appendExpression(sqlExpression, search);

        return queryList(sqlExpression, pagerModel, UserView.class);
    }

    @Override
    public User queryModelByLogin(String account, String password) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(User.class)
                .andEq("account", account)
                .andEq("password", password);

        return queryModel(sqlExpression);
    }

    @Override
    public User queryModelByAccount(String account) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(User.class)
                .andEq("account", account);

        return queryModel(sqlExpression);
    }

    @Override
    public UserView queryViewModelById(String id) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(User.class, "user")
                .setMainTableAlias("user")
                .appendSelect("dept.full_name as departmentName")
                .leftJoin(Department.class, "dept")
                .on("user.department_id", "dept.id")
                .andEq("id", id);


        return queryModel(sqlExpression, UserView.class);
    }

    @Override
    public int queryCountByLoginUserSearch(UserSearch search) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectCount().from(User.class);

        appendExpression(sqlExpression, search);

        return queryCount(sqlExpression);
    }

    @Override
    public void appendExpression(ISqlExpression sqlExpression, UserSearch search) {
        sqlExpression.andEqNoEmpty("id", search.getId());

        sqlExpression.andEqNoEmpty("name", search.getName());

        sqlExpression.andEqNoEmpty(User::getNickName, search.getNickName());

        sqlExpression.andEqNoEmpty("account", search.getAccount());

        sqlExpression.andNotEqNoEmpty("id", search.getNotId());

        sqlExpression.andRightLikeNoEmpty("name", search.getLikeName());

        sqlExpression.andEqNoEmpty(User::getNickName, search.getLikeNickName());

        sqlExpression.andRightLikeNoEmpty("account", search.getLikeAccount());

        sqlExpression.andEqNoEmpty("status", search.getStatus());

        if(StringUtils.hasText(search.getLikeQuery())){
            sqlExpression.andWhere("(name like concat('%',#{likeQuery},'%')) or (nick_name like concat('%',#{likeQuery},'%'))")
                    .setParam("likeQuery", search.getLikeQuery());
        }

        if(!CollectionUtils.isEmpty(search.getStatusList())){
            sqlExpression.andWhereInString("status", search.getStatusList());
        }

        if(!CollectionUtils.isEmpty(search.getRoleMarkList())){
            sqlExpression.andWhereInString("role.role_mark", search.getRoleMarkList());
        }
    }
}
