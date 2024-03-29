package com.cditer.free.develop.apis;

import com.cditer.free.core.message.data.PagerModel;
import com.cditer.free.core.message.web.BaseResponse;
import com.cditer.free.develop.data.entity.apimodel.dictcolumn.DictColumnListReq;
import com.cditer.free.develop.data.entity.apimodel.dictcolumn.DictColumnListResp;
import com.cditer.free.develop.data.entity.apimodel.dictcolumn.SaveDictColumnReq;
import com.cditer.free.develop.service.IDictColumnService;
import com.cditer.free.security.baseapi.TokenApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-09-01 09:11
 * @comment
 */
@RestController
@RequestMapping(value = "/api/v1/develop/dictColumn/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags="字典字段管理",value ="字典字段相关的操作")
public class DictColumnApi extends TokenApi {

    @Autowired
    IDictColumnService dictColumnService;

    @ApiOperation(value = "获取字典字段列表")
    @PostMapping("list")
    public BaseResponse list(@RequestBody @Valid DictColumnListReq listReq){
        DictColumnListResp resp = new DictColumnListResp();
        PagerModel pagerModel =new PagerModel(2000,1);

        resp.setDictColumns(dictColumnService.queryListBySearch(listReq.getSearch(),pagerModel));

        return resp;
    }

    @ApiOperation(value = "保存一个字典字段")
    @PostMapping("save")
    public BaseResponse save(@RequestBody @Valid SaveDictColumnReq saveDictColumnReq){
        BaseResponse response = new BaseResponse();

        response.put("result",dictColumnService.saveDictColumns(saveDictColumnReq.getTableId(),saveDictColumnReq.getDictColumns()));

        return response;
    }
}
