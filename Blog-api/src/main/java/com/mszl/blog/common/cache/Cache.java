package com.mszl.blog.common.cache;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache
{
    long expire() default 1*60*1000;
    // 缓存的时候有一个标识，存入缓存的时候肯定有一个key，我们给一个name标识，这样我们可以自定义前缀的标识
    String name() default "";
}
