package com.mszl.blog.service;

import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.param.ArticleParam;
import com.mszl.blog.vo.param.PageParams;

public interface ArticleService
{
    
    /*分页查询*/
    Result listArticle(PageParams pageParams);
    
    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);
    
    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticles(int limit);
    
    /**
     * 文章归档
     * @return
     */
    Result listArchives();
    
    /**
     * 查看文章详情
     * @date: 2021/12/7 20:28
     * @return
     */
    Result findArticleById(Long articleId);
    
    Result publish(ArticleParam articleParam);
}
