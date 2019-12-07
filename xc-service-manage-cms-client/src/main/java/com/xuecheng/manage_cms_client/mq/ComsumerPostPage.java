package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ComsumerPostPage {
    public static final Logger LOGGER = LoggerFactory.getLogger(ComsumerPostPage.class);

    @Autowired
    PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg){
        // 解析消息
        Map map = JSON.parseObject(msg, Map.class);
        //得到消息中的页面id
        String pageId = (String)map.get("pageId");
        // 校验页面是否合法
        CmsPage cmsPage = pageService.getCmsPageById(pageId);
        if (null == cmsPage) {
            LOGGER.error("receive postpage msg, CmsPage is null, pageId : {}", pageId);
        }
        // 调用service方法将页面从GridFs中下载到服务器
        pageService.savePageToServerPath(pageId);
    }
}
