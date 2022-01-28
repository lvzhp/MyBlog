package com.mszl.blog.controller;

import com.mszl.blog.common.aop.LogAnnotation;
import com.mszl.blog.common.cache.Cache;
import com.mszl.blog.service.ArticleService;
import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.param.ArticleParam;
import com.mszl.blog.vo.param.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*json数据进行交互*/
@ResponseBody
@RestController
@RequestMapping ("/articles")
public class ArticleController
{
    @Autowired
    private ArticleService articleService;
    
    
    /*因为接口是Post，所以采用PostMapping*/
    /*
        又因为参数是page 和pageSize 因此建立一个vo.params 里面定义
     * 又因为返回值根据json类型，在vo下创建一个返回值
     * */
    @PostMapping
    // public void listArticle(@RequestBody PageParams pageParams)
    
    
    // 加上此注解，代表要对此接口记录日志
    @LogAnnotation (module= "文章",operator="获取文章列表")
    @Cache(expire = 5*60*1000,name = "listArticle")
    public Result listArticle(@RequestBody PageParams pageParams)
    {
//        int i=10/0;
        return articleService.listArticle(pageParams);
    }
    
    
    
    
    
    
    
    
    /**
     * 首页 最热文章
     *
     */
    @PostMapping("hot")
    //加入切点，将其放入到缓存中
    @Cache(expire = 5*60*1000,name="hot_article")
    public Result hotArticle()
    {
        int limit = 5;
        return articleService.hotArticle(limit);
    }
    
    @PostMapping("new")
    @Cache(expire = 5*60*1000,name = "new_article")
    public Result newArticles()
    {
        int limit=5;
        return articleService.newArticles(limit);
    }
    
    @PostMapping("listArchives")
    public Result listArchives()
    {
        return articleService.listArchives();
    }
    
    
    
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId)
    {
        return articleService.findArticleById(articleId);
    }
    
    
    
    @PostMapping("publish")
    public Result pulish(@RequestBody ArticleParam articleParam)
    {
        return articleService.publish(articleParam);
    }
}
