package com.tennetcn.free.security.core;

import com.tennetcn.free.core.util.SpringContextUtils;
import com.tennetcn.free.security.properties.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2020-12-24 09:07
 * @comment
 */

@Component
public class CreateTokenFactory {

    public final static String ID="id";

    public final static String ACCOUNT="account";

    public final static String NAME="name";

    public final static String DEFAULT_TOKEN_CREATE="defaultTokenCreate";

    @Autowired
    JwtConfig jwtConfig;

    public ITokenCreate newTokenCreate(){
        String tokenCreateBean = DEFAULT_TOKEN_CREATE;

        if(!"default".equals(jwtConfig.getCreateTokenBean())&&!StringUtils.isEmpty(jwtConfig.getCreateTokenBean())){
            tokenCreateBean = jwtConfig.getCreateTokenBean();
        }
        return (ITokenCreate)SpringContextUtils.getCurrentContext().getBean(tokenCreateBean);
    }
}
