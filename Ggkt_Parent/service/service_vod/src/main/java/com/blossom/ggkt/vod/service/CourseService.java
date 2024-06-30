package com.blossom.ggkt.vod.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blossom.ggkt.model.vod.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blossom.ggkt.vo.vod.CourseFormVo;
import com.blossom.ggkt.vo.vod.CoursePublishVo;
import com.blossom.ggkt.vo.vod.CourseQueryVo;
import com.blossom.ggkt.vo.vod.CourseVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-05-07
 */
public interface CourseService extends IService<Course> {

    //视频点播查询
    Map<String, Object> findPageCourse(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    //新增课程
    Long saveCourseInfo(CourseFormVo courseFormVo);

    //修改(获取id)
    CourseFormVo getCourseFormVoById(Long id);
    //修改
    void updateCourseById(CourseFormVo courseFormVo);

    //课程发布(根据id获取课程发布信息)
    CoursePublishVo getCoursePublishVo(Long id);
    //课程发布(根据id发布课程)
    boolean publishCourseById(Long id);

    //删除
    void removeCourseById(Long id);

    //公众号根据课程分类查询相关课程信息
    Map<String,Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    //公众号根据id查询课程详细信息
    Map<String, Object> getInfoById(Long courseId);

    //直播查询所有课程
    List<Course> findlist();
}

