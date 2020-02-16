package com.tennetcn.free.authority.enums;

import com.tennetcn.free.core.enums.BaseEnum;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2020-02-16 09:42
 * @comment
 */

public enum LoginAuthType implements BaseEnum<String> {
    LOGIN("login","登陆");


    private String key;

    private String value;

    LoginAuthType(String key, String value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
