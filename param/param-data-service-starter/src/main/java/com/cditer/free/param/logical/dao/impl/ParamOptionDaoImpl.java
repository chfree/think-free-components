package com.cditer.free.param.logical.dao.impl;

import com.cditer.free.param.logical.dao.IParamOptionDao;
import com.cditer.free.param.data.entity.model.ParamDefine;
import com.cditer.free.param.data.entity.model.ParamOption;
import com.cditer.free.param.data.entity.viewmodel.ParamOptionSearch;
import com.cditer.free.param.data.entity.viewmodel.ParamOptionView;
import com.cditer.free.core.enums.OrderEnum;
import com.cditer.free.core.message.data.PagerModel;
import com.cditer.free.data.dao.base.ISqlExpression;
import com.cditer.free.data.dao.base.impl.SuperDao;
import com.cditer.free.data.utils.SqlExpressionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-11 23:13
 * @comment
 */

@Component
public class ParamOptionDaoImpl extends SuperDao<ParamOption> implements IParamOptionDao {

    @Override
    public int queryCountBySearch(ParamOptionSearch search) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectCount().from(ParamOption.class);

        appendExpression(sqlExpression,search);

        return queryCount(sqlExpression);
    }

    @Override
    public List<ParamOption> queryListBySearch(ParamOptionSearch search, PagerModel pagerModel) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(ParamOption.class)
                     .addOrder("sort_code", OrderEnum.ASC);

        appendExpression(sqlExpression,search);

        return queryList(sqlExpression,pagerModel);
    }

    @Override
    public List<ParamOption> queryListByDefineName(String defineName) {
        ISqlExpression defineIdSql = SqlExpressionFactory.createExpression().select("id").from(ParamDefine.class).andEq("name",defineName);

        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(ParamOption.class)
                .andWhereIn("define_id",defineIdSql)
                .addOrder("sort_code", OrderEnum.ASC);

        return queryList(sqlExpression);
    }

    @Override
    public List<ParamOptionView> queryListByDefineNames(List<String> defineNames) {
        if(defineNames == null || defineNames.size()<=0){
            return null;
        }

        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.select("po.*")
                .appendSelect("pd.name as defineName")
                .from(ParamOption.class,"po")
                .leftJoin(ParamDefine.class,"pd").on("po.define_id","pd.id")
                .andWhereInString("pd.name",defineNames)
                .addOrder("sort_code", OrderEnum.ASC);

        return queryList(sqlExpression,ParamOptionView.class);
    }

    @Override
    public ParamOption queryModel(ParamOptionSearch search) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(ParamOption.class).addOrder("sort_code", OrderEnum.ASC);;
        if(!StringUtils.isEmpty(search.getDefineName())){
            ISqlExpression defineIdSql = SqlExpressionFactory.createExpression().select("id").from(ParamDefine.class).andEq("name",search.getDefineName());
            sqlExpression.andWhereIn("define_id",defineIdSql);
        }

        appendExpression(sqlExpression,search);

        return queryModel(sqlExpression);
    }

    @Override
    public ParamOption queryFirstOption(String defineName) {
        ISqlExpression sqlExpression = SqlExpressionFactory.createExpression();
        sqlExpression.selectAllFrom(ParamOption.class)
                .addOrder("sort_code", OrderEnum.ASC)
                .limit(1,1);

        ISqlExpression defineIdSql = SqlExpressionFactory.createExpression().select("id").from(ParamDefine.class).andEq("name",defineName);
        sqlExpression.andWhereIn("define_id",defineIdSql);

        return queryModel(sqlExpression);
    }

    private void appendExpression(ISqlExpression sqlExpression, ParamOptionSearch search){
        sqlExpression.andEqNoEmpty("id",search.getId());

        sqlExpression.andNotEqNoEmpty("id",search.getNotId());

        sqlExpression.andEqNoEmpty("define_id",search.getDefineId());

        sqlExpression.andEqNoEmpty("text",search.getText());

        sqlExpression.andEqNoEmpty("value",search.getValue());
    }
}
