package com.mszl.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mszl.blog.dao.pojo.Tag;

import java.util.List;

public interface TagMapper extends  BaseMapper<Tag>
{
    // 根据文章id查询标签列表
    List<Tag> findTagsByArticleID(Long articleId);
    
    /**
     * 查询最热的标签
     * @param limit
     * @return
     */
    List<Long> findHotsTagIds(int limit);
    
    List<Tag> findTagsByTagIds(List<Long> tagIds);
    
}
