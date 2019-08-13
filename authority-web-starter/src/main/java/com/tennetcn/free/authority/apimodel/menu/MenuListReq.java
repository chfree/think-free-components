package com.tennetcn.free.authority.apimodel.menu;

import com.tennetcn.free.authority.viewmodel.MenuSearch;
import com.tennetcn.free.web.webapi.BaseRequest;
import lombok.Data;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-29 22:57
 * @comment
 */

@Data
public class MenuListReq extends BaseRequest {
    private MenuSearch search;
}
