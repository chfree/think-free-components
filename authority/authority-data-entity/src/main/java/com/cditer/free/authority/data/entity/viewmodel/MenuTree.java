package com.cditer.free.authority.data.entity.viewmodel;

import com.cditer.free.authority.data.entity.model.Menu;
import lombok.Data;

import java.util.List;

/**
 * @author chfree
 * @email chfree001@gmail.com
 * @create 2019-08-12 12:10
 * @comment
 */

@Data
public class MenuTree extends Menu {
    private List<MenuTree> children;

    private String parentName;
}
