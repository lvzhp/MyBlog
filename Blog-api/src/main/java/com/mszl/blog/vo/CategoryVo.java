package com.mszl.blog.vo;

import lombok.Data;

@Data
public class CategoryVo
{
    // 防止前端精度损失，把id转为String
    // @JsonSerialize (using = ToStringSerializer.class)
    private String id;
    private String avatar;
    private String categoryName;
    private String description;
}
