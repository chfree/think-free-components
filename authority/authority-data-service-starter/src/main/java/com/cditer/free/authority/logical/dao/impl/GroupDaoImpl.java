package com.cditer.free.authority.logical.dao.impl;

import com.cditer.free.authority.data.entity.viewmodel.GroupSearch;
import com.cditer.free.authority.logical.dao.IGroupDao;
import com.cditer.free.authority.data.entity.model.UserGroup;
import com.cditer.free.core.enums.OrderEnum;
import com.cditer.free.data.dao.base.ISqlExpression;
import com.cditer.free.core.message.data.PagerModel;
import com.cditer.free.data.utils.SqlExpressionFactory;
import org.springframework.stereotype.Component;
import com.cditer.free.data.dao.base.impl.SuperDao;
import com.cditer.free.authority.data.entity.model.Group;

import java.util.List;


/**
 * @author      auto build code by think
 * @email       chfree001@gmail.com
 * @createtime  2020-05-31 12:47:27
 * @comment     权限组
 */

@Component
public class GroupDaoImpl extends SuperDao<Group> implements IGroupDao {
    @Override
    public int queryCountBySearch(GroupSearch search) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectCount().from(Group.class);

        appendExpression(sqlExpression,search);

        return queryCount(sqlExpression);
    }

    @Override
    public List<Group> queryListBySearch(GroupSearch search, PagerModel pagerModel) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(Group.class);

        appendExpression(sqlExpression,search);

        return queryList(sqlExpression,pagerModel);
    }

    @Override
    public List<Group> queryListByUserId(String userId) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(Group.class, "gr")
                     .leftJoin(UserGroup.class, "ur").on("gr.id","ur.group_id")
                     .andEq("ur.user_id",userId)
                     .addOrder("gr.sort_code", OrderEnum.ASC);

        return queryList(sqlExpression);

    }

    private void appendExpression(ISqlExpression sqlExpression, GroupSearch search){
        sqlExpression.andEqNoEmpty("id",search.getId());

        sqlExpression.andNotEqNoEmpty("id",search.getNotId());

        sqlExpression.andEqNoEmpty("name",search.getName());

        sqlExpression.andEqNoEmpty("description",search.getDescription());

        sqlExpression.andEqNoEmpty("group_mark",search.getGroupMark());

        sqlExpression.andEqNoEmpty("level",search.getLevel());

        sqlExpression.andEqNoEmpty("status",search.getStatus());

        sqlExpression.andLikeNoEmpty("group_mark",search.getLikeGroupMark());

        sqlExpression.andLikeNoEmpty("name",search.getLikeName());

    }
}
