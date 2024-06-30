package com.blossom.ggkt.live.mapper;

import com.blossom.ggkt.model.live.LiveCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blossom.ggkt.vo.live.LiveCourseVo;

import java.util.List;

/**
 * <p>
 * 直播课程表 Mapper 接口
 * </p>
 *
 * @author blossom
 * @since 2023-07-27
 */
public interface LiveCourseMapper extends BaseMapper<LiveCourse> {

    //获取最近5个直播信息
    List<LiveCourseVo> getLatelyList();
}
