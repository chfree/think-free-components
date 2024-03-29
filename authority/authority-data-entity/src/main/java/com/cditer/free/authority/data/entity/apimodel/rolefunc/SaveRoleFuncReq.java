package com.cditer.free.authority.data.entity.apimodel.rolefunc;

import com.cditer.free.authority.data.entity.model.RoleFunc;
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

    @NotBlank(message = "角色id不能为空")
    private String roleId;

    private List<RoleFunc> roleFuncs;
}
