package com.xuecheng.framework.domain.goods.response;

import com.xuecheng.framework.domain.goods.Brand;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;

@Data
public class BrandResult extends ResponseResult {
    Brand brand;
    public BrandResult(ResultCode resultCode, Brand brand) {
        super(resultCode);
        this.brand = brand;
    }
}
