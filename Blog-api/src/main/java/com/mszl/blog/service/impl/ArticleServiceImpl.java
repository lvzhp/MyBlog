package com.mszl.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mszl.blog.dao.dos.Archives;
import com.mszl.blog.dao.mapper.ArticleBodyMapper;
import com.mszl.blog.dao.mapper.ArticleMapper;
import com.mszl.blog.dao.mapper.ArticleTagMapper;
import com.mszl.blog.dao.pojo.Article;
import com.mszl.blog.dao.pojo.ArticleBody;
import com.mszl.blog.dao.pojo.ArticleTag;
import com.mszl.blog.dao.pojo.SysUser;
import com.mszl.blog.service.*;
import com.mszl.blog.utils.UserThreadLocal;
import com.mszl.blog.vo.ArticleBodyVo;
import com.mszl.blog.vo.ArticleVo;
import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.TagVo;
import com.mszl.blog.vo.param.ArticleParam;
import com.mszl.blog.vo.param.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService
{
    @Resource
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    
    @Override
    public Result listArticle(PageParams pageParams)
    {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page, pageParams.getCategoryId(), pageParams.getTagId(), pageParams.getYear(), pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records, true, true));
    }
    
    
    // @Override
    // public Result listArticle(PageParams pageParams)
    // {
    //     //分页查询article 数据库表
    //
    //
    //     /**
    //      * 因为是对Article 进行分页，因此参数里面传的是Article类型的参数
    //      */
    //     Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
    //     /**
    //      *  LambdaQueryWrapper 是QueryMapper使用lambda格式
    //      */
    //     LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
    //
    //     if(pageParams.getCategoryId()!=null)
    //     {
    //         // 相当于 ：  and category_id = # {categoryId}
    //         queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
    //     }
    //     List<Long> articleIdList = new ArrayList<>();
    //     if(pageParams.getTagId()!=null)
    //     {
    //         // 加入标签条件查询
    //         //  article 表中 并没有tag字段 ： 因为一篇文章有多个标签
    //         // article_tag  是一对多的关系  article_id  1：n  tag_id
    //         LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
    //         articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
    //         List<ArticleTag> articleTags= articleTagMapper.selectList(articleTagLambdaQueryWrapper);
    //         for(ArticleTag articleTag:articleTags)
    //         {
    //             articleIdList.add(articleTag.getArticleId());
    //         }
    //         if(articleIdList.size()>0)
    //         {
    //             queryWrapper.in(Article::getId,articleIdList);
    //         }
    //     }
    //
    //
    //
    //
    //
    //
    //
    //     // 是否置顶排序
    //     queryWrapper.orderByDesc(Article :: getWeight);
    //     // 相当于: order by crreateDate desc
    //     queryWrapper.orderByDesc(Article :: getCreateDate);
    //     // 这样就得到了分页好了的page
    //     Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
    //     // 得到page后，也能得到其中的list
    //     List<Article> records = articlePage.getRecords();
    //     // 能直接返回吗？很明显是不能的。因为每一个都是一个对象
    //
    //
    //     // 转成对应的Vo对象
    //     List<ArticleVo> articleVoList = copyList(records,true,true);
    //     return Result.success(articleVoList);
    // }
    
    @Override
    public Result hotArticle(int limit)
    {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article :: getViewCounts);
        queryWrapper.select(Article :: getId, Article :: getTitle);
        queryWrapper.last("limit " + limit);
        
        List<Article> articles = articleMapper.selectList(queryWrapper);
        
        return Result.success(copyList(articles, false, false));
    }
    
    @Override
    public Result newArticles(int limit)
    {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article :: getCreateDate);
        queryWrapper.select(Article :: getId, Article :: getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        
        return Result.success(copyList(articles, false, false));
    }
    
    @Override
    public Result listArchives()
    {
        /*select year(FROM_UNIXTIME(create_date/1000)) year,month(FROM_UNIXTIME(create_date/1000)) month, count(*) count from ms_article group by year,month;*/
        // 返回的数据 需要是 year mouth 不是真实存在的数据，是通过数据库SQL语句得到的。因此需要创建Dos 也就是不需要持久化的数据
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }
    
    
    @Autowired
    private ThreadService threadService;
    
    /**
     * 更新阅读为什么要放在这个文章呢？ 因为打开文章一次就得更新一次阅读数嘛，很合理
     *
     * @param articleId
     *
     * @return
     */
    @Override
    public Result findArticleById(Long articleId)
    {
        /**
         * 1. 根据id查询文章信息
         * 2. 根据bodyid和categoryid去做关联查询
         */
        
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);
        
        
        // 查看完文章了，新增阅读数，此处有一个问题
        // 查看文章之后，本应该直接返回数据了，但是这个时候做了一个更新操作【更新阅读数】，更新时需要加写锁，阻塞其他的读操作，性能就会比较低
        // 更新  增加了此次接口的耗时，如果一旦更新出问题，不能影响查看文章的操作
        // 解决技术：  线程池  --->  可以把更新操作扔到线程池中进行执行，那么和主线程就不相关了。
        
        
        threadService.updateArticleViewCount(articleMapper, article);
        return Result.success(articleVo);
        
    }
    
    @Override
    public Result publish(ArticleParam articleParam)
    {
        /**
         * 1. 发布文章： 目的是构建Article 对象
         * 2. 作者id : 即当前的登陆用户
         * 3. 标签： 要将标签加入到关联列表中
         * 4. body: 内容存储
         */
        
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        // 获取作者id
        
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        
        
        // 拿到标签
        // 插入文章之后，会得到一个文章id,有了文章id，才能获取标签id;
        this.articleMapper.insert(article);
        List<TagVo> tags = articleParam.getTags();
        if (tags != null)
        {
            for (TagVo tag : tags)
            {
                Long articleId = article.getId();
                // 加入到关联列表中，需要创建pojo 的ArticleTag
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                this.articleTagMapper.insert(articleTag);
            }
        }
        // body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
//        Map<String,String> map = new HashMap<>();
//        map.put("id",article.getId().toString());
//        return Result.success(map);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        return Result.success(articleVo);
    }
    
    
    // 要做对应的转移，将其转移成为Vo对象
    // 复制到List集合
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor)
    {
        // 此处就简单了，循环即可，因为在下面的函数中已经转换好了。
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records)
        {
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }
    
    // 重载
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory)
    {
        // 此处就简单了，循环即可，因为在下面的函数中已经转换好了。
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records)
        {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }
    
    @Autowired
    private CategoryService categoryService;
    
    
    // 转换操作
    // 这样就把相同的属性copy进来了。
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory)
    {
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        // 使用这个方法可以进行对象之间的属性赋值，避免通过Set、Get方法一个一个属性的赋值
        BeanUtils.copyProperties(article, articleVo);
        
        // 但是有一个问题就是不能copy CreateDate 因为它在Article中是Long类型，而在ArticleVo中是String类型
        // 因此就直接设置
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 但是并不是所有的接口  都是需要标签以及作者信息的
        if (isTag)
        {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor)
        {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody)
        {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory)
        {
            Long categoryId = article.getCategoryId();
            articleVo.setCategorys(categoryService.findCategoryById(categoryId));  //  category是对应表的一个类别 ，因此创建一个新的Service:
            //  因为ArticleBody 是属于文章的服务，但是Category与atricle并不是强绑定 的关系，因此将其单独提出去
        }
        return articleVo;
    }
    
    
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    
    
    private ArticleBodyVo findArticleBodyById(Long bodyId)
    {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        
        return articleBodyVo;
    }
    
}
