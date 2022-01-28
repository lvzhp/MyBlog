package com.mszl.blog.vo;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVo
{
    // @JsonSerialize(using = ToStringSerializer.class)  // 保证了雪花算法得到ID的精度
    private String id;
    
    private String title;
    
    private String summary;
    
    private Integer commentCounts;
    
    private Integer viewCounts;
    
    private Integer weight;
    
    // 创建时间
    private String createDate;
    
    private String author;
    
    private ArticleBodyVo body;
    
    private List<TagVo> tags;
    
    private CategoryVo categorys;
}
