package com.blossom.ggkt.live.service;

import com.blossom.ggkt.model.live.LiveCourseDescription;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-07-27
 */
public interface LiveCourseDescriptionService extends IService<LiveCourseDescription> {

    //获取描述
    LiveCourseDescription getLiveCourseById(Long id);
}
