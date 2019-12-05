package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryTemplateRequest {
    // 站点id
    @ApiModelProperty("站点id")
    private String siteId;
    // 站点id
    @ApiModelProperty("模板名称")
    private String templateName;
    // 模板文件id
    @ApiModelProperty("模板文件id")
    private String templateField;
}
