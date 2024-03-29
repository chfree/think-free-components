package com.cditer.free.authority.logical.service;

import com.cditer.free.authority.data.entity.model.LoginAuth;
import com.cditer.free.authority.data.entity.viewmodel.LoginAuthSearch;
import com.cditer.free.authority.data.entity.viewmodel.LoginAuthView;
import com.cditer.free.core.message.data.PagerModel;
import com.cditer.free.data.dao.base.ISuperService;

import java.util.List;


/**
 * @author      auto build code by think
 * @email       chfree001@gmail.com
 * @createtime  2020-02-15 14:33:16
 * @comment     登陆授权表
 */

public interface ILoginAuthService extends ISuperService<LoginAuth>{
    int queryCountBySearch(LoginAuthSearch search);

    List<LoginAuthView> queryListBySearch(LoginAuthSearch search, PagerModel pagerModel);

    boolean saveLoginAuth(LoginAuth loginAuth);

    boolean checkTokenIsValid(String token);

    boolean updateStatusByToken(String token,String status);
}