package com.blossom.ggkt.vod.controller;

import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vod.service.VodService;
import com.blossom.ggkt.vod.utils.ConstantPropertiesUtil;
import com.blossom.ggkt.vod.utils.Signature;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Random;

//云点播视频部分
@RestController
@RequestMapping("/admin/vod")
//已在gateway的config做统一跨域处理
//@CrossOrigin//解决跨域
public class VodController {
    @Resource
    private VodService vodService;

    @PostMapping("upload")
    public Result<String> uploadVideo(){

        //返回上传视频的ID
        String fileId = vodService.uploadVideo();
        return Result.ok(fileId);
    }

    //删除视频
    @DeleteMapping("remove/{fileId}")
    public Result<Object> removeVideo( @PathVariable String fileId) {
        vodService.removeVideo(fileId);
        return Result.ok(null);
    }

    //返回客户端上传视频需要的签名
    @GetMapping("sign")
    public Result<String> sign(){
        Signature sign = new Signature();
        // 设置 App 的云 API 密钥
        sign.setSecretId(ConstantPropertiesUtil.ACCESS_KEY_ID);
        sign.setSecretKey(ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 24 * 2); // 签名有效期：2天

        try {
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);
            return Result.ok(signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
            return Result.fail("获取签名失败");
        }
    }
}
