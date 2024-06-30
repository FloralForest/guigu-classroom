package com.blossom.ggkt.live.api;

import com.alibaba.fastjson.JSONObject;
import com.blossom.ggkt.live.service.LiveCourseService;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.utils.AuthContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//公众号观看直播
@RestController
@RequestMapping("api/live/liveCourse")
public class LiveCourseApiController {

    @Resource
    private LiveCourseService liveCourseService;

    //获取用户access_token
    @GetMapping("getPlayAuth/{id}")
    public Result<JSONObject> getPlayAuth(@PathVariable Long id,
                                          HttpServletRequest request) {
        JSONObject object = liveCourseService.getAccessToken(id, AuthContextHolder.getUserId());
        return Result.ok(object);
    }

    //根据id得到直播详情信息
    @GetMapping("getInfo/{courseId}")
    public Result<Map<String, Object>> getInfo(
            @PathVariable Long courseId){
        Map<String, Object> map = liveCourseService.getInfoById(courseId);
        return Result.ok(map);
    }
}
