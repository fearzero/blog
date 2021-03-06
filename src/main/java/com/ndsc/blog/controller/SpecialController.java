package com.ndsc.blog.controller;

import com.ndsc.blog.entity.Collect;
import com.ndsc.blog.entity.Relation;
import com.ndsc.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class SpecialController {
    @Autowired
    BlogService blogService;
    @RequestMapping("/removeRelation1")
    public String  removeRelation1(Relation relation){
        int ret= blogService.deleteRelation(relation);
        return "viewfollow";
    }
    @RequestMapping("/deleteCollection1")
    public String deleteCollection1( Collect collection){
                blogService.deleteCollection(collection);
                return "viewcollection";
    }
}
