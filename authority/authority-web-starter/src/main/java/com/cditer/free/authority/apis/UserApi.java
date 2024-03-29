package com.cditer.free.authority.apis;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.cditer.free.authority.data.entity.apimodel.user.SaveUserReq;
import com.cditer.free.authority.data.entity.apimodel.user.UpdatePwd;
import com.cditer.free.authority.data.entity.apimodel.user.UserGetResp;
import com.cditer.free.authority.data.entity.apimodel.user.UserListReq;
import com.cditer.free.authority.data.entity.apimodel.user.UserListResp;
import com.cditer.free.authority.data.entity.model.User;
import com.cditer.free.authority.data.entity.viewmodel.UserSearch;
import com.cditer.free.authority.data.entity.viewmodel.UserView;
import com.cditer.free.authority.data.enums.LoginStatus;
import com.cditer.free.authority.exception.AuthorityBizException;
import com.cditer.free.authority.logical.service.IButtonService;
import com.cditer.free.authority.logical.service.IGroupService;
import com.cditer.free.authority.logical.service.IMenuService;
import com.cditer.free.authority.logical.service.IRoleService;
import com.cditer.free.authority.logical.service.IUserService;
import com.cditer.free.core.enums.ModelStatus;
import com.cditer.free.core.message.web.BaseResponse;
import com.cditer.free.coreweb.message.WebResponseStatus;
import com.cditer.free.security.baseapi.TokenApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-12 00:03
 * @comment
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/authority/user/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags="用户模块",value ="用户相关的操作" )
public class UserApi extends TokenApi {

    @Autowired
    IUserService userService;

    @Autowired
    IMenuService menuService;

    @Autowired
    IRoleService roleService;

    @Autowired
    IButtonService buttonService;

    @Autowired
    IGroupService groupService;

    @ApiOperation(value = "获取用户列表")
    @PostMapping("list")
    public BaseResponse list(@RequestBody @Valid UserListReq listReq){
        UserListResp response = new UserListResp();
        response.setTotalCount(userService.queryCountBySearch(listReq.getSearch()));
        response.setUsers(userService.queryViewListBySearch(listReq.getSearch(),listReq.getPager()));

        return response;
    }

    @ApiOperation(value = "获取指定用户")
    @GetMapping("get")
    public UserGetResp get(@Valid @NotBlank(message = "用户id不能为空") String id){
        UserGetResp response=new UserGetResp();

        UserView user = userService.queryViewModelById(id);
        response.setUser(user);

        return response;
    }

    @ApiOperation(value = "搜索用户数量")
    @PostMapping("countSearch")
    public BaseResponse countSearch(UserSearch search){
        BaseResponse response=new BaseResponse();

        int count =  userService.queryCountBySearch(search);
        response.put("count",count);

        return response;
    }

    @ApiOperation(value = "删除指定用户")
    @PostMapping("delete")
    public BaseResponse delete(@Valid String id){
        return null;
    }

    @ApiOperation(value = "保存一个用户")
    @PostMapping("save")
    public BaseResponse save(@Valid SaveUserReq userReq){
        BaseResponse response = new BaseResponse();
        if(StringUtils.isEmpty(userReq.getId())){
            userReq.setId(IdUtil.randomUUID());
            userReq.setModelStatus(ModelStatus.add);
            userReq.setStatus(LoginStatus.NORMAL.getValue());
        }else{
            userReq.setModelStatus(ModelStatus.update);
        }

        boolean result = userService.saveUser(userReq);
        response.put("result",result);

        return response;
    }

    @ApiOperation(value = "更改密码")
    @PostMapping("updatePwd")
    public BaseResponse updatePwd(@Valid UpdatePwd updatePwd){
        BaseResponse response = new BaseResponse();
        if(!updatePwd.getNewPwd().equals(updatePwd.getConfirmNewPwd())){
            response.setMessage("两次密码输入不一致");
            response.setStatus(WebResponseStatus.DATA_ERROR);

            return response;
        }
        User user = userService.queryModelByLogin(updatePwd.getAccount(),updatePwd.getOldPwd());
        if(user==null){
            response.setMessage("账号或密码输入不正确");
            response.setStatus(WebResponseStatus.DATA_ERROR);

            return response;
        }
        user.setPassword(userService.passwordFormat(updatePwd.getNewPwd()));

        response.put("result",userService.updateModel(user));

        return response;
    }

    @ApiOperation(value = "验证用户名密码")
    @PostMapping("checkAccountAndPwd")
    public BaseResponse checkAccountAndPwd(@Valid @NotBlank(message = "用户账号不能为空") String account,@Valid @NotBlank(message = "密码不能为空") String pwd){
        BaseResponse response = new BaseResponse();

        User user = userService.queryModelByLogin(account,pwd);
        response.put("result", user !=null);

        return response;
    }

    @ApiOperation(value = "强制更改密码")
    @PostMapping("forceUpdatePwd")
    public BaseResponse forceUpdatePwd(@Valid @NotBlank(message = "用户id不能为空") String id,@Valid @NotBlank(message = "密码不能为空") String pwd){
        BaseResponse response = new BaseResponse();
        User user = userService.queryModel(id);

        if(user==null){
            throw new AuthorityBizException("用户不存在，更改密码失败");
        }
        user.setPassword(userService.passwordFormat(pwd));
        user.setModifyDate(DateUtil.date());
        user.setModifyUserId(getLoginId());
        user.setModifyUserName(getLoginName());

        response.put("result",userService.updateModel(user));

        return response;
    }

    @ApiOperation(value = "更改用户状态")
    @PostMapping("updateUserStatus")
    public BaseResponse updateUserStatus(@Valid @NotBlank(message = "用户id不能为空") String id,@Valid @NotBlank(message = "密码不能为空") String status){
        BaseResponse response = new BaseResponse();
        User user = userService.queryModel(id);

        if(Arrays.asList(LoginStatus.DELETE.getValue(),LoginStatus.NORMAL.getValue(),LoginStatus.FORBIDDEN.getValue()).indexOf(status)<0){
            throw new AuthorityBizException("状态只能更新为【正常、禁用、废除】");
        }

        if(user==null){
            throw new AuthorityBizException("用户不存在，更新状态失败");
        }
        user.setStatus(status);
        user.setModifyDate(DateUtil.date());
        user.setModifyUserId(getLoginId());
        user.setModifyUserName(getLoginName());

        response.put("result",userService.updateModel(user));

        return response;
    }




}
