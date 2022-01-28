package com.mszl.blog.controller;


import com.mszl.blog.utils.QiniuUtils;
import com.mszl.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController
{
    @Autowired
    private QiniuUtils qiniuUtils;
    
    
    @PostMapping
    // MultipartFile 是Spring中专门用来接受文件的类型
    public Result upload(@RequestParam("image")MultipartFile file)
    {
        // 拿到原始名称，比如pic.png
        String originalFilename = file.getOriginalFilename();
        // 但是上传文件名称的时候不能上传文件的原始名称，保证唯一的文件名称
        String fileName=UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename,".");
        // 上传文件到七牛云
        boolean uploaded = qiniuUtils.upload(file,fileName);
        if(uploaded)
        {
            return Result.success(qiniuUtils.url+fileName);
        }
        return Result.fail(20001,"上传失败");
        
        
        
        
    }
}
