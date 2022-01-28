package com.mszl.blog.controller;


import com.mszl.blog.service.CommentsService;
import com.mszl.blog.vo.Result;
import com.mszl.blog.vo.param.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController
{
    @Autowired
    private CommentsService commentsService;
    // comments/article/{id}
    @GetMapping ("article/{id}")
    public Result comments(@PathVariable("id") Long id)
    {
        return commentsService.commentsByArticleId(id);
    }
    
    
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam)
    {
        return commentsService.comment(commentParam);
    }
    
    
}
