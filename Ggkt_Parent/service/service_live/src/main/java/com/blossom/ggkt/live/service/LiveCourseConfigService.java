package com.blossom.ggkt.live.service;

import com.blossom.ggkt.model.live.LiveCourseConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 直播课程配置表 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-07-27
 */
public interface LiveCourseConfigService extends IService<LiveCourseConfig> {

    //根据id查询配置信息
    LiveCourseConfig getCourseConfigCourseId(Long id);
}
