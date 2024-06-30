package com.blossom.ggkt.vod.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blossom.ggkt.model.vod.Course;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vo.vod.CourseQueryVo;
import com.blossom.ggkt.vo.vod.CourseVo;
import com.blossom.ggkt.vod.service.CourseService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

//公众号消息业务实现
@RestController
@RequestMapping("/api/vod/course")
public class CourseApiController {
    @Resource
    private CourseService courseService;

    //根据关键字查询课程
    @GetMapping("inner/findByKeyword/{keyword}")
    public List<Course> findByKeyword(
            @ApiParam(value = "关键字", required = true)
            @PathVariable String keyword){
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Course::getTitle,keyword);
        return courseService.list(wrapper);
    }

    //根据课程分类查询相关课程信息
    @GetMapping("{subjectParentId}/{page}/{limit}")
    public Result<Map<String, Object>> findPageCourse(@ApiParam(value = "课程一级分类ID", required = true) @PathVariable Long subjectParentId,
                                 @ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
                                 @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit) {

        //封装条件
        CourseQueryVo courseQueryVo = new CourseQueryVo();
        courseQueryVo.setSubjectParentId(subjectParentId);
        //创建page对象
        Page<Course> pageParam = new Page<>(page,limit);
        Map<String,Object> map = courseService.findPage(pageParam,courseQueryVo);
        return Result.ok(map);
    }

    //根据id查询课程详细信息
    @GetMapping("getInfo/{courseId}")
    public Result<Map<String, Object>> getInfo(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable Long courseId){
        Map<String,Object> map = courseService.getInfoById(courseId);
        return Result.ok(map);
    }

    //生成订单order模块远程调用该接口
    @GetMapping("inner/getById/{courseId}")
    public Course getById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable Long courseId){
        return courseService.getById(courseId);
    }
}
