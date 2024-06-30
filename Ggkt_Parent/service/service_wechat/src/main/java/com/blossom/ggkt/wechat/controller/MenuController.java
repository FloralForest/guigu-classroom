package com.blossom.ggkt.wechat.controller;


import com.alibaba.fastjson.JSONObject;
import com.blossom.ggkt.model.wechat.Menu;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vo.wechat.MenuVo;
import com.blossom.ggkt.wechat.service.MenuService;
import com.blossom.ggkt.wechat.utlis.ConstantPropertiesUtil;
import com.blossom.ggkt.wechat.utlis.HttpClientUtils;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 微信订单明细 订单明细 前端控制器
 * </p>
 *
 * @author blossom
 * @since 2023-07-20
 */
@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    //获取所有菜单按照一级二级封装
    @GetMapping("findMenuInfo")
    public Result<List<MenuVo>> findMenuInfo(){
        List<MenuVo> list = menuService.findMenuInfo();
        return Result.ok(list);
    }

    //获取一级菜单
    @GetMapping("findOneMenuInfo")
    public Result<List<Menu>> findOneMenuInfo(){
        List<Menu> list = menuService.findMenuOneInfo();
        return Result.ok(list);
    }

    //增删改查
    @GetMapping("get/{id}")
    public Result<Menu> get(@PathVariable Long id){
        return Result.ok(menuService.getById(id));
    }

    @PostMapping("save")
    public Result<Object> save(@RequestBody Menu menu){
        menuService.save(menu);
        return Result.ok(null);
    }

    @PutMapping("update")
    public Result<Object> updateById(@RequestBody Menu menu){
        menuService.updateById(menu);
        return Result.ok(null);
    }

    @DeleteMapping("remove/{id}")
    public Result<Object> remove(@PathVariable Long id){
        menuService.removeById(id);
        return Result.ok(null);
    }

    @DeleteMapping("batchRemove")
    public Result<Object> batchRemove(@RequestBody List<Long> idList) {
        menuService.removeByIds(idList);
        return Result.ok(null);
    }

    //公众号菜单删除
    @DeleteMapping("removeMenu")
    public Result<Object> removeMenu() {
        menuService.removeMenu();
        return Result.ok(null);
    }

    //同步菜单方法
    @GetMapping("syncMenu")
    public Result<Object> createMenu(){
        menuService.syncMenu();
        return Result.ok(null);
    }

// 在this.wxMpService.getMenuService().menuCreate(button.toJSONString());中已实现以下可以省略
//    //获取access_token
//    @GetMapping("getAccessToken")
//    public Result<String> getAccessToken() {
//        //拼接请求地址
//        StringBuffer buffer = new StringBuffer();
//        buffer.append("https://api.weixin.qq.com/cgi-bin/token");
//        buffer.append("?grant_type=client_credential");
//        buffer.append("&appid=%s");
//        buffer.append("&secret=%s");
//        //设置路径参数
//        String url = String.format(buffer.toString(),
//                ConstantPropertiesUtil.ACCESS_KEY_ID,
//                ConstantPropertiesUtil.ACCESS_KEY_SECRET);
//        //发送http请求
//        try {
//            String tokenString = HttpClientUtils.get(url);
//            //获取access_token----JSONObject解析字符串变成json
//            JSONObject jsonObject = JSONObject.parseObject(tokenString);
//            String access_token = jsonObject.getString("access_token");
//            return Result.ok(access_token);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}

