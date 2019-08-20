package com.tennetcn.free.authority.apimodel.rolefunc;

import com.tennetcn.free.authority.model.RoleFunc;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-29 23:00
 * @comment
 */

@Data
public class SaveRoleFuncReq {

    @NotBlank(message = "用户id不能为空")
    private String userId;

    private List<RoleFunc> roleFuncs;
}
