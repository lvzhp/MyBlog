package com.mszl.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mszl.blog.dao.mapper.SysUserMapper;
import com.mszl.blog.dao.pojo.SysUser;
import com.mszl.blog.service.LoginService;
import com.mszl.blog.service.SysUserService;
import com.mszl.blog.vo.ErrorCode;
import com.mszl.blog.vo.LoginUserVo;
import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService
{
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    private LoginService loginService;
    
    @Override
    public UserVo findUserVoById(Long id)
    {
        // 为了防止其为空，如果其没有，设置一个默认的名称
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser==null)
        {
            sysUser= new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("码神之路");
        }
        UserVo userVo = new UserVo();
        userVo.setId(String.valueOf(sysUser.getId()));
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }
    
    @Override
    public SysUser findUserById(Long id)
    {
        // 为了防止其为空，如果其没有，设置一个默认的名称
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser==null)
        {
            sysUser= new SysUser();
            sysUser.setNickname("码神之路");
        }
        return sysUser;
    }
    
    @Override
    public SysUser findUser(String account, String password)
    {
    
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        /**
         * password在数据库中是加密的，因此此处需要在加密
         */
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }
    
    
    @Override
    public Result findUserByToken(String token)
    {
        /**
         * 1. token合法性校验
         *      是否为空，解析是否成功，redis是否存在
         * 2. 如果校验失败，返回错误
         * 3. 如果成功，返回对应的结果LoginUserVo
         */
        
        SysUser sysUser=loginService.checkToken(token);
        if(sysUser==null)
        {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo= new LoginUserVo();
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setAccount(sysUser.getAccount());
        return Result.success(loginUserVo);
    }
    
    @Override
    public SysUser findUserByAcccount(String account)
    {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }
    
    @Override
    public void save(SysUser sysUser)
    {
        // 保存用户 ，id会自动生成，生成的id 默认为分布式ID，采用了雪花算法
        // 为什么采用雪花算法,不可以采用自增的呢？
            // 以后用户多了以后，要进行分表操作，id就需要用到分布式id了。
        this.sysUserMapper.insert(sysUser);
    }
}
