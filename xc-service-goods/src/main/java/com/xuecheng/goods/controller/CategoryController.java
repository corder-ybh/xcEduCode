package com.xuecheng.goods.controller;

import com.xuecheng.api.goods.CategoryControllerApi;
import com.xuecheng.framework.domain.goods.Category;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.goods.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goods/category")
public class CategoryController implements CategoryControllerApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;

    @Override
    @GetMapping("/list/{pid}")
    public QueryResponseResult queryCategoryListByParent(@PathVariable("pid") Long pid) {
       if (pid == null || pid.longValue() < 0) {
           return new QueryResponseResult(CommonCode.INVALID_PARAM, null);
       }
       LOGGER.debug("pid:" + pid);
        List<Category> res = categoryService.queryCategoryListByParentId(pid);
       return new QueryResponseResult(CommonCode.INVALID_PARAM, null);
    }
}
