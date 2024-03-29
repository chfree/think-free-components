package com.cditer.free.param.logical.dao;

import com.cditer.free.param.data.entity.model.ParamOption;
import com.cditer.free.param.data.entity.viewmodel.ParamOptionSearch;
import com.cditer.free.param.data.entity.viewmodel.ParamOptionView;
import com.cditer.free.core.message.data.PagerModel;
import com.cditer.free.data.dao.base.ISuperDao;

import java.util.List;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-11 23:04
 * @comment
 */

public interface IParamOptionDao extends ISuperDao<ParamOption> {
    int queryCountBySearch(ParamOptionSearch search);

    List<ParamOption> queryListBySearch(ParamOptionSearch search, PagerModel pagerModel);

    List<ParamOption> queryListByDefineName(String defineName);

    List<ParamOptionView> queryListByDefineNames(List<String> defineNames);

    ParamOption queryModel(ParamOptionSearch search);

    ParamOption queryFirstOption(String defineName);
}
