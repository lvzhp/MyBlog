package com.mszl.blog.common.cache;

import com.alibaba.fastjson.JSON;
import com.mszl.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;


// AOP定义一个切面，而切面定义了切点和通知的关系
@Aspect
@Component
@Slf4j
public class CacheAspect
{
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Pointcut ("@annotation(com.mszl.blog.common.cache.Cache)")
    public void pt()
    {
    
    }
    
    @Around ("pt()")
    public Object around(ProceedingJoinPoint pjb)
    {
        try
        {
            Signature signature = pjb.getSignature();
            // 通过这样一个方法，我们就可以得到类名
            String className = pjb.getTarget().getClass().getSimpleName();
            // 以及拿到调用的方法名
            String methodName = signature.getName();
            
            // 然后拿到对应的参数
            Class[] parameterTypes = new Class[ pjb.getArgs().length ];
            Object[] args = pjb.getArgs();
            // 对参数进行判断
            String params = "";
            for (int i = 0; i < args.length; i++)
            {
                // 如果参数有值
                if (args[ i ] != null)
                {
                    // 因为参数有可能是一个类，因此将其转换为一个Json 便于显示
                    params += JSON.toJSONString(args[ i ]);
                    // 将参数的类型放入到数组中
                    parameterTypes[ i ] = args[ i ].getClass();
                }
                else
                {
                    parameterTypes[i]=null;
                }
            }
            // 根据参数的不同来设置不同的key
            if(StringUtils.isNotEmpty(params))
            {
            //    加密，防止出现key过长以及字符转义获取不到的情况
                params= DigestUtils.md5Hex(params);
            }
            /* 这个主要是为了拿到一个对应的 方法  */
            
            Method method = pjb.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            /* 拿到方法，就能拿到方法上加的这个cache注解 */
            // 获取Cache注解
            Cache annotation = method.getAnnotation(Cache.class);
            // 拿到缓存过期时间
            long expire = annotation.expire();
            // 拿到缓存名称
            String name = annotation.name();
            // 先从redis获取
            String redisKey=name+"::"+className+"::"+methodName+"::"+params;
            //  拿到对应的value
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            
            // 如果value不为空，则直接返回
            if(StringUtils.isNotEmpty(redisValue))
            {
                log.info("走了缓存~~~,{},{}",className,methodName);
                return JSON.parseObject(redisValue, Result.class);
            }
            // 如果value为空,则要访问方法了
            Object proceed = pjb.proceed();
            // 将方法返回的结果转换为json，存到redis
            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("存入缓存~~~{},{}",className,methodName);
            return proceed;
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return  Result.fail(-999,"系统错误");
    }
}
