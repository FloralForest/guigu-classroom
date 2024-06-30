package com.blossom.ggkt.vod.service;

import java.util.Map;

//云点播视频部分
public interface VodService {

    //上传视频的ID
    String uploadVideo();

    //删除视频
    void removeVideo(String fileId);

    //公众号视频播放
    Map<String,Object> getPlayAuth(Long courseId, Long videoId);
}
