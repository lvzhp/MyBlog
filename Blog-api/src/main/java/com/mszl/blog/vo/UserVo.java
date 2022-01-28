package com.mszl.blog.vo;

import lombok.Data;

@Data
public class UserVo
{
    private String nickname;
    
    private String avatar;
    
    // 防止前端精度损失，把id转为String
    // @JsonSerialize (using = ToStringSerializer.class)
    private String id;
}
