package com.blossom.ggkt.wechat.controller;

import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.utils.AuthContextHolder;
import com.blossom.ggkt.utils.Base64Util;
import com.blossom.ggkt.vo.wechat.WxJsapiSignatureVo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

//微信分享
@RestController
@RequestMapping("/api/wechat/share")
@Slf4j
public class ShareController {

    @Resource
    private WxMpService wxMpService;

    @GetMapping("/getSignature")
    public Result<WxJsapiSignatureVo> getSignature(@RequestParam("url") String url) throws WxErrorException {
        String currentUrl = url.replace("guiguketang", "#");
        WxJsapiSignature jsapiSignature = wxMpService.createJsapiSignature(currentUrl);

        WxJsapiSignatureVo wxJsapiSignatureVo = new WxJsapiSignatureVo();
        BeanUtils.copyProperties(jsapiSignature, wxJsapiSignatureVo);
        wxJsapiSignatureVo.setUserEedId(Base64Util.base64Encode(AuthContextHolder.getUserId()+""));
        return Result.ok(wxJsapiSignatureVo);
    }

}
