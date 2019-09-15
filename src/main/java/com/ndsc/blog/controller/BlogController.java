package com.ndsc.blog.controller;

import com.ndsc.blog.entity.*;
import com.ndsc.blog.service.BlogService;
import com.ndsc.blog.service.Md5Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    Md5Encryption md5Encryption;

    @RequestMapping("/getUserinfo")
    public Userinfo getUserinfo(int userId){
        return blogService.selectUserId(userId);
    }

    @RequestMapping("/updateUserinfo")
    public int updateUserinfo(@RequestBody Userinfo userinfo){
        return blogService.updateUserinfo(userinfo);
    }

    @RequestMapping("/addRelation")
    public int addRelation(@RequestBody Relation relation){
        return blogService.insertRelation(relation);
    }

    @RequestMapping("/getRelationUserinfo")
    public List<Userinfo> getRelationUserinfo(int fansId){
        return blogService.selectRelationUser(fansId);
    }

    @RequestMapping("/removeRelation")
    public int removeRelation(@RequestBody Relation relation){
        return blogService.deleteRelation(relation);
    }

    @RequestMapping("/getFans")
    public List<Userinfo> getFans(int blogerId){
        return blogService.selectFans(blogerId);
    }

    @RequestMapping("/updateUsersafe")
    public int updateUsersafe(@RequestBody Usersafe usersafe){
        usersafe.setPassword(md5Encryption.encrype(usersafe.getPassword()));
        return blogService.updateUsersafe(usersafe);
    }

    @RequestMapping("/selectByUserinfoGetBlog")
    public Userinfo selectByUserinfoGetBlog(int userId){
        return blogService.selectByUserinfoGetBlog(userId);
    }

    @RequestMapping("/selectByBlogtagGetBlog")
    public List<Blog> selectByBlogtagGetBlog(String tagName){
        return blogService.selectByBlogtagGetBlog(tagName);
    }

    @RequestMapping("/insertCollection")
    public int insertCollection(@RequestBody Collect collection){
        return blogService.insertCollection(collection);
    }

    @RequestMapping("/selectCollectionBlog")
    public List<Blog> selectCollectionBlog(Integer collectionId){
        return blogService.selectCollectionBlog(collectionId);
    }

    @RequestMapping("/deleteCollection")
    public int deleteCollection(@RequestBody Collect collection){
        return blogService.deleteCollection(collection);
    }

}