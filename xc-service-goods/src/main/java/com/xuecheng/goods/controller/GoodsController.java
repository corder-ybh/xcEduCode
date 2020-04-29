package com.xuecheng.goods.controller;

import com.xuecheng.api.goods.GoodsControllerApi;
import com.xuecheng.framework.domain.goods.SpuBo;
import com.xuecheng.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item/goods")
public class GoodsController implements GoodsControllerApi {

    @Autowired
    private GoodsService goodsService;

    @Override
    @GetMapping("/spu/{id}")
    public SpuBo queryGoodsById(@PathVariable("id") Long id) {
        return this.goodsService.queryGoodsById(id);
    }
}
