package com.blossom.ggkt.vod.api;

import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vod.service.VodService;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

//公众号视频播放
@RestController
@RequestMapping("/api/vod")
public class VodApiController {
    @Resource
    private VodService vodService;

    @GetMapping("getPlayAuth/{courseId}/{videoId}")
    public Result<Map<String, Object>> getPlayAuth(
            @ApiParam(value = "课程id", required = true)
            @PathVariable Long courseId,
            @ApiParam(value = "视频id", required = true)
            @PathVariable Long videoId) {
        return  Result.ok(vodService.getPlayAuth(courseId, videoId));
    }
}
