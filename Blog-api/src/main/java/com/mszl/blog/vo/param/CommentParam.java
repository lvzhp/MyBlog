package com.mszl.blog.vo.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CommentParam
{
    // 防止前端精度损失，把id转为String
    @JsonSerialize (using = ToStringSerializer.class)
    private Long articleId;
    
    private String content;
    
    private Long parent;
    
    private Long toUserId;
}
