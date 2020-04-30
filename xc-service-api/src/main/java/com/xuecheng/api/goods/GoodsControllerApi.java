package com.xuecheng.api.goods;

import com.xuecheng.framework.domain.goods.SpuBo;
import com.xuecheng.framework.domain.goods.SpuDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "商品中心", description = "商品管理中心")
public interface GoodsControllerApi {

    @ApiOperation("根据id获取商品")
    @ApiImplicitParam(name = "id", value = "spu id", required = true, paramType = "path", dataType = "long")
    public SpuBo queryGoodsById(Long id);

    @ApiOperation("根据spu id获取商品详情信息")
    @ApiImplicitParam(name = "id", value = "spu id", required = true, paramType = "path", dataType = "long")
    public SpuDetail querySpuDetailBySpuId(Long id);
}
