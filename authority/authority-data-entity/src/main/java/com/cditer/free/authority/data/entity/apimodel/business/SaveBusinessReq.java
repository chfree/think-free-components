package com.cditer.free.authority.data.entity.apimodel.business;

import cn.hutool.core.date.DateUtil;
import com.cditer.free.authority.data.entity.model.Business;
import com.cditer.free.core.enums.ModelStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-07-29 12:54
 * @comment
 */

@Data
public class SaveBusinessReq extends Business {

    @NotBlank
    private String name;

    @NotBlank
    private String shortName;

    @Override
    public void setModelStatus(ModelStatus modelStatus) {
        super.setModelStatus(modelStatus);

        if(modelStatus==ModelStatus.add){
            setCreateDate(DateUtil.date());
        }else if(modelStatus==ModelStatus.update){
            setModifyDate(DateUtil.date());
        }
    }
}
