package com.mszl.blog.controller;

import com.mszl.blog.service.TagService;
import com.mszl.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // RestController 代表返回的是Json数据
@RequestMapping("tags")
public class TagsController
{
    @Autowired
    private TagService tagService;
    
    
    // 路径就是： /tags/hot
    @GetMapping("hot")
    public Result hot()
    {
        // 查询最热的6个标签
        int limit=6;
        return tagService.hots(limit);
    }
    
    
    @GetMapping
    public Result findAll()
    {
        return tagService.findAll();
    }
    
    @GetMapping("detail")
    public Result findAllDetail()
    {
        return tagService.findAllDetail();
    }
    
    
    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id )
    {
        return tagService.findDetailById(id);
    }
}
