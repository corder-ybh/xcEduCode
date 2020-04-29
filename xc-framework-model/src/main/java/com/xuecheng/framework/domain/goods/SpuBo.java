package com.xuecheng.framework.domain.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class SpuBo extends Spu{
    @Transient
    private String cname;   // 商品分类名称

    @Transient
    private String bname;   // 品类名称

    @Transient
    private SpuDetail spuDetail;

    @Transient
    private List<Sku> skus;

    public SpuBo(Long brandId, Long cid1, Long cid2, Long cid3, String title, String subTitle, Boolean saleAble,
                 Boolean valid, Date createTime, Date lastUpdateTime) {
        super(brandId, cid1, cid2, cid3, title, subTitle, saleAble, valid, createTime, lastUpdateTime);
    }
}
