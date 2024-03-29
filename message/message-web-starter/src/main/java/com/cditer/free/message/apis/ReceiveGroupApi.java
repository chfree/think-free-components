package com.cditer.free.message.apis;

import com.cditer.free.message.entity.model.ReceiveGroup;
import com.cditer.free.message.entity.viewmodel.ReceiveGroupSearch;
import com.cditer.free.message.entity.viewmodel.ReceiveGroupView;
import com.cditer.free.message.service.IReceiveGroupService;
import com.cditer.free.security.baseapi.TokenApi;
import com.cditer.free.core.message.web.BasePagerReq;
import com.cditer.free.core.message.web.BasePagerResp;
import com.cditer.free.core.message.web.BaseRequest;
import com.cditer.free.core.message.web.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


/**
 * @author      C.H
 * @email       chfree365@qq.com
 * @createtime  2022-06-28 12:49:13
 * @comment     消息组
 */
@RestController
@RequestMapping(value = "/api/v1/message/receiveGroup/",produces =  MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags="消息组管理",value ="消息组相关的操作")
public class ReceiveGroupApi extends TokenApi {
    @Autowired
    private IReceiveGroupService receiveGroupService;

    @ApiOperation(value = "获取消息组列表")
    @PostMapping("list")
    public BaseResponse list(@RequestBody @Valid BasePagerReq<ReceiveGroupSearch> req){
        int totalCount = receiveGroupService.queryCountBySearch(req.getData());
        List<ReceiveGroupView> list = receiveGroupService.queryListViewBySearch(req.getData(), req.getPager());

        return BasePagerResp.success(list, totalCount);
    }

    @ApiOperation(value = "获取指定消息组")
    @GetMapping("get")
    public BaseResponse<ReceiveGroupView> get(@Valid @NotBlank(message = "消息组id不能为空") String id){
        ReceiveGroupView receiveGroup = receiveGroupService.queryModelViewById(id);
        return BaseResponse.success(receiveGroup);
    }

    @ApiOperation(value = "搜索消息组数量")
    @PostMapping("countSearch")
    public BaseResponse countSearch(@RequestBody BaseRequest<ReceiveGroupSearch> req){
        int count =  receiveGroupService.queryCountBySearch(req.getData());
        return BaseResponse.success(count);
    }

    @ApiOperation(value = "删除一个消息组")
    @PostMapping("delete")
    public BaseResponse delete(@Valid @NotBlank(message = "消息组id不能为空")String id){
        boolean result =  receiveGroupService.deleteModel(id);

        return BaseResponse.success(result);
    }

    @ApiOperation(value = "保存一个消息组")
    @PostMapping("save")
    public BaseResponse save(@RequestBody @Valid BaseRequest<ReceiveGroup> req){
        boolean result = receiveGroupService.savereceiveGroup(req.getData());

        return BaseResponse.success(result);
    }

}
