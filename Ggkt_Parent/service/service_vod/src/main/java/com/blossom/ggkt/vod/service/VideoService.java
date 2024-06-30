package com.blossom.ggkt.vod.service;

import com.blossom.ggkt.model.vod.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-05-07
 */
public interface VideoService extends IService<Video> {

    //根据课程id删除小节并且删除小节的视频
    void removeVideoByCourseId(Long id);

    //删除小节时删除视频
    void removeVideoById(Long id);
}
