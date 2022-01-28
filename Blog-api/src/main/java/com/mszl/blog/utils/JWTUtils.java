package com.mszl.blog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils
{
    // 这个就是A+B 然后加上秘钥的  那个 秘钥
    private static final String jwtToken = "123456Mszlu!@#$$";
    
    
    public static String createToken(Long userId)
    {
        // 这个部分就是B部分
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
        
        
        JwtBuilder jwtBuilder = Jwts.builder()
                // 这个就是A部分的算法
                .signWith(SignatureAlgorithm.HS256,jwtToken)// 签发算法，秘钥为jwtToken
                .setClaims(claims)// body数据，要唯一，自行设置
                .setIssuedAt(new Date())// 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis()+24*60*60*60*1000));// 一天的有效时间
        String token = jwtBuilder.compact();
        return token;
    }
    
    public static  Map<String, Object> checkToken(String token)
    {
        try
        {
            // 提供秘钥去解析
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
