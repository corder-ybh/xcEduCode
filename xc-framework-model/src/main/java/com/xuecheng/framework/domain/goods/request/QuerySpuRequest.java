package com.xuecheng.framework.domain.goods.request;

import com.xuecheng.framework.model.request.RequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySpuRequest extends RequestData {
    @ApiModelProperty("排序字段")
    private String sortBy;

    @ApiModelProperty("desc")
    private Boolean desc;

    @ApiModelProperty("key")
    private Boolean key;

    @ApiModelProperty("saleable")
    private Boolean saleable;
}
