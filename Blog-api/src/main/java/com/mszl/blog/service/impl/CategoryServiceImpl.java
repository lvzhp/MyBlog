package com.mszl.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mszl.blog.dao.mapper.CategoryMapper;
import com.mszl.blog.dao.pojo.Category;
import com.mszl.blog.service.CategoryService;
import com.mszl.blog.vo.CategoryVo;
import com.mszl.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService
{
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Override
    public CategoryVo findCategoryById(Long categoryId)
    {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }
    
    @Override
    public Result findAll()
    {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        
        // 要返回的是页面交互的对象 ,因此需要转换一下
        return Result.success(copyList(categories));
    }
    
    @Override
    public Result findAllDetail()
    {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return Result.success(copyList(categories));
    }
    
    @Override
    public Result categoryDetailById(Long id)
    {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }
    
    public CategoryVo copy(Category category)
    {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }
    public List<CategoryVo> copyList(List<Category> categoryList)
    {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for(Category category :categoryList)
        {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }
    
}
