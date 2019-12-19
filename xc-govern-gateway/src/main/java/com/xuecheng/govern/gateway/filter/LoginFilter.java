package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class LoginFilter extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    AuthService authService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2; // int值来定义过滤器的执行顺序，数值越小优先级越高
    }


    @Override
    public boolean shouldFilter() {
        return true; // 该过滤器需要执行
    }

    @Override
    public Object run() {
        // 上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 请求对象
        HttpServletRequest request = requestContext.getRequest();
        // 得到response
        HttpServletResponse response = requestContext.getResponse();
        // 取出cookie中的身份令牌
        String tokenFromCookie = authService.getTokenFromCookie(request);
        if (StringUtils.isEmpty(tokenFromCookie)) {

        }
        if (StringUtils.isEmpty(tokenFromCookie)) {
            requestContext.setSendZuulResponse(false); // 拒绝访问
            requestContext.setResponseStatusCode(200); // 设置响应状态码
            ResponseResult unauthenticated = new ResponseResult(CommonCode.UNAUTHENTICATED);
            String jsonString = JSON.toJSONString(unauthenticated);
            requestContext.setResponseBody(jsonString);
            requestContext.getResponse().setContentType("application/json;charset=UTF-8");
            return null;
        }
        return null;
    }

    private void access_denied() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 得到response
        HttpServletResponse response = requestContext.getResponse();
        // 拒绝访问
        requestContext.setSendZuulResponse(false);
        // 设置响应代码
        requestContext.setResponseStatusCode(200);
        // 构建响应的信息
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        // 转成json
        String jsonString = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(jsonString);
        //转成json，设置contentType
        response.setContentType("application/json;charset=utf-8");
    }
}
