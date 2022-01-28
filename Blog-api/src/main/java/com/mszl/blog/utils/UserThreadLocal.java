package com.mszl.blog.utils;

import com.mszl.blog.dao.pojo.SysUser;

public class UserThreadLocal
{
    private UserThreadLocal()
    {
    }
    
    // ThreadLocal是用来做线程变量隔离的： 想要在每个线程里面存储每个线程特定的信息就可以用ThreadLocal
    // 因为ThreadLocal相当于在每个线程上面创建一个单独的线程，存储只给这个线程使用的一些信息，其他线程无法访问其他线程的ThreadLocaL信息
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();
    
    public static void put(SysUser sysUser)
    {
        LOCAL.set(sysUser);
    }
    public static SysUser get()
    {
        return  LOCAL.get();
    }
    
    public static void remove()
    {
        LOCAL.remove();
    }
}
