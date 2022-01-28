package com.mszl.blog.vo.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mszl.blog.vo.CategoryVo;
import com.mszl.blog.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam
{
//    // 防止前端精度损失，把id转为String
//    @JsonSerialize (using = ToStringSerializer.class)
    private Long id;
    
    private ArticleBodyParam body;
    
    private CategoryVo category;
    
    private String summary;
    
    private List<TagVo> tags;
    
    private String title;
}
