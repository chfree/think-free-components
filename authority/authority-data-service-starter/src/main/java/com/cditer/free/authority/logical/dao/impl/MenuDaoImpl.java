package com.cditer.free.authority.logical.dao.impl;

import com.cditer.free.authority.data.entity.viewmodel.MenuRoute;
import com.cditer.free.authority.data.entity.viewmodel.MenuSearch;
import com.cditer.free.authority.data.entity.viewmodel.MenuTree;
import com.cditer.free.authority.logical.dao.IMenuDao;
import com.cditer.free.authority.data.entity.model.GroupFunc;
import com.cditer.free.authority.data.enums.MenuType;
import com.cditer.free.authority.data.enums.MenuFuncType;
import com.cditer.free.authority.data.entity.model.Menu;
import com.cditer.free.authority.data.entity.model.RoleFunc;
import com.cditer.free.core.enums.OrderEnum;
import com.cditer.free.data.dao.base.ISqlExpression;
import com.cditer.free.data.dao.base.impl.SuperDao;
import com.cditer.free.data.utils.SqlExpressionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-11 23:13
 * @comment
 */

@Component
public class MenuDaoImpl extends SuperDao<Menu> implements IMenuDao {

    @Override
    public int queryCountBySearch(MenuSearch search) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectCount().from(Menu.class);

        appendExpression(sqlExpression,search);

        return queryCount(sqlExpression);
    }

    @Override
    public List<MenuTree> queryListTreeBySearch(MenuSearch search) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(Menu.class)
                     .addOrder("sort_code", OrderEnum.ASC);

        appendExpression(sqlExpression,search);

        return queryList(sqlExpression,MenuTree.class);
    }

    @Override
    public MenuTree queryModelTree(String id) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(Menu.class,"menu")
                .appendSelect("parent.title as parentName")
                .leftJoin(Menu.class,"parent")
                .on("menu.parent_id","parent.id")
                .andEq("menu.id",id);

        return queryModel(sqlExpression, MenuTree.class);
    }

    @Override
    public List<MenuRoute> queryMenuRouteByRoleIds(List<String> roleIds) {
        if(CollectionUtils.isEmpty(roleIds)){
            return null;
        }
        ISqlExpression sqlExpression = roleSql(roleIds);
        return queryList(sqlExpression,MenuRoute.class);
    }

    private ISqlExpression roleSql(List<String> roleIds){
        if(CollectionUtils.isEmpty(roleIds)){
            return null;
        }

        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.select("distinct menu.id,menu.name,menu.title,menu.icon,menu.hidden,menu.path,menu.page_path,menu.lang_path,menu.type,menu.use_type,menu.parent_id,menu.theme,menu.sort_code,menu.delete_mark,menu.comments,menu.menu_mark,menu.level,menu.server_name")
                .from(RoleFunc.class,"roleFunc")
                .leftJoin(Menu.class,"menu").on("roleFunc.func_id","menu.id")
                .andWhereInString("role_id",roleIds)
                .andEq("func_type", MenuFuncType.MENU)
                .andWhereInString("menu.type", MenuType.MENU,MenuType.ROUTER)
                .addOrder("menu.sort_code",OrderEnum.ASC);

        return  sqlExpression;
    }

    private ISqlExpression groupSql(List<String> groupIds){
        if(CollectionUtils.isEmpty(groupIds)){
            return null;
        }

        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.select("distinct menu.id,menu.name,menu.title,menu.icon,menu.hidden,menu.path,menu.page_path,menu.lang_path,menu.type,menu.use_type,menu.parent_id,menu.theme,menu.sort_code,menu.delete_mark,menu.comments,menu.menu_mark,menu.level,menu.server_name")
                .from(GroupFunc.class,"groupFunc")
                .leftJoin(Menu.class,"menu").on("groupFunc.func_id","menu.id")
                .andWhereInString("group_id",groupIds)
                .andEq("func_type", MenuFuncType.MENU)
                .andWhereInString("menu.type", MenuType.MENU,MenuType.ROUTER)
                .addOrder("menu.sort_code",OrderEnum.ASC);

        return  sqlExpression;
    }

    @Override
    public List<MenuRoute> queryMenuRouteByRGIds(List<String> roleIds, List<String> groupIds) {
        ISqlExpression roleSqlExpression = roleSql(roleIds);
        ISqlExpression groupSqlExpression = groupSql(groupIds);

        // 同为null，则返回null
        if(roleSqlExpression==null&&groupSqlExpression==null){
            return null;
        }

        // group为null则返回role
        if(groupSqlExpression==null){
            return queryList(roleSqlExpression,MenuRoute.class);
        }

        // role为null则返回group
        if(roleSqlExpression==null){
            return queryList(groupSqlExpression,MenuRoute.class);
        }

        // 同不为null，则进行union
        ISqlExpression unionSqlExpression = SqlExpressionFactory.createExpression();
        unionSqlExpression.union(roleSqlExpression, groupSqlExpression);

        // 进行union
        ISqlExpression unionOrderSqlExpression = SqlExpressionFactory.createExpression();
        unionOrderSqlExpression.addBody("select * from ("+unionSqlExpression.toSql()+") odrTmp")
                .setParamAll(unionSqlExpression.getParams())
                .addOrder("odrTmp.sort_code",OrderEnum.ASC);

        return queryList(unionOrderSqlExpression,MenuRoute.class);
    }

    @Override
    public List<Menu> queryMenuBySeverNameAndRGIds(String serverName, List<String> roleIds, List<String> groupIds) {
        ISqlExpression roleSqlExpression = roleSql(roleIds);
        ISqlExpression groupSqlExpression = groupSql(groupIds);

        // 同为null，则返回null
        if(roleSqlExpression==null&&groupSqlExpression==null){
            return null;
        }

        // group为null则返回role
        if(groupSqlExpression==null){
            roleSqlExpression.appendSelect("menu.server_name").andEq("server_name", serverName);
            return queryList(roleSqlExpression,Menu.class);
        }

        // role为null则返回group
        if(roleSqlExpression==null){
            groupSqlExpression.appendSelect("menu.server_name").andEq("server_name", serverName);
            return queryList(groupSqlExpression,Menu.class);
        }

        roleSqlExpression.appendSelect("menu.server_name").andEq("server_name", serverName);
        groupSqlExpression.appendSelect("menu.server_name").andEq("server_name", serverName);

        // 同不为null，则进行union
        ISqlExpression unionSqlExpression = SqlExpressionFactory.createExpression();
        unionSqlExpression.union(roleSqlExpression, groupSqlExpression);

        // 进行union
        ISqlExpression unionOrderSqlExpression = SqlExpressionFactory.createExpression();
        unionOrderSqlExpression.addBody("select * from ("+unionSqlExpression.toSql()+") odrTmp")
                .setParamAll(unionSqlExpression.getParams())
                .addOrder("odrTmp.sort_code",OrderEnum.ASC);

        return queryList(unionOrderSqlExpression,Menu.class);
    }


    private void appendExpression(ISqlExpression sqlExpression, MenuSearch search){
        sqlExpression.andEqNoEmpty("id",search.getId());

        sqlExpression.andEqNoEmpty("name",search.getName());

        sqlExpression.andNotEqNoEmpty("id",search.getNotId());

        sqlExpression.andRightLikeNoEmpty("name",search.getLikeName());

        sqlExpression.andEqNoEmpty("title",search.getTitle());

        sqlExpression.andRightLikeNoEmpty("title",search.getLikeTitle());
    }
}
