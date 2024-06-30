package com.blossom.ggkt.vod.controller;


import com.blossom.ggkt.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 登录 前端控制器
 * </p>
 *
 * @author blossom
 * @since 2023-03-23
 */
@RestController //返回json数据
@RequestMapping(value = "/admin/vod/user/")
//已在gateway的config做统一跨域处理
//@CrossOrigin //解决跨域
public class UserLoginController {
    //登录
    @PostMapping("login")
    public Result<Map<String, Object>> login(){
        Map<String, Object> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);
    }

    //获取用户信息
    @GetMapping("info")
    public Result<Map<String, Object>> info(){
        Map<String, Object> map = new HashMap<>();
        map.put("roles","admin");
        map.put("introduction","我是一名超级管理员");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Super Admin");
        return Result.ok(map);
    }

    //退出
    @PostMapping("logout")
    public Result<String> logout(){
        return Result.ok("退出成功！");
    }

}

