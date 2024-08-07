package com.cditer.free.devops.web.apis;

import com.cditer.free.core.message.web.BasePagerReq;
import com.cditer.free.core.message.web.BasePagerResp;
import com.cditer.free.core.message.web.BaseRequest;
import com.cditer.free.core.message.web.BaseResponse;
import com.cditer.free.dbdevops.data.model.ColumnSetting;
import com.cditer.free.dbdevops.data.viewmodel.ColumnSettingSearch;
import com.cditer.free.dbdevops.data.viewmodel.ColumnSettingView;
import com.cditer.free.dbdevops.logical.service.IColumnSettingService;
import com.cditer.free.security.baseapi.TokenApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * @author      C.H
 * @email       chfree365@qq.com
 * @createtime  2024-07-25 13:03:55
 * @comment     列信息配置
 */
@RestController
@RequestMapping(value = "/api/v1/dbDevops/columnSetting/",produces =  MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags="列信息配置管理",value ="列信息配置相关的操作")
public class ColumnSettingApi extends TokenApi {
    @Autowired
    private IColumnSettingService columnSettingService;

    @ApiOperation(value = "获取列信息配置列表")
    @PostMapping("list")
    public BaseResponse<List<ColumnSettingView>> list(@RequestBody @Valid BasePagerReq<ColumnSettingSearch> req){
        int totalCount = columnSettingService.queryCountBySearch(req.getData());
        List<ColumnSettingView> list = columnSettingService.queryListViewBySearch(req.getData(), req.getPager());

        return BasePagerResp.success(list, totalCount);
    }

    @ApiOperation(value = "获取指定列信息配置")
    @GetMapping("get")
    public BaseResponse<ColumnSettingView> get(@Valid @NotBlank(message = "列信息配置id不能为空") String id){
        ColumnSettingView columnSettingView = columnSettingService.queryModelViewById(id);

        return BaseResponse.success(columnSettingView);
    }

    @ApiOperation(value = "搜索列信息配置数量")
    @PostMapping("countSearch")
    public BaseResponse countSearch(@RequestBody BaseRequest<ColumnSettingSearch> req){
        int count =  columnSettingService.queryCountBySearch(req.getData());

        return BaseResponse.success(count);
    }

    @ApiOperation(value = "删除一个列信息配置")
    @PostMapping("delete")
    public BaseResponse delete(@Valid @NotBlank(message = "列信息配置id不能为空")String id){
        boolean result =  columnSettingService.deleteModel(id);

        return BaseResponse.success(result);
    }

    @ApiOperation(value = "保存一个列信息配置")
    @PostMapping("save")
    public BaseResponse save(@RequestBody @Valid BaseRequest<ColumnSetting> req){
        boolean result = columnSettingService.saveColumnSetting(req.getData());

        return BaseResponse.success(result);
    }

}