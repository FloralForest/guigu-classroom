package com.blossom.ggkt.user.controller;

import com.alibaba.fastjson.JSON;
import com.blossom.ggkt.model.user.UserInfo;
import com.blossom.ggkt.user.service.UserInfoService;
import com.blossom.ggkt.utils.JwtHelper;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

//微信授权登录 为了做页面跳转所以使用的是@Controller
@Controller
@RequestMapping("/api/user/wechat")
public class WechatController {
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private WxMpService wxMpService;

    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    //授权跳转
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl, HttpServletRequest request) {

        //授权后跳转的路径以及微信官方文档指定的范围
        String url = wxMpService.oauth2buildAuthorizationUrl(userInfoUrl,
                WxConsts.OAUTH2_SCOPE_USER_INFO,
                URLEncoder.encode(returnUrl.replace("guiguketang", "#")));
        return "redirect:" + url;
    }

    //获取code
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl){

        try {
            //拿着code请求
            WxMpOAuth2AccessToken wxToken = wxMpService.oauth2getAccessToken(code);
            //获取openId
            String openId = wxToken.getOpenId();
            System.out.println("【微信网页授权】openId={}"+openId);
            //获取微信信息
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxToken, null);
            System.out.println("【微信网页授权】wxMpUser={}"+ JSON.toJSONString(wxMpUser));
            //获取微信信息添加到数据库
            //判断是否第一次授权--查询数据库
            UserInfo userInfo = userInfoService.getUserInfoOpenid(openId);
            if (userInfo == null){ //第一次添加
                userInfo = new UserInfo();
                userInfo.setOpenId(openId);
                userInfo.setUnionId(wxMpUser.getUnionId());
                userInfo.setNickName(wxMpUser.getNickname());
                userInfo.setAvatar(wxMpUser.getHeadImgUrl());
                userInfo.setSex(wxMpUser.getSexId());
                userInfo.setProvince(wxMpUser.getProvince());

                userInfoService.save(userInfo);
            }
            //用户id和名称生成token
            String token = JwtHelper.createToken(userInfo.getId(),userInfo.getNickName());

            //检索url中有无“？”
            if(!returnUrl.contains("?")) {
                return "redirect:" + returnUrl + "?token=" + token;
            } else {
                return "redirect:" + returnUrl + "&token=" + token;
            }
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
}
