package com.cditer.free.authority.data.entity.viewmodel;

import lombok.Data;

import java.util.List;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-08-07 12:52
 * @comment
 */

@Data
public class UserSearch {
    private String id;

    private String name;

    private String nickName;

    private String account;

    private String notId;

    private String likeName;

    private String likeNickName;

    private String likeAccount;

    private String status;

    private String likeQuery;

    private List<String> statusList;

    private List<String> roleMarkList;
}
