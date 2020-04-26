package com.xuecheng.framework.domain.goods.response;

import com.xuecheng.framework.domain.goods.Category;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;

@Data
public class CategoryResult extends ResponseResult {
    Category category;
    public CategoryResult(ResultCode resultCode, Category category) {
        super(resultCode);
        this.category = category;
    }
}
