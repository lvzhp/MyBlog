package com.mszl.blog.handler;

import com.alibaba.fastjson.JSON;
import com.mszl.blog.dao.pojo.SysUser;
import com.mszl.blog.service.LoginService;
import com.mszl.blog.utils.UserThreadLocal;
import com.mszl.blog.vo.ErrorCode;
import com.mszl.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor
{
    @Autowired
    private LoginService loginService;
    
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        // 在执行controller方法（Handler）之前进行执行
        
        /**
         * 1. 需要判断 请求的接口路径， 是否为HandlerMethod(Controller方法) 如果是则放行
         * 2. 判断token是否为空，如果为空---> 未登录
         * 3. 如果token不为空，登陆验证loginService checkToken
         * 4. 如果认证成功，放行即可
         */
        // 第一步
        if (!(handler instanceof HandlerMethod))
        {
            // handler 可能是 RequestResourceHandler    Springboot程序访问静态资源默认去classpath下的static目录下去查询
            return true;
        }
        // 打印一下日志方便DEBUG
        String token = request.getHeader("Authorization");
        log.info("=====request start=====");
        String requestURI=request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}",token);
        log.info("=====request end=====");
    
    
        // 第二步
        if (StringUtils.isBlank(token))
        {
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            // 让浏览器识别到他返回的是JSON
            response.setContentType("application/json;charset=utf-8");
            // 返回的是Json信息。把他转成Json
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        // 第三步
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null)
        {
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            // 让浏览器识别到他返回的是JSON
            response.setContentType("application/json;charset=utf-8");
            // 返回的是Json信息。把他转成Json
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        // 登陆验证成功，放行
        // 我希望在controller 中 直接获取用户的信息，怎么获取？
        UserThreadLocal.put(sysUser);
        return true;
        // 注意此时，拦截器还不能用，需要在WebMVCConfig 中进行声明
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
        // 如果不删除ThreadLocal中用完的信息，就会有内存泄漏的风险
        UserThreadLocal.remove();
    }
}
