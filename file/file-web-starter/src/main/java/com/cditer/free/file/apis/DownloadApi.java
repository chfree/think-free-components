package com.cditer.free.file.apis;

import cn.hutool.core.util.IdUtil;
import com.cditer.free.core.exception.BizException;
import com.cditer.free.core.message.web.BaseResponse;
import com.cditer.free.file.data.entity.model.FileInfo;
import com.cditer.free.file.exception.FileBizException;
import com.cditer.free.file.service.IFileInfoService;
import com.cditer.free.file.utils.FilePathUtils;
import com.cditer.free.security.annotation.TokenPassport;
import com.cditer.free.security.baseapi.TokenApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2020-09-18 22:05
 * @comment
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/file/download/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags="下载上传的文件",value ="下载上传的文件相关的操作")
public class DownloadApi extends TokenApi {

    @Autowired
    IFileInfoService fileInfoService;

    @ApiOperation(value = "根据文件编号获取下载编号")
    @GetMapping("getDownId")
    public BaseResponse getDownId(@Valid @NotBlank(message = "文件id不能为空") String fileId){
        BaseResponse response = new BaseResponse();

        String downId = IdUtil.randomUUID();
        cached.put(downId,fileId);

        response.put("downId",downId);

        return response;
    }

    @TokenPassport
    @ApiOperation(value = "根据下载编号下载文件")
    @GetMapping("downFile/{downId}")
    public void downFile(@Valid @NotBlank(message = "下载id不能为空") @PathVariable("downId") String downId, String fileName){
        Object fileId = cached.get(downId);
        HttpServletResponse servletResponse = getServletResponse();

        OutputStream outputStream = null;
        try{
            outputStream =  servletResponse.getOutputStream();
        }catch (Exception ex){
            log.error("downFile getOutputStream is error", ex);
            throw new BizException("downFile getOutputStream is error",ex);
        }
        if(fileId==null){
            throw new FileBizException("downId异常，找不到对应的文件id");
        }

        FileInfo fileInfo = fileInfoService.queryModel(fileId.toString());
        if(fileInfo == null){
            throw new FileBizException("fileId找不到对应的文件:"+fileId);
        }
        String localPath = FilePathUtils.getDiskPath() + fileInfo.getPath();
        String filePathName = localPath+fileInfo.getFileName();

        if(StringUtils.isEmpty(fileName)){
            fileName = fileInfo.getDisplayName();
        }

        servletResponse.reset();
        servletResponse.setHeader("Content-Disposition", "attachment; filename=" + getEncode(fileName));
        servletResponse.setContentType(fileInfo.getMimeType());

        readFile(filePathName, outputStream);
    }

    private String getEncode(String fileName){
        try {
            return URLEncoder.encode(fileName, "UTF-8");
        }catch (Exception ex){
            log.error("getEncode is error", ex);
        }
        return fileName;
    }


    private void readFile(String filePathName,OutputStream outputStream){
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(filePathName));
            int i = bis.read(buff);
            while (i != -1) {
                outputStream.write(buff, 0, buff.length);
                outputStream.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            log.error("readFile is error;",e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
