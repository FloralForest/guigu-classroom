package com.blossom.ggkt.user.controller;


import com.blossom.ggkt.model.user.UserInfo;
import com.blossom.ggkt.user.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author blossom
 * @since 2023-07-19
 */
@RestController
@RequestMapping("/admin/user/userInfo")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    //根据id查询 返回对象方便下一步取值
    @GetMapping("inner/getById/{id}")
    public UserInfo getById(@PathVariable Long id){
        return userInfoService.getById(id);
    }
}

