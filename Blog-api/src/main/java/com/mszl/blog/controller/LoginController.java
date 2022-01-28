package com.mszl.blog.controller;


import com.mszl.blog.service.LoginService;
import com.mszl.blog.service.SysUserService;
import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.param.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController
{
    @Autowired
    // 这样引入SysUserService 是不好的，因为他是负责用户表的操作，但是login是业务。那么就需要专门设置一个业务的service来完成
//    private SysUserService sysUserService;
    private LoginService loginService;
    
    @PostMapping
    public Result login(@RequestBody LoginParam loginParam)
    {
        // 登陆  就需要验证用户， 访问用户表
        // 里面是需要传参数的，根据浏览器得知，里面是json格式的，因此需要创建一个LoginParam
        return loginService.login(loginParam);
    }
}
