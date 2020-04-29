package com.xuecheng.goods.controller;

import com.xuecheng.api.goods.BrandControllerApi;
import com.xuecheng.framework.domain.goods.Brand;
import com.xuecheng.framework.domain.goods.request.QueryBrandRequest;
import com.xuecheng.framework.domain.goods.response.BrandResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item/brand")
public class BrandController implements BrandControllerApi {
    @Autowired
    private BrandService brandService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryBrandRequest queryBrandRequest) {
        return brandService.findList(page, size, queryBrandRequest);
    }

    @Override
    @PostMapping("/add")
    public BrandResult add(Brand brand) {
        return brandService.add(brand);
    }

}
