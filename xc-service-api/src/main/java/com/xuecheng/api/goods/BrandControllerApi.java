package com.xuecheng.api.goods;

import com.xuecheng.framework.domain.goods.Brand;
import com.xuecheng.framework.domain.goods.request.QueryBrandRequest;
import com.xuecheng.framework.domain.goods.response.BrandResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "品牌管理接口", description = "品牌管理接口")
public interface BrandControllerApi {
    @ApiOperation("获取品牌列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    public QueryResponseResult findList(int page, int size, QueryBrandRequest queryBrandRequest);

    @ApiOperation("添加页面")
    public BrandResult add(Brand brand);
}
