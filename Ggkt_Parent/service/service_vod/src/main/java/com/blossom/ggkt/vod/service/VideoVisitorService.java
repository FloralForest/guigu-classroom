package com.blossom.ggkt.vod.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blossom.ggkt.model.vod.VideoVisitor;

import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-05-13
 */
public interface VideoVisitorService extends IService<VideoVisitor> {

    //课程统计
    Map<String, Object> findCount(Long courseId, String startDate, String endDate);
}
