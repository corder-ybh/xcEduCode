package com.xuecheng.framework.domain.goods;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_spu_detail")
@Data
public class SpuDetail {
    @Id
    private Long spuId;     //对应的SPU的ID
    private String description;     // 商品描述
    private String specTemplate;    // 商品特殊规格的名称以及可选值的模板
    private String Specifications;  // 商品的全规格属性
    private String packingList;     // 包装清单
    private String afterService;    // 售后服务
}
