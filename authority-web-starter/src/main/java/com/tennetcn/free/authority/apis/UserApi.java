package com.tennetcn.free.authority.apis;

import cn.hutool.core.util.IdUtil;
import com.tennetcn.free.authority.apimodel.user.SaveUserReq;
import com.tennetcn.free.authority.apimodel.user.UserListReq;
import com.tennetcn.free.authority.apimodel.user.UserListResp;
import com.tennetcn.free.authority.service.IUserService;
import com.tennetcn.free.authority.viewmodel.UserSearch;
import com.tennetcn.free.authority.viewmodel.UserView;
import com.tennetcn.free.data.enums.ModelStatus;
import com.tennetcn.free.security.webapi.AuthorityApi;
import com.tennetcn.free.web.webapi.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-12 00:03
 * @comment
 */

@RestController
@RequestMapping(value = "/api/v1/authority/user/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags="用户模块",value ="用户相关的操作" )
public class UserApi extends AuthorityApi {

    @Autowired
    private IUserService userService;

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
    public BaseResponse get(@Valid @NotBlank(message = "用户id不能为空") String id){
        BaseResponse response=new BaseResponse();

        UserView user = userService.queryViewModelById(id);
        response.put("user",user);

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
        }else{
            userReq.setModelStatus(ModelStatus.update);
        }

        boolean result = userService.applyChange(userReq);
        response.put("result",result);

        return response;
    }
}
