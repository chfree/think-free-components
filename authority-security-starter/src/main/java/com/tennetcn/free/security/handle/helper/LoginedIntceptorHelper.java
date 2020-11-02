package com.tennetcn.free.security.handle.helper;

import com.tennetcn.free.core.message.web.BaseResponse;
import com.tennetcn.free.core.util.SpringContextUtils;
import com.tennetcn.free.security.handle.ILoginedIntceptor;
import com.tennetcn.free.security.message.LoginModel;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2020-11-02 17:15
 * @comment
 */

public class LoginedIntceptorHelper {
    public static void loginedCallback(LoginModel loginModel){
        Map<String, ILoginedIntceptor> loginedIntceptorMap = SpringContextUtils.getCurrentContext().getBeansOfType(ILoginedIntceptor.class);
        if(loginedIntceptorMap==null||loginedIntceptorMap.isEmpty()){
            return;
        }
        List<ILoginedIntceptor> loginedList = loginedIntceptorMap.values().stream().sorted(Comparator.comparing(ILoginedIntceptor::getOrder)).collect(Collectors.toList());
        loginedList.forEach(loginedItem -> {
            loginedItem.logined(loginModel);
        });
    }
}
