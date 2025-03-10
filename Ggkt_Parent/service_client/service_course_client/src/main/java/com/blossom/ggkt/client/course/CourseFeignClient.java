package com.blossom.ggkt.client.course;

import com.blossom.ggkt.model.vod.Course;
import com.blossom.ggkt.model.vod.Teacher;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//公众号关键字查询课程的远程调用
@FeignClient(value = "service-vod")
public interface CourseFeignClient {

    @ApiOperation("根据关键字查询课程")
    @GetMapping("/api/vod/course/inner/findByKeyword/{keyword}")
    List<Course> findByKeyword(@PathVariable String keyword);

    @ApiOperation("根据ID查询课程")
    @GetMapping("/api/vod/course/inner/getById/{courseId}")
    Course getById(@PathVariable Long courseId);

    //直播获取讲师
    @GetMapping("/admin/vod/teacher/inner/getTeacher/{id}")
    Teacher getTeacherLive(@PathVariable Long id);
}
