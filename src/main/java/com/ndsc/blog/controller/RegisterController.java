package com.ndsc.blog.controller;

import com.ndsc.blog.entity.Userinfo;
import com.ndsc.blog.entity.Usersafe;
import com.ndsc.blog.mapper.UserinfoMapper;
import com.ndsc.blog.mapper.UsersafeMapper;
import com.ndsc.blog.service.Md5Encryption;
import com.ndsc.blog.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RegisterController {

    @Resource
    @Autowired
    private RegisterService registerService;
    @Autowired
    Md5Encryption md5Encryption;
    @Autowired
    UsersafeMapper usersafeMapper;
    @Autowired
    UserinfoMapper userinfoMapper;
    @RequestMapping("/register")
    public int register(Usersafe usersafe) {
//        String pwd=usersafe.getPassword();
//        pwd=md5Encryption.encrype(pwd);
//        usersafe.setPassword(pwd);
        usersafe.setPassword(md5Encryption.encrype(usersafe.getPassword()));
        registerService.insertUser(usersafe);
        String userName=usersafe.getUserName();
        int userId = usersafeMapper.selectUserId(userName);
        Userinfo userinfo=new Userinfo();
        userinfo.setUserId(userId);
        userinfoMapper.insert(userinfo);

        System.out.println("zhuce chenggong ");
        return 0;
    }
//@RequestMapping("/insertUser")
//public int insertUser(Usersafe usersafe){
//    usersafe.setPassword(md5Encryption.encrype(usersafe.getPassword()));
//    registerService.insertUser(usersafe);
//    return 0;
//}

    @RequestMapping("/checkSamePhone")
    public int checkSamePhone(String userTel) {
        return registerService.checkSamePhone(userTel);
    }

    @RequestMapping("/checkSameName")
    public int checkSameName(String userName) {
        return registerService.checkSameName(userName);
    }

    @RequestMapping("/checkSameEmail")
    public int checkSameEmail(String userEmail) {
        return registerService.checkSameEmail(userEmail);
    }
}
