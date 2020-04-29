package com.xuecheng.goods.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.goods.Brand;
import com.xuecheng.framework.domain.goods.request.QueryBrandRequest;
import com.xuecheng.framework.domain.goods.response.BrandCode;
import com.xuecheng.framework.domain.goods.response.BrandResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.goods.mapper.BrandMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
@Service
public class BrandService {
    @Autowired
    BrandMapper branderMapper;

    public QueryResponseResult findList(int page, int size, QueryBrandRequest queryBrandRequest) {

        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size < 0) {
            size = 20;
        }
        PageHelper.startPage(page, size);
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(queryBrandRequest.getKey())) {
            example.createCriteria().andLike("name", "%" + queryBrandRequest.getKey() + "%")
                    .orEqualTo("letter", queryBrandRequest.getKey());
        }

        if (StringUtils.isNotBlank(queryBrandRequest.getSortBy())) {
            String orderByClause = queryBrandRequest.getSortBy() + (queryBrandRequest.isDesc() ? " DESC " : " ASC ");
            example.setOrderByClause(orderByClause);
        }
        Page<Brand> pageInfo = (Page<Brand>) branderMapper.selectByExample(example);

        QueryResult<Brand> brandQueryResult = new QueryResult<Brand>();
        brandQueryResult.setList(pageInfo);
        brandQueryResult.setTotal(pageInfo.getTotal());
        return new QueryResponseResult(CommonCode.SUCCESS, brandQueryResult);
    }

    public BrandResult add(Brand brand) {
        if (null == brand) {
            ExceptionCast.cast(BrandCode.BRAND_NOTEXISTS);
        }

        return null;
    }
}
