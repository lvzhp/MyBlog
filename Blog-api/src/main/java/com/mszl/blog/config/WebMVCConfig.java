package com.mszl.blog.config;


import com.mszl.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer
{
    @Autowired
    private LoginInterceptor loginInterceptor;
    
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        /*重写这个方法是为了跨域配置
         * 因为前端端口是8080，而我这个项目端口是8888，相当于两个域名
         * 会有跨域问题，因此我们需要让8080，可以访问8888
         **/
        /*允许这个域名可以访问*/
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        // 此处拦截test接口，后续实际遇到需要拦截的接口时，再配置为真正的拦截接口
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/test")
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish");
    }
}
