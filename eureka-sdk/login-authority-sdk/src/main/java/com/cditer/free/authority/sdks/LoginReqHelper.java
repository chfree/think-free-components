package com.cditer.free.authority.sdks;

import com.cditer.cloud.commons.http.entity.RequestEntityEx;
import com.cditer.cloud.commons.http.entity.RequestEntityExBuilder;
import com.cditer.cloud.commons.http.entity.ResponseEntityEx;
import com.cditer.cloud.commons.http.request.impl.BaseServerRequest;
import com.cditer.free.authority.data.entity.apimodel.login.CheckUserLoginResp;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2021-09-21 22:27
 * @comment
 */

@Component
public class LoginReqHelper extends BaseServerRequest {
    final static String prefix = "/api/v1/authority/login/";

    private final static String checkUserLoginUrl = prefix + "checkUserLogin";

    public CheckUserLoginResp checkUserLogin(String userId){

        RequestEntityEx requestEntityEx = RequestEntityExBuilder.builder()
                .setUrl(checkUserLoginUrl)
                .setMethod(HttpMethod.POST)
                .addParam("userId", userId)
                .build();

        ResponseEntityEx<CheckUserLoginResp> request = getServerRequestHelper().request(requestEntityEx, CheckUserLoginResp.class);

        return request.getBody();
    }
}
