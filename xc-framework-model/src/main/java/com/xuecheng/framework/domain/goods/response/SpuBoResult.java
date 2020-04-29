package com.xuecheng.framework.domain.goods.response;

import com.xuecheng.framework.domain.goods.SpuBo;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;

public class SpuBoResult extends ResponseResult {
    SpuBo spuBo;
    public SpuBoResult(ResultCode resultCode, SpuBo spuBo) {
        super(resultCode);
        this.spuBo = spuBo;
    }
}
