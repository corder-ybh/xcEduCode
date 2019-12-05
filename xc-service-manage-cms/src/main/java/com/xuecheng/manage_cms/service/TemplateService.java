package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryTemplateRequest;
import com.xuecheng.framework.domain.cms.response.CmsTemplateResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TemplateService {
    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    public QueryResponseResult findList(int page, int size, QueryTemplateRequest queryTemplateRequest) {
        // todo 这个玩意的用法
        // 条件匹配器
        // 页面名称模糊查询，需要自定义字符串的匹配器实现模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("templateName", ExampleMatcher.GenericPropertyMatchers.contains());
        // 条件值
        CmsTemplate cmsTemplate = new CmsTemplate();
        // 站点ID
        if (StringUtils.isNotEmpty(queryTemplateRequest.getSiteId())) {
            cmsTemplate.setSiteId(queryTemplateRequest.getSiteId());
        }

        // 创建条件实例
        Example<CmsTemplate> example = Example.of(cmsTemplate, exampleMatcher);

        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        // 分页对象
        // todo 这个分页的原理是什么，然后现在提示过时了，应该用什么替代呢
        Pageable pageable = new PageRequest(page, size);
        // 分页查询
        Page<CmsTemplate> all = cmsTemplateRepository.findAll(example, pageable);
        QueryResult<CmsTemplate> cmsTemplateQueryResult = new QueryResult<>();
        cmsTemplateQueryResult.setList(all.getContent());
        cmsTemplateQueryResult.setTotal(all.getTotalElements());
        // 返回结果
        return new QueryResponseResult(CommonCode.SUCCESS, cmsTemplateQueryResult);
    }

    /**
     * 根据id返回模板信息
     * @param id
     * @return
     */
    public CmsTemplate getById(String id) {
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

//    public CmsTemplateResult update(String id, CmsTemplate cmsTemplate) {
//        CmsTemplate cmsTemplate1 = this.getById(id);
//    }
}
