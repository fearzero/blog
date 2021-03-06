package com.ndsc.blog.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;

    // 这个方法是用来配置静态资源的，比如html，js，css，等等

    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns("/**") 表示拦截所有的请求，
        // excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
        InterceptorRegistration loginRegistry = registry.addInterceptor(loginInterceptor).addPathPatterns("/p");
//        loginRegistry.excludePathPatterns("/login.html","/login","/getLoginUserName","/solr/search");
//        loginRegistry.excludePathPatterns("/blogIndex.html");
//        loginRegistry.excludePathPatterns("/paySuccess.html","/insertUser","/checkSamePhone","/checkSameName","/getCheckCode","/checkSameEmail");
//        loginRegistry.excludePathPatterns("/login.html", "/login", "/index.html", "/getLoginUserName");
//        loginRegistry.excludePathPatterns("/JDregister.html", "/insertUser", "/checkSamePhone", "/checkSameName", "/getCheckCode", "/checkSameEmail");
//        loginRegistry.excludePathPatterns("/css/*");
//        loginRegistry.excludePathPatterns("/js/*");
//        loginRegistry.excludePathPatterns("/images/*");
//        loginRegistry.excludePathPatterns("/images/blogIndex/*");
//        loginRegistry.excludePathPatterns("/pay","/return_url");
//        loginRegistry.excludePathPatterns("/images/JDregister/*");
//        loginRegistry.excludePathPatterns("/css/cart/*");
//        loginRegistry.excludePathPatterns("/cart.html");
//        loginRegistry.excludePathPatterns("/pay", "/return_url", "/uploadResource", "/uploadSuccess.html", "/paySuccess.html");
    }
}