package com.cditer.free.authority.apis;

import cn.hutool.core.util.IdUtil;
import com.cditer.free.authority.data.entity.apimodel.userrole.SaveUserRoleReq;
import com.cditer.free.authority.data.entity.model.UserRole;
import com.cditer.free.authority.logical.service.IUserRoleService;
import com.cditer.free.core.message.web.BaseResponse;
import com.cditer.free.security.baseapi.TokenApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-29 12:45
 * @comment
 */
@RestController
@RequestMapping(value = "/api/v1/authority/userRole/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags="用户角色",value ="用户角色相关的操作")
public class UserRoleApi extends TokenApi {

    @Autowired
    private IUserRoleService userRoleService;

    @ApiOperation(value = "根据用户id获取角色信息")
    @GetMapping("listRole")
    public BaseResponse listRole(@Valid @NotBlank(message = "用户id不能为空") String userId){
        BaseResponse response = new BaseResponse();
        response.put("roles",userRoleService.queryListByUserId(userId));

        return response;
    }

    @ApiOperation(value = "保存用户角色")
    @PostMapping("save")
    public BaseResponse save(@RequestBody @Valid SaveUserRoleReq saveUserRoleReq){
        BaseResponse response = new BaseResponse();
        boolean result = userRoleService.saveUserRoles(saveUserRoleReq.getUserId(),getUserRoleFormat(saveUserRoleReq));
        response.put("result", result);

        return response;
    }

    private List<UserRole> getUserRoleFormat(SaveUserRoleReq saveUserRoleReq){
        return saveUserRoleReq.getRoleIds().stream().map(roleId-> UserRole.builder()
                .id(IdUtil.randomUUID())
                .userId(saveUserRoleReq.getUserId())
                .roleId(roleId)
                .build()
        ).collect(Collectors.toList());
    }
}
