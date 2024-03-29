package com.cditer.free.authority.data.entity.apimodel.role;

import com.cditer.free.authority.data.entity.model.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-29 23:00
 * @comment
 */

@Data
public class SaveRoleReq extends Role {

    @NotBlank(message = "角色名称不能为空")
    private String roleName;
}
