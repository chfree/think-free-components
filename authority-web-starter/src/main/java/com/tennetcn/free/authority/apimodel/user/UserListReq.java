package com.tennetcn.free.authority.apimodel.user;

import com.tennetcn.free.authority.viewmodel.UserSearch;
import com.tennetcn.free.web.message.BasePagerReq;
import lombok.Data;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-26 20:37
 * @comment
 */

@Data
public class UserListReq extends BasePagerReq {
    private UserSearch search;
}
