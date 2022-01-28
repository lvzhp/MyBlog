package com.mszl.blog.service;

import com.mszl.blog.dao.pojo.SysUser;
import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.UserVo;

public interface SysUserService
{
    UserVo findUserVoById(Long id);
    
    SysUser findUserById(Long id);
    
    SysUser findUser(String account, String password);
    
    // 根据token查询用户信息
    Result findUserByToken(String token);
    
    // 根据账户查找用户
    SysUser findUserByAcccount(String account);
    
    // 保存用户
    void save(SysUser sysUser);
}
