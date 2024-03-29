package com.cditer.free.file.dao.impl;

import com.cditer.free.file.data.entity.viewmodel.FileCatalogSearch;
import com.cditer.free.file.data.entity.viewmodel.FileInfoView;
import com.cditer.free.file.data.enums.CatalogScope;
import com.cditer.free.file.mapper.IFileCatalogMapper;
import com.cditer.free.core.enums.OrderEnum;
import com.cditer.free.data.dao.base.ISqlExpression;
import com.cditer.free.core.message.data.PagerModel;
import com.cditer.free.data.utils.SqlExpressionFactory;
import com.cditer.free.file.data.entity.model.FileCatalog;
import com.cditer.free.file.data.enums.FileDataKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cditer.free.data.dao.base.impl.SuperDao;
import com.cditer.free.file.dao.IFileCatalogDao;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;


/**
 * @author      auto build code by think
 * @email       chfree001@gmail.com
 * @createtime  2020-10-06 22:16:12
 * @comment     文件目录表
 */

@Component
public class FileCatalogDaoImpl extends SuperDao<FileCatalog> implements IFileCatalogDao{

    @Autowired
    IFileCatalogMapper fileCatalogMapper;

    @Override
    public int queryCountBySearch(FileCatalogSearch search) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectCount().from(FileCatalog.class);

        appendExpression(sqlExpression,search);

        return queryCount(sqlExpression);
    }

    @Override
    public List<FileCatalog> queryListBySearch(FileCatalogSearch search, PagerModel pagerModel) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(FileCatalog.class);

        appendExpression(sqlExpression,search);

        return queryList(sqlExpression,pagerModel);
    }

    @Override
    public List<FileCatalog> queryListByTopLevel(FileCatalogSearch search) {
        ISqlExpression childCount = SqlExpressionFactory.createExpression();
        childCount.selectCount().from(FileCatalog.class,"cc").andWhere("cc.parent_id=catalog.id");
        childCount.andWhere("cc.scope=#{ccGlobalScope} or (cc.scope=#{ccPersonScope} and cc.user_id=#{ccUserId})")
                .setParam("ccGlobalScope", CatalogScope.GLOBAL)
                .setParam("ccPersonScope", CatalogScope.PERSON)
                .setParam("ccUserId",search.getUserId());

        ISqlExpression topSql = SqlExpressionFactory.createExpression();
        topSql.selectAllFrom(FileCatalog.class,"catalog")
                .appendSelect("("+childCount.toSql()+") childCount")
                .andEq("parent_id",FileDataKeys.CATALOG_TOP)
                .andEq("scope",search.getScope())
                .setParamAll(childCount.getParams())
                .addOrder("create_date", OrderEnum.ASC);

        return queryList(topSql);
    }

    @Override
    public List<FileCatalog> queryListByTwoLevel(List<String> topIds,FileCatalogSearch search) {
        if(topIds==null||topIds.isEmpty()){
            return null;
        }
        ISqlExpression childCount = SqlExpressionFactory.createExpression();
        childCount.selectCount().from(FileCatalog.class,"cc").andWhere("cc.parent_id=catalog.id");
        if(CatalogScope.GLOBAL.equals(search.getScope())){
            childCount.andWhere("cc.scope=#{ccGlobalScope}").setParam("ccGlobalScope",CatalogScope.GLOBAL);
        }else if(CatalogScope.PERSON.equals(search.getScope())){
            childCount.andWhere("cc.scope=#{ccPersonScope} and cc.user_id=#{ccUserId}")
                      .setParam("ccPersonScope", CatalogScope.PERSON)
                      .setParam("ccUserId",search.getUserId());
        }

        ISqlExpression twoSql = SqlExpressionFactory.createExpression();
        twoSql.selectAllFrom(FileCatalog.class,"catalog")
              .appendSelect("("+childCount.toSql()+") childCount")
              .andWhereInString("parent_id",topIds)
              .addOrder("name", OrderEnum.ASC)
              .setParamAll(childCount.getParams());

        appendExpression(twoSql,search);

        return queryList(twoSql);
    }

    @Override
    public List<FileCatalog> queryPathList(String id) {
        FileCatalog fileCatalog = queryModel(id);
        if(fileCatalog==null|| StringUtils.isEmpty(fileCatalog.getRelScnDsc())){
            return null;
        }
        String relScnDsc = fileCatalog.getRelScnDsc();
        List<String> relScnDscList = Arrays.asList(relScnDsc.split("\\|"));

        ISqlExpression pathSql = SqlExpressionFactory.createExpression();
        pathSql.selectAllFrom(FileCatalog.class)
                .andWhereInString("id", relScnDscList).addOrder("level",OrderEnum.ASC);

        return queryList(pathSql);
    }

    @Override
    public List<FileCatalog> queryChildList(String userId,String id) {
        return fileCatalogMapper.queryChildList(userId,id);
    }

    @Override
    public List<FileInfoView> queryFileInfoList(String catalogId) {
        return fileCatalogMapper.queryFileInfoList(catalogId);
    }

    private void appendExpression(ISqlExpression sqlExpression, FileCatalogSearch search){
        sqlExpression.andEqNoEmpty("id",search.getId());

        sqlExpression.andNotEqNoEmpty("id",search.getNotId());

        sqlExpression.andEqNoEmpty("user_id",search.getUserId());

        sqlExpression.andEqNoEmpty("parent_id",search.getParentId());

        sqlExpression.andEqNoEmpty("name",search.getName());

        sqlExpression.andEqNoEmpty("rel_scn_dsc",search.getRelScnDsc());

        sqlExpression.andEqNoEmpty("icon",search.getIcon());

        sqlExpression.andEqNoEmpty("scope",search.getScope());

        sqlExpression.andEqNoEmpty("mark",search.getMark());

        sqlExpression.andEqNoEmpty("comments",search.getComments());
    }
}