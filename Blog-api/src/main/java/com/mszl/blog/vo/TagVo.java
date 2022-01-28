package com.mszl.blog.vo;

import lombok.Data;
// Vo是和页面进行交互的数据，这个数据不应该和数据库的对象做耦合。因此要分开
@Data
public class TagVo
{
    // 防止前端精度损失，把id转为String
    // @JsonSerialize (using = ToStringSerializer.class)
    private String id;
    private String tagName;
    private String avatar;
}
