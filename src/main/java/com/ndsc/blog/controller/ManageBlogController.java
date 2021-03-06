package com.ndsc.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.ndsc.blog.entity.Blog;
import com.ndsc.blog.entity.Tag;
import com.ndsc.blog.service.ManageBlogService;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ManageBlogController {
    @Autowired
    ManageBlogService manageBlogService;
    @RequestMapping("/solr/addblog")
    public  int save(@RequestBody JSONObject object){
        Map<String, Object> itemMap = JSONObject.toJavaObject(object, Map.class);
        System.out.println(itemMap);
        manageBlogService.addBlog(itemMap);

//        int ret =manageBlogService.addBlog(blog);
//        System.out.println(blog.getBlogId());
        return 1;

    }
    @RequestMapping("/searchUserBlogs")
    public List<Blog> searchUserBlogs(int userId){
        return manageBlogService.searchUserBlog(userId);
    }

    @RequestMapping("/deleteBlog")
    public String deleteBlogByBlogId(Integer blogId){
         manageBlogService.deleteBlogByBlogId(blogId);
         return "<a id=\"ak\" href='blogManage.html'></a>\n" +
                 "<script>\n" +
                 "    document.getElementById(\"ak\").click();\n" +
                 "</script>";
    }

    @RequestMapping("/uploadimage")
    public Map upload(HttpServletRequest request,@RequestParam(name = "file") MultipartFile file) throws IOException{
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        if(file!=null){
            String webapp = "G:/IdeaProjects/blog/blog/src/main/resources/static/upload/";
            try{
                String substring = file.getOriginalFilename();
                String fileName = "/static/upload/" + substring;
                System.out.println(fileName);
                // 图片的在服务器上面的物理路径
                File destFile = new File(webapp, fileName);
                System.out.println(destFile.getAbsolutePath());
                File parentFile = destFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();// 自动生成upload目录
                }

                // 把上传的临时图片，复制到当前项目的webapp路径
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(destFile));
                map = new HashMap<>();
                map2 = new HashMap<>();
                map.put("errno", 0);//0表示成功，1失败
//                map.put("msg", "上传成功");//提示消息
//                map.put("data", map2);
                List<String> list = new ArrayList<>();
                list.add(fileName);
                map.put("data", list);//图片url
//                map2.put("title", substring);//图片名称，这个会显示在输入框里
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return map;
    }

}
