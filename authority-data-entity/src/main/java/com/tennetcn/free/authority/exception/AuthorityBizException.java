package com.tennetcn.free.authority.exception;

import com.tennetcn.free.core.exception.BizException;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-11-28 22:53
 * @comment
 */

public class AuthorityBizException extends BizException {
    public AuthorityBizException(){}

    public AuthorityBizException(String message){
        super(message);
    }

    public AuthorityBizException(int code, String message){
        super(code,message);
    }
}