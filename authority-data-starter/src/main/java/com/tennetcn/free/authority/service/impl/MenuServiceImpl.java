package com.tennetcn.free.authority.service.impl;

import com.tennetcn.free.authority.dao.IMenuDao;
import com.tennetcn.free.authority.enums.MenuType;
import com.tennetcn.free.authority.enums.RoleFuncType;
import com.tennetcn.free.authority.model.Menu;
import com.tennetcn.free.authority.model.RoleFunc;
import com.tennetcn.free.authority.service.IMenuService;
import com.tennetcn.free.authority.viewmodel.MenuRoute;
import com.tennetcn.free.authority.viewmodel.MenuSearch;
import com.tennetcn.free.authority.viewmodel.MenuTree;
import com.tennetcn.free.data.dao.base.ISqlExpression;
import com.tennetcn.free.data.dao.base.impl.SuperService;
import com.tennetcn.free.data.enums.OrderEnum;
import com.tennetcn.free.data.utils.SqlExpressionFactory;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-11 23:13
 * @comment
 */

@Component
public class MenuServiceImpl extends SuperService<Menu> implements IMenuService {

    @Autowired
    IMenuDao menuDao;

    @Override
    public int queryCountBySearch(MenuSearch search) {
        return menuDao.queryCountBySearch(search);
    }

    @Override
    public List<MenuTree> queryListTreeBySearch(MenuSearch search) {
        return menuDao.queryListTreeBySearch(search);
    }

    @Override
    public List<MenuTree> queryListTreeFormat(MenuSearch search) {
        List<MenuTree> allMenuTrees = queryListTreeBySearch(search);
        if(allMenuTrees==null||allMenuTrees.size()<=0){
            return null;
        }
        // 求出最顶级的level是多少
        // 默认顶级是0，考虑到搜索的情况，这里在求一下
        Integer minLevel = allMenuTrees.stream().min(Comparator.comparing(Menu::getLevel)).get().getLevel();

        // 在求出顶级所有的顶级部门
        var minMenuTrees = allMenuTrees.stream().filter(menu -> menu.getLevel() == minLevel).collect(Collectors.toList());

        // 进行递归循环
        for (MenuTree menuTree: minMenuTrees) {
            menuTreeLoop(menuTree,allMenuTrees);
        }

        return minMenuTrees;
    }

    private void menuTreeLoop(MenuTree currentMenuTree,List<MenuTree> allMenuTrees){

        var childrenMenuTree = allMenuTrees.stream().filter(menu-> currentMenuTree.getId().equals(menu.getParentId())).collect(Collectors.toList());

        currentMenuTree.setChildren(childrenMenuTree);
        for (MenuTree menuTree:childrenMenuTree) {
            menuTreeLoop(menuTree,allMenuTrees);
        }

    }

    @Override
    public MenuTree queryModelTree(String id) {
        return menuDao.queryModelTree(id);
    }

    @Override
    public List<MenuRoute> queryMenuRouteByRoleIds(List<String> roleIds) {
        return menuDao.queryMenuRouteByRoleIds(roleIds);
    }

    @Override
    public List<MenuRoute> queryMenuRouteFormatByRoleIds(List<String> roleIds) {
        List<MenuRoute> allMenuRoutes = queryMenuRouteByRoleIds(roleIds);

        if(allMenuRoutes==null||allMenuRoutes.size()<=0){
            return null;
        }

        int minLevel = 0;
        // 在求出顶级所有的顶级部门
        var minMenuRoutes = allMenuRoutes.stream().filter(menu -> menu.getLevel() == minLevel).collect(Collectors.toList());

        // 进行递归循环
        for (MenuRoute menuRoute: minMenuRoutes) {
            menuRouteLoop(menuRoute,allMenuRoutes);
        }

        return minMenuRoutes;
    }

    private void menuRouteLoop(MenuRoute currentMenuRoute,List<MenuRoute> allMenuRoutes){

        var childrenMenuRoute = allMenuRoutes.stream().filter(menu-> currentMenuRoute.getId().equals(menu.getParentId())).collect(Collectors.toList());

        currentMenuRoute.setChildren(childrenMenuRoute);
        for (MenuRoute menuRoute:childrenMenuRoute) {
            menuRouteLoop(menuRoute,allMenuRoutes);
        }

    }
}
