package com.xuecheng.api.goods;

import com.xuecheng.framework.domain.goods.SpuBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "商品中心", description = "商品管理中心")
public interface GoodsControllerApi {

    @ApiOperation("根据id获取商品")
    @ApiImplicitParam(name = "id", value = "spu id", required = true, paramType = "path", dataType = "long")
    public SpuBo queryGoodsById(Long id);
}
