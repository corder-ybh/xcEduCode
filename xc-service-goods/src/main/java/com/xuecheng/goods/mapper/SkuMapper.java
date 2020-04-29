package com.xuecheng.goods.mapper;

import com.xuecheng.framework.domain.goods.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SkuMapper extends Mapper<Sku> {
    @Select("SELECT ts_sku.*,ts_sk.stock FROM tb_sku ts_sku INNER JOIN tb_stock ts_sk ON ts_sku.id = ts_sk.sku_id WHERE ts_sku.spu_id = #{id}")
    List<Sku> queryById(@Param("id") Long id);
}
