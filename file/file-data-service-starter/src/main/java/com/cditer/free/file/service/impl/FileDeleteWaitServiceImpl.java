package com.cditer.free.file.service.impl;

import cn.hutool.core.io.FileUtil;
import com.cditer.free.file.data.entity.viewmodel.FileDeleteWaitSearch;
import com.cditer.free.file.utils.FilePathUtils;
import com.cditer.free.core.message.data.PagerModel;
import com.cditer.free.data.dao.base.impl.SuperService;
import com.cditer.free.file.dao.IFileDeleteWaitDao;
import com.cditer.free.file.data.entity.model.FileDeleteWait;
import com.cditer.free.file.data.entity.model.FileInfo;
import com.cditer.free.file.service.IFileDeleteWaitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;


/**
 * @author      auto build code by think
 * @email       chfree001@gmail.com
 * @createtime  2020-09-21 12:06:00
 * @comment     文件删除等待表
 */

@Component
public class FileDeleteWaitServiceImpl extends SuperService<FileDeleteWait> implements IFileDeleteWaitService {
    @Autowired
    IFileDeleteWaitDao fileDeleteWaitDao;

    @Override
    public int queryCountBySearch(FileDeleteWaitSearch search) {
        return fileDeleteWaitDao.queryCountBySearch(search);
    }

    @Override
    public List<FileDeleteWait> queryListBySearch(FileDeleteWaitSearch search, PagerModel pagerModel) {
        return fileDeleteWaitDao.queryListBySearch(search,pagerModel);
    }

    @Override
    public boolean moveFileToDelayDir(FileInfo fileInfo) {
        String pathFileName = FilePathUtils.getDiskPath() + fileInfo.getPath() + fileInfo.getFileName();
        String targetFileName = FilePathUtils.getDiskPath() +FilePathUtils.delayPath+ fileInfo.getPath() + fileInfo.getFileName();

        FileUtil.move(new File(pathFileName),new File(targetFileName),true);

        return true;
    }

    @Override
    public boolean moveFilesToDelayDir(List<FileInfo> fileInfos) {
        if(fileInfos == null || fileInfos.isEmpty()){
            return true;
        }
        for (FileInfo fileInfo : fileInfos) {
            moveFileToDelayDir(fileInfo);
        }
        return true;
    }

    @Override
    public List<FileDeleteWait> queryCanDeleteFile(PagerModel pagerModel) {
        return fileDeleteWaitDao.queryCanDeleteFile(pagerModel);
    }

}