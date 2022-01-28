package com.mszl.blog.vo;

import lombok.Data;

@Data
public class LoginUserVo
{
    // 防止前端精度损失，把id转为String
    // @JsonSerialize (using = ToStringSerializer.class)
    private String id;
    private String account;
    private String nickname;
    private String avatar;
}
