package com.mszl.blog.dao.dos;

import lombok.Data;


// Do 对象也是数据库的，但是不需要持久化的操作，是通过Sql命令得到的



@Data
public class Archives
{
    private Integer year;
    private Integer month;
    private Long count;
}
