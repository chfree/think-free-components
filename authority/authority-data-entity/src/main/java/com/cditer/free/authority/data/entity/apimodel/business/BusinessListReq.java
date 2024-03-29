package com.cditer.free.authority.data.entity.apimodel.business;

import com.cditer.free.authority.data.entity.viewmodel.BusinessSearch;
import com.cditer.free.core.message.web.BasePagerReq;
import lombok.Data;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-29 12:53
 * @comment
 */

@Data
public class BusinessListReq extends BasePagerReq {
    private BusinessSearch search;
}
