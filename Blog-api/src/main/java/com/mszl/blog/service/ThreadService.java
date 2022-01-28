package com.mszl.blog.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mszl.blog.dao.mapper.ArticleMapper;
import com.mszl.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService
{
    
    
    
    
    
    // 期望此操作在线程池中进行执行，不会影响原有的 主线程
    @Async("taskExecutor") // 里面是线程池名称
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article)
    {
        int viewCounts = article.getViewCounts();
        Article articleUpdate  = new Article();
        articleUpdate.setViewCounts(viewCounts+1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        // 为了在多线程的环境下保证线程安全问题
        updateWrapper.eq(Article::getViewCounts,viewCounts);
        // sql:   update article set view_count =100  where  view_count =99 and id =xxx
        articleMapper.update(articleUpdate,updateWrapper);
        try
        {
            Thread.sleep(5000);
            System.out.println("更新完成");
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
