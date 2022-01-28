package com.mszl.blog.service;

import com.mszl.blog.dao.pojo.SysUser;
import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.param.LoginParam;
import org.springframework.transaction.annotation.Transactional;


public interface LoginService
{
    // 登陆功能
    Result login(LoginParam loginParam);
    
    SysUser checkToken(String token);
    
    Result logout(String token);
    
    Result register(LoginParam loginParam);
    
}
