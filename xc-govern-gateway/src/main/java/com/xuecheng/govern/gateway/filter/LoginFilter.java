package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
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

    // todo 登录过滤先关掉
    @Override
    public boolean shouldFilter() {
        return false; // 该过滤器需要执行
    }

    /**
     * run方法
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        // 上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 请求对象
        HttpServletRequest request = requestContext.getRequest();
        // 得到response
        HttpServletResponse response = requestContext.getResponse();
        // 取出cookie中的身份令牌
        String tokenFromCookie = authService.getTokenFromCookie(request);
        if (StringUtils.isEmpty(tokenFromCookie)) {
            // 拒绝访问
            access_denied();
            return null;
        }
        // 从header取jwt
        String jwtFromHeader = authService.getJwtFromHeader(request);
        if (StringUtils.isEmpty(jwtFromHeader)) {
            // 拒绝访问
            access_denied();
            return null;
        }
        // 从redis中取出jwt的过期时间
        long expire = authService.getExpire(tokenFromCookie);
        if (expire < 0) {
            // 拒绝访问
            access_denied();
            return null;
        }
        return null;
    }

    /**
     * 拒绝访问
     */
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
