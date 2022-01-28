package com.mszl.blog.service;

import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.TagVo;

import java.util.List;

public interface TagService
{
    List<TagVo> findTagsByArticleId(Long articleId);
    
    Result hots(int limit);
    
    Result findAll();
    
    Result findAllDetail();
    
    Result findDetailById(Long id);
}
