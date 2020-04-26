package com.xuecheng.api.goods;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "品类管理接口", description = "品类管理接口")
public interface CategoryControllerApi {
    @ApiOperation("获取品类列表")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "pid", value = "parent id", required = false, paramType = "path", dataType = "long")
    )
    public QueryResponseResult queryCategoryListByParent (Long pid);
}
