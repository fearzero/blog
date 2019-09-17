package com.ndsc.blog.service;

import com.ndsc.blog.entity.Resources;

import java.util.List;

/**
 * @Author 扶明方
 * @Date 2019/9/12 21:08
 * @Version 1.0
 */
public interface ResourcesService {
    int uploadResource(Resources resources);

    List<Resources> getAllResources();
}
