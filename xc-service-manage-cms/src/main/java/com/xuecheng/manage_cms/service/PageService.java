package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;
import sun.nio.ch.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    CmsConfigRepository cmsConfigRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 页面列表分页查询
     * @param page 当前页码
     * @param size 页面显示个数
     * @param queryPageRequest 查询条件
     * @return 页面列表
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        // 条件匹配器
        // 页面名称模糊查询，需要自定义字符串的匹配器实现模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 条件值
        CmsPage cmsPage = new CmsPage();
        // 站点ID
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        // 页面别名
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        // 创建条件实例
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        if (page <= 0) {
            page = 1;
        }
        page = page - 1;//为了适应mongodb的接口将页码减1         
        if (size <= 0)
        {
            size = 20;
        }
        // 分页对象
        Pageable pageable = new PageRequest(page, size);
        // 分页查询
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<CmsPage>();
        cmsPageQueryResult.setList(all.getContent());
        cmsPageQueryResult.setTotal(all.getTotalElements());
        // 返回结果
        return new QueryResponseResult(CommonCode.SUCCESS, cmsPageQueryResult);
    }

    // 添加页面
    public CmsPageResult add(CmsPage cmsPage) {
        if (null == cmsPage) {
            // 抛出异常，非法请求
        }

        // 校验页面是否存在，根据页面名称、站点id、页面webpath查询
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

        if (cmsPage1 != null) {
            // 抛出异常，页面已存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTS);
        }
        // 页面主键由spring data 自动生成
        cmsPage.setPageId(null);
        // 保存
        cmsPageRepository.save(cmsPage);
        // 返回结果
        CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        return cmsPageResult;
    }

    /**
     * 根据id查询页面
     * @param id
     * @return
     */
    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        // 返回空
        return null;
    }

    /**
     * 更新页面
     * @param id
     * @param cmsPage
     * @return
     */
    public CmsPageResult update(String id, CmsPage cmsPage) {
        // 根据id查询页面信息
        CmsPage one = this.getById(id);
        if (one != null) {
            // 更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            // 更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            // 更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            // 更新页面名称
            one.setPageName(cmsPage.getPageName());
            // 更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            // 更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            // 更新dataUrl
            one.setDataUrl(cmsPage.getDataUrl());
            // 执行更新
            CmsPage save = cmsPageRepository.save(one);
            if (save != null) {
                // 返回成功
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, save);
                return cmsPageResult;
            }
        }
        // 返回失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
     * 根据id删除页面
     * @param id
     * @return
     */
    public ResponseResult delete(String id) {
        CmsPage one = this.getById(id);
        if (one != null) {
            // 删除页面
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult((CommonCode.FAIL));
    }

    /**
     * 页面静态化
     * @param pageId
     * @return
     */
    public String getPageHtml(String pageId) {
        // 获取页面模型数据
        Map model = this.getModelByPageId(pageId);
        if (null == model) {
            // 数据模型获取不到
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        // 获取页面的模板信息
        String template = getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(template)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        // 执行静态化
        String html = generateHtml(template, model);
        return html;
    }

    /**
     * 执行静态化
     * @param templateContent
     * @param model
     * @return
     */
    private String generateHtml(String templateContent, Map model) {
        // 创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateContent);
        // 向configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        // 获取模板
        try {
            Template template = configuration.getTemplate("template");
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取页面的模板信息
     * @param pageId
     * @return
     */
    private String getTemplateByPageId(String pageId) {
        // 取出页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if (null == cmsPage) {
            // 页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 获取页面的模板id
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        // 查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            // 获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            // 从GridFS中取模板文件内容
            // 根据文件id查询文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));

            // 打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            // 创建GridFSResource对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            // 从流中取数据
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取数据模型
     * @param pageId
     * @return
     */
    private Map getModelByPageId(String pageId) {
        // 取出页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if (null == cmsPage) {
            // 页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 取出页面的dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            // 页面dataUrl为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        // 通过restTemplate请求dataUrl获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    /**
     * 页面发布
     * @param pageId
     * @return
     */
    public ResponseResult postPage(String pageId) {
        // 执行静态化
        String pageHtml = this.getPageHtml(pageId);
        if (StringUtils.isEmpty(pageHtml)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        // 保存静态化文件
        CmsPage cmsPage = saveHtml(pageId, pageHtml);
        // 发送消息
        sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 发哦送页面发布消息
     * @param pageId
     */
    private void sendPostPage(String pageId) {
        CmsPage cmsPage = this.getById(pageId);
        if (null == cmsPage) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("pageId", pageId);
        // 消息内容
        String msg = JSON.toJSONString(msgMap);
        // 获取站点id作为routingKey
        String siteId = cmsPage.getSiteId();
        // 发布消息
        this.rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE, siteId, msg);
    }

    /**
     * 将html文件保存到gridfs然后更新CmsPage的htmlFiledId
     * @param pageId
     * @param content
     * @return
     */
    private CmsPage saveHtml(String pageId, String content) {
        // 查询页面
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = optional.get();
        // 存储之前先删除
        String htmlFileId = cmsPage.getHtmlFileId();
        if (StringUtils.isNotEmpty(htmlFileId)) {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }
        // 保存html文件到GridFS
        InputStream inputStream = IOUtils.toInputStream(content);
        Object object = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        // 文件id
        String fileId = object.toString();
        // 将文件id存储到cmspage中
        cmsPage.setHtmlFileId(fileId);
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }
}
