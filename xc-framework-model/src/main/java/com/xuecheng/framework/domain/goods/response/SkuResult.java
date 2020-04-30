package com.xuecheng.framework.domain.goods.response;

import com.xuecheng.framework.domain.goods.Sku;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;

@Data
public class SkuResult extends ResponseResult {
    Sku sku;
    public SkuResult(ResultCode resultCode, Sku sku) {
        super(resultCode);
        this.sku = sku;
    }
}
