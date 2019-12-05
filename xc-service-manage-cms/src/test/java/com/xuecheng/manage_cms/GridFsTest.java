package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import sun.nio.ch.IOUtil;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTest {
    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Test
    public void queryFile() throws IOException {
        String fileId = "5de8b1c86738f31204292de6";
        // 根据id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        // 打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 创建gridFsResource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        // 获取流中的数据
        String s = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
        s = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>${name}</title>\n" +
                "    <link rel=\"stylesheet\" href=\"http://www.xuecheng.com/plugins/normalize-css/normalize.css\" />\n" +
                "    <link rel=\"stylesheet\" href=\"http://www.xuecheng.com/plugins/bootstrap/dist/css/bootstrap.css\" />\n" +
                "    <link rel=\"stylesheet\" href=\"http://www.xuecheng.com/css/page-learing-index.css\" />\n" +
                "    <link rel=\"stylesheet\" href=\"http://www.xuecheng.com/css/page-header.css\" />\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"banner-roll\">\n" +
                "    <div class=\"banner-item\">\n" +
                "\t<#list model as url>\n" +
                "        <div class=\"item\" style=\"background-image: url(${url.value});\"></div>\n" +
                "\t</#list>\t\n" +
                "    </div>\n" +
                "    <div class=\"indicators\"></div>\n" +
                "</div>\n" +
                "<script type=\"text/javascript\" src=\"http://www.xuecheng.com/plugins/jquery/dist/jquery.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"http://www.xuecheng.com/plugins/bootstrap/dist/js/bootstrap.js\"></script>\n" +
                "<script type=\"text/javascript\">\n" +
                "    var tg = $('.banner-item .item');\n" +
                "    var num = 0;\n" +
                "    for (i = 0; i < tg.length; i++) {\n" +
                "        $('.indicators').append('<span></span>');\n" +
                "        $('.indicators').find('span').eq(num).addClass('active');\n" +
                "    }\n" +
                "\n" +
                "    function roll() {\n" +
                "        tg.eq(num).animate({\n" +
                "            'opacity': '1',\n" +
                "            'z-index': num\n" +
                "        }, 1000).siblings().animate({\n" +
                "            'opacity': '0',\n" +
                "            'z-index': 0\n" +
                "        }, 1000);\n" +
                "        $('.indicators').find('span').eq(num).addClass('active').siblings().removeClass('active');\n" +
                "        if (num >= tg.length - 1) {\n" +
                "            num = 0;\n" +
                "        } else {\n" +
                "            num++;\n" +
                "        }\n" +
                "    }\n" +
                "    $('.indicators').find('span').click(function() {\n" +
                "        num = $(this).index();\n" +
                "        roll();\n" +
                "    });\n" +
                "    var timer = setInterval(roll, 3000);\n" +
                "    $('.banner-item').mouseover(function() {\n" +
                "        clearInterval(timer)\n" +
                "    });\n" +
                "    $('.banner-item').mouseout(function() {\n" +
                "        timer = setInterval(roll, 3000)\n" +
                "    });\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        // 保存html文件到GridFS
        InputStream inputStream = IOUtils.toInputStream(s);
        Object object;
        object = gridFsTemplate.store(inputStream, "ybh测试.html");
        System.out.println(s);
    }
}
