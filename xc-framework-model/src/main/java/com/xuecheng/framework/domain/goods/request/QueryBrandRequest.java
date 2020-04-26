package com.xuecheng.framework.domain.goods.request;

import com.xuecheng.framework.model.request.RequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryBrandRequest extends RequestData {
    @ApiModelProperty("排序方式")
    private String sortBy;
    @ApiModelProperty("是否倒序")
    private boolean desc;
    @ApiModelProperty("关键字")
    private String key;
}
