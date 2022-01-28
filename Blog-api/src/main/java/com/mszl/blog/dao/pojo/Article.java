package com.mszl.blog.dao.pojo;

import lombok.Data;

@Data
public class Article
{
    public static final int Article_TOP=1;
    
    public static final int Article_Common=0;
    
    private Long id;
    
    private String title;
    
    private String summary;
    
    // 注意此处必须为Integer ,因为如果为基本类型int,那么默认是为0的，那么在mybatis-plus中，只要数据不为null，就会参与到sql语句中
    // 而在更新语句中，并不需要这个属性，但是如果改为int，就会也参与到sql语句中，此处就会变为0.
    private Integer commentCounts;
    // 注意此处必须为Integer ,因为如果为基本类型int,那么默认是为0的，那么在mybatis-plus中，只要数据不为null，就会参与到sql语句中
    // 而在更新语句中，并不需要这个属性，但是如果改为int，就会也参与到sql语句中，此处就会变为0.
    private Integer viewCounts;
    
    /*作者ID*/
    private Long authorId;
    /*内容ID*/
    private Long bodyId;
    /*类别Id*/
    private Long categoryId;
    /*置顶*/
    // 注意此处必须为Integer ,因为如果为基本类型int,那么默认是为0的，那么在mybatis-plus中，只要数据不为null，就会参与到sql语句中
    // 而在更新语句中，并不需要这个属性，但是如果改为int，就会也参与到sql语句中，此处就会变为0.
    private Integer weight;
    /*创建时间*/
    private Long createDate;
    
}
