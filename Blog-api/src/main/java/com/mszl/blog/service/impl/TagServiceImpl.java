package com.mszl.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mszl.blog.dao.mapper.TagMapper;
import com.mszl.blog.dao.pojo.Tag;
import com.mszl.blog.service.TagService;
import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service

public class TagServiceImpl implements TagService
{
    @Autowired
    private TagMapper tagMapper;
    
    
    public TagVo copy(Tag tag1)
    {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag1,tagVo);
        tagVo.setId(String.valueOf(tag1.getId()));
        return tagVo;
    }
    public List<TagVo> copyList(List<Tag> tag1List)
    {
        List<TagVo> tagVoList = new ArrayList<>();
        for(Tag tag1:tag1List)
        {
            tagVoList.add(copy(tag1));
        }
        return tagVoList;
    }
    
    @Override
    public List<TagVo> findTagsByArticleId(Long articleId)
    {
        // mybatis-plus是无法进行多表查询的。因此自己实现
        List<Tag> tags1= tagMapper.findTagsByArticleID(articleId);
        return copyList(tags1);
    }
    
    @Override
    public Result hots(int limit)
    {
        /**
         * 最热标签就是这个标签下文章数量最多 的，因此需要对这个标签进行groupBy
         * 根据tag_id  分组计数，从小到大排列，取前limit个
         */
        List<Long> tagIds=tagMapper.findHotsTagIds(limit);
        /*但是有可能为空，因此给其一个判断*/
        if(CollectionUtils.isEmpty(tagIds))
        {
            return Result.success(Collections.emptyList());
        }
        // 最终需求的是tagID 和tagName
        List<Tag> tagList= tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }
    
    /**
     * 查询文章的所有标签
     * @return
     */
    @Override
    public Result findAll()
    {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        //同样的要返回与页面交互的Vo对象，因此此处还是需要转换一下
        /**
         * Tag  TagVo
         * Vo目录下的是与页面进行交互的，一般数据库的对象【例如Tag 】与页面上要的数据不一定一样，因此都需要再次封装，因此需要转换
         */
        return Result.success(copyList(tags));
    }
    
    @Override
    public Result findAllDetail()
    {
        List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<>());
        //同样的要返回与页面交互的Vo对象，因此此处还是需要转换一下
        /**
         * Tag  TagVo
         * Vo目录下的是与页面进行交互的，一般数据库的对象【例如Tag 】与页面上要的数据不一定一样，因此都需要再次封装，因此需要转换
         */
        return Result.success(copyList(tags));
        
    }
    
    @Override
    public Result findDetailById(Long id)
    {
        Tag tag = tagMapper.selectById(id);
        return Result.success(copy(tag));
    }
}
