package com.xuecheng.goods.service;

import com.xuecheng.framework.domain.goods.*;
import com.xuecheng.goods.mapper.SkuMapper;
import com.xuecheng.goods.mapper.SpuDetailMapper;
import com.xuecheng.goods.mapper.SpuMapper;
import com.xuecheng.goods.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    /**
     * 第一页所需信息
     * @param id
     * @return
     */
    public SpuBo queryGoodsById(Long id) {
        // 使用spu主键获取到spu
        Spu spu = this.spuMapper.selectByPrimaryKey(id);
        // 使用spu主键获取到spu的详细信息
        SpuDetail spuDetail = this.spuDetailMapper.selectByPrimaryKey(spu.getId());

        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId", spu.getId());
        // 这里使用spu的主键获取到spu下面的sku
        List<Sku> skuList = this.skuMapper.selectByExample(example);
        // 获取sku主键列表
        List<Long> skuIdList = new ArrayList<>();
        for (Sku sku : skuList) {
            skuIdList.add(sku.getId());
        }
        // 这里能用这个功能是因为stockMapper实现了接口SelectByIdListMapper
        List<Stock> stocks = this.stockMapper.selectByIdList(skuIdList);
        for (Sku sku : skuList) {
            for (Stock stock : stocks) {
                // 这里本来可以使用left join一条sql查出来的，但是这里没有
                if (sku.getId().equals(stock.getSkuId())) {
                    sku.setStock(stock.getStock());
                }
            }
        }

        SpuBo spuBo = new SpuBo(spu.getBrandId(), spu.getCid1(), spu.getCid2(), spu.getCid3(), spu.getTitle(),
                spu.getSubTitle(), spu.getSaleable(), spu.getValid(), spu.getCreateTime(), spu.getLastUpdateTime());
        spuBo.setSpuDetail(spuDetail);
        spuBo.setSkus(skuList);
        return spuBo;
    }
}
