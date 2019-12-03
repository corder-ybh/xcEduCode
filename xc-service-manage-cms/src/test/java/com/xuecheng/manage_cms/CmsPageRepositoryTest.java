package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Test
    public void testFindPage() {
        int page = 0; //从0开始
        int size = 10; // 每页记录数
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    @Test
    public void testFindAll() {
        // 条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 页面别名模糊查询, 需要自定义字符串的匹配器实现模糊查询
//        ExampleMatcher.GenericPropertyMatchers.contains(); // 包含
//        ExampleMatcher.GenericPropertyMatchers.startsWith(); // 开头匹配
        // 条件值
        CmsPage cmsPage = new CmsPage();
        // 站点ID
        cmsPage.setTemplateId("5a751fab6abb5044e0d19ea1");
        // 模板ID
        cmsPage.setTemplateId("5a962c16b00ffc514038fafd");
        // 创建条件实例
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Pageable pageable = new PageRequest(0, 10);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all);
    }

    @Test
    public void testRestTemplate() {
        ResponseEntity<Map> forEntity =
                restTemplate.getForEntity("http://127.0.0.1:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        System.out.println(forEntity);
    }

    @Test
    public void testGridFs() throws FileNotFoundException {
        // 要存储的文件
        File file = new File("e:/index_banner.html");
        // 定义输入流
        FileInputStream inputStream = new FileInputStream(file);
        // 向GridFs存储文件
        ObjectId objectId = gridFsTemplate.store(inputStream, "轮播图测试文件", "");
        //得到文件ID
        String fileID = objectId.toString();
        System.out.println(file);
    }
}
