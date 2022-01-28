package com.mszl.blog.service;

import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.param.CommentParam;

public interface CommentsService
{
    /**
     * 根据文章 id 查询所有的评论列表
     * @param id  文章的id
     * @return
     */
    Result commentsByArticleId(Long id);
    
    Result comment(CommentParam commentParam);
    
}
