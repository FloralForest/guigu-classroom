package com.blossom.ggkt.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blossom.ggkt.model.live.LiveCourseDescription;
import com.blossom.ggkt.live.mapper.LiveCourseDescriptionMapper;
import com.blossom.ggkt.live.service.LiveCourseDescriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author blossom
 * @since 2023-07-27
 */
@Service
public class LiveCourseDescriptionServiceImpl extends ServiceImpl<LiveCourseDescriptionMapper, LiveCourseDescription> implements LiveCourseDescriptionService {

    //获取描述
    @Override
    public LiveCourseDescription getLiveCourseById(Long id) {
        final LambdaQueryWrapper<LiveCourseDescription> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveCourseDescription::getLiveCourseId,id);
        return baseMapper.selectOne(wrapper);
    }
}
