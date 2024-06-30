package com.blossom.ggkt.vod.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.blossom.ggkt.model.vod.Video;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vod.service.VideoService;
import com.blossom.ggkt.vod.service.VodService;
import com.blossom.ggkt.vod.utils.ConstantPropertiesUtil;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

//云点播视频部分
@Slf4j
@Service
public class VodServiceImpl implements VodService {
    @Resource
    private VideoService videoService;

    //上传视频的ID（上传视频）
    @Override
    public String uploadVideo() {
        //指定当前腾讯云账号的id和key
        VodUploadClient client = new VodUploadClient(ConstantPropertiesUtil.ACCESS_KEY_ID,
                ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        //上传请求对象
        VodUploadRequest request = new VodUploadRequest();
        //视频本地地址
        request.setMediaFilePath("C:\\diaolingyun.mp4");
        //指定任务流
        request.setProcedure("LongVideoPreset");
        try {
            //调用上传方法，传入接入点地域及上传请求。
            VodUploadResponse response = client.upload("ap-guangzhou", request);
            //获取上传后的视频id
            return response.getFileId();
        } catch (Exception e) {
            log.info("------{}",String.valueOf(e));
            // 业务方进行异常处理
            return null;
        }
    }

    //删除视频
    @Override
    public void removeVideo(String fileId) {
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            Credential cred = new Credential(ConstantPropertiesUtil.ACCESS_KEY_ID,
                            ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            // 实例化要请求产品的client对象,clientProfile是可选的
            VodClient client = new VodClient(cred, "");
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DeleteMediaRequest req = new DeleteMediaRequest();
            req.setFileId(fileId);
            // 返回的resp是一个DeleteMediaResponse的实例，与请求对象对应
            DeleteMediaResponse resp = client.DeleteMedia(req);
            // 输出json格式的字符串回包
            System.out.println(DeleteMediaResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }

    //公众号视频播放
    @Value("${tencent.video.appid}")
    private String appId;
    @Override
    public Map<String,Object> getPlayAuth(Long courseId, Long videoId) {
        //根据小节id获取小节对象，获取腾讯云视频id
        Video video = videoService.getById(videoId);
        Integer AppId = 1304728012;
        String psgin = "";
        String FileId = video.getVideoSourceId();
        Integer RawAdaptiveDefinition = 10;
        Integer ImageSpriteDefinition = 10;
        Integer CurrentTime =  Math.toIntExact(System.currentTimeMillis() / 1000);//获取当前的unix时间
//        Integer PsignExpire = 1589548067;//签名过期时间
        String PlayKey = "ICDrgd1UOZYW9dVm86tO";
        HashMap<String, Object> contentInfo = new HashMap<String, Object>();
        contentInfo.put("audioVideoType", "RawAdaptive");//视频类型
        contentInfo.put("rawAdaptiveDefinition", RawAdaptiveDefinition);//转码模板id
        contentInfo.put("imageSpriteDefinition", ImageSpriteDefinition);

        Algorithm algorithm = Algorithm.HMAC256(PlayKey);
        String token = JWT.create().withClaim("appId", AppId).withClaim("fileId", FileId)
                .withClaim("contentInfo", contentInfo)
                .withClaim("currentTimeStamp", CurrentTime)
//                .withClaim("expireTimeStamp", PsignExpire)//签名过期时间
                .sign(algorithm);
        System.out.println("token:" + token);
        psgin = token;

        Map<String, Object> map = new HashMap<>();
        map.put("videoSourceId",video.getVideoSourceId());//获取视频id
        map.put("appId",appId);//获取应用id
        map.put("psign",psgin);
        return map;
    }
}
