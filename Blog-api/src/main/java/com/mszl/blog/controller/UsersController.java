package com.mszl.blog.controller;

import com.mszl.blog.service.SysUserService;
import com.mszl.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UsersController
{
    @Autowired
    private SysUserService sysUserService;
    
    
    @GetMapping ("currentUser")
    // 因为参数名称是：Authorization .因此通过@RequestHeader 来获取这个头部参数信息
    public Result currentUser(@RequestHeader("Authorization") String token)
    {
        return sysUserService.findUserByToken(token);
    }
}
