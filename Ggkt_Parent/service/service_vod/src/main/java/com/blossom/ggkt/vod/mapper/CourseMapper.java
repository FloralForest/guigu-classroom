package com.blossom.ggkt.vod.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blossom.ggkt.model.vod.Course;
import com.blossom.ggkt.vo.vod.CoursePublishVo;
import com.blossom.ggkt.vo.vod.CourseVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author blossom
 * @since 2023-05-07
 */
public interface CourseMapper extends BaseMapper<Course> {

    //课程发布(根据id获取课程发布信息)
    CoursePublishVo selectCoursePublishVoById(Long id);

    //公众号查询详情
    CourseVo selectCourseVoById(Long courseId);
}
