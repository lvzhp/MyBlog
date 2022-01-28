package com.mszl.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.mszl.blog.dao.pojo.SysUser;
import com.mszl.blog.service.LoginService;
import com.mszl.blog.service.SysUserService;
import com.mszl.blog.utils.JWTUtils;
import com.mszl.blog.vo.ErrorCode;
import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.param.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional  // 此处加上事务，是为了防止当redis服务器挂掉的时候，注册用户依然会成功存入到数据库中，
public class LoginServiceImpl implements LoginService
{
    // 用login 功能肯定用到UserService,因为一登录你肯定要去查询用户的表
    @Autowired
    private SysUserService sysUserService;
    
    // 整合redis ,windows启动是点击redis-server ,启动查询是redis-cli
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    // 加密盐
    private static final String slat = "mszlu!@#";
    
    @Override
    public Result login(LoginParam loginParam)
    {
        /**
         * 1. 检查参数是否合法
         * 2. 根据用户名和密码去user表中查询是否存在
         * 3. 如果不存在，就登陆失败
         * 4. 如果存在，使用jwt 生成token ，返回给前端
         * 5. token放入redis中， reids token: user信息  设置过期时间
         * （登陆认证的时候，先认证token字符串是否合法，然后去redis认证是否存在）
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        // 第一步
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password))
        {
            // 每次都这样写错误代码，太麻烦了，因此整合错误代码到ErrorCode中
//            return Result.fail(10001,"参数不合法");
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        // 第二步
        // 此处需要对密码进行加密，同时为了防止破解，加入加密盐
        
        password = DigestUtils.md5Hex(password + slat);
        
        SysUser sysUser = sysUserService.findUser(account, password);
        if (sysUser == null)
        {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        // 第三步
        String token = JWTUtils.createToken(sysUser.getId());
        // user信息是String，因此使用JSONString转成对应的String
        // 以及设置1天的过期时间
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
        return Result.success(token);
    }
    
    @Override
    public SysUser checkToken(String token)
    {
        if (StringUtils.isBlank(token))
        {
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null)
        {
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson))
        {
            return null;
        }
        // 把userjson 解析为User的对象
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }
    
    @Override
    public Result logout(String token)
    {
        redisTemplate.delete("TOKEN_" + token);
        return Result.success(null);
    }
    
    @Override
    public Result register(LoginParam loginParam)
    {
        /**
         * 1. 判断参数是否合法
         * 2. 判断账户是否已存在，若存在，则返回账户已经被注册。
         * 3. 如果账户不存在，则注册用户
         * 4. 生成Token
         * 5. 存入Redis。 并返回
         * 6. 注意： 加上事务，一旦中间的任何过程出现问题，注册的用户需要回滚
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if(StringUtils.isBlank(account)||StringUtils.isBlank(password)||StringUtils.isBlank(nickname))
        {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser=sysUserService.findUserByAcccount(account);
        if(sysUser!=null)
        {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),"账号已存在");
        }
        // 第三步
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);
        
        // 第四步
        String token = JWTUtils.createToken(sysUser.getId());
        // 第五步
        redisTemplate.opsForValue().set("TOKEN_"+token,JSON.toJSONString(sysUser),1,TimeUnit.DAYS);
        return Result.success(token);
    }
}
