package com.blossom.ggkt.vod.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blossom.ggkt.model.vod.Course;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vo.vod.CourseFormVo;
import com.blossom.ggkt.vo.vod.CoursePublishVo;
import com.blossom.ggkt.vo.vod.CourseQueryVo;
import com.blossom.ggkt.vod.service.CourseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author blossom
 * @since 2023-05-07
 */
@RestController
@RequestMapping(value = "/admin/vod/course")
//已在gateway的config做统一跨域处理
//@CrossOrigin//解决跨域
public class CourseController {

    @Resource
    private CourseService courseService;

    //视频点播查询
    //当前页，每页记录数，查询对象
    @GetMapping("{page}/{limit}")
    public Result
            <Map<String, Object>> courseList(@PathVariable Long page,
                                             @PathVariable Long limit,
                                             CourseQueryVo courseQueryVo) {
        Page<Course> pageParam = new Page<>(page, limit);
        Map<String, Object> map = courseService.findPageCourse(pageParam, courseQueryVo);
        return Result.ok(map);
    }

    //新增课程@RequestBody(接收json数据)
    @PostMapping("save")
    public Result<Long> save(@RequestBody CourseFormVo courseFormVo){
        Long courseId = courseService.saveCourseInfo(courseFormVo);
        return Result.ok(courseId);
    }

    //修改（获取id）@PathVariable用于获取URL中路径的参数值
    @GetMapping("get/{id}")
    public Result<CourseFormVo> get(@PathVariable Long id) {
        CourseFormVo course = courseService.getCourseFormVoById(id);//使用的原因CourseFormVo里包含所有信息
        return Result.ok(course);
    }

    //修改
    @PutMapping("update")
    public Result<Object> updateById(@RequestBody CourseFormVo courseFormVo) {
        courseService.updateCourseById(courseFormVo);
        //返回课程id
        return Result.ok(courseFormVo.getId());
    }

    //课程发布(根据id获取课程发布信息)
    @GetMapping("getCoursePublishVo/{id}")
    public Result<CoursePublishVo> getCoursePublishVoById(@PathVariable Long id){
        CoursePublishVo coursePublishVo =  courseService.getCoursePublishVo(id);
        return Result.ok(coursePublishVo);
    }
    //课程发布(根据id发布课程)
    @PutMapping("publishCourse/{id}")
    public Result<Object> publishCourseById(@PathVariable Long id){
        boolean result = courseService.publishCourseById(id);
        return Result.ok(result);
    }

    //删除
    @DeleteMapping("remove/{id}")
    public Result<Object> remove(@PathVariable Long id) {
        courseService.removeCourseById(id);
        return Result.ok(null);
    }

    //直播查询所有课程
    @GetMapping("findAll")
    public Result<List<Course>> findAll() {
        List<Course> list = courseService.findlist();
        return Result.ok(list);
    }
}

