package com.blossom.ggkt.vod.controller;


import com.blossom.ggkt.model.vod.Video;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vod.service.VideoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 课程视频 前端控制器
 * 小节
 * </p>
 *
 * @author blossom
 * @since 2023-05-07
 */
@RestController
@RequestMapping(value="/admin/vod/video")
//已在gateway的config做统一跨域处理
//@CrossOrigin//解决跨域
public class VideoController {
    @Resource
    private VideoService videoService;

    //2 查询小节
    @PostMapping("save")
    public Result<Object> save(@RequestBody Video video) {
        videoService.save(video);
        return Result.ok(null);
    }

    //3 查询小节
    @GetMapping("get/{id}")
    public Result<Video> get(@PathVariable Long id) {
        Video video = videoService.getById(id);
        return Result.ok(video);
    }

    //4 修改-最终实现
    @PutMapping("update")
    public Result<Object> updateById(@RequestBody Video video) {
        videoService.updateById(video);
        return Result.ok(null);
    }

    //5 删除
    @DeleteMapping("remove/{id}")
    public Result<Object> remove(@PathVariable Long id) {
        videoService.removeVideoById(id);
        return Result.ok(null);
    }
}

