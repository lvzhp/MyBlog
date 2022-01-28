package com.mszl.blog.controller;


import com.mszl.blog.service.LoginService;
import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.param.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController
{
    @Autowired
    private LoginService loginService;
    @PostMapping
    // 用RequestBody 来接受参数
    public Result register(@RequestBody LoginParam loginParam)
    {
        // sso 单点登陆，后期如果把登陆注册功能单独提出去 （单独的服务，可以独立提供接口服务）
        return loginService.register(loginParam);
    }
}
