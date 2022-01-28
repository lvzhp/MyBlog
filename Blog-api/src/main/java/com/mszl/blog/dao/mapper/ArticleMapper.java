package com.mszl.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mszl.blog.dao.dos.Archives;
import com.mszl.blog.dao.pojo.Article;

import java.util.List;

/*mybatis-plus提供的，可以更方便查询mapper这张表*/
/*此时Article就和数据库表ms_*/

public interface ArticleMapper extends BaseMapper<Article>
{
    
    List<Archives> listArchives();
    
    
    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);
    
    
}
