package com.blossom.ggkt.vod.controller;


import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vod.service.VideoVisitorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 前端控制器
 * </p>
 *
 * @author blossom
 * @since 2023-05-13
 */
@RestController
@RequestMapping(value="/admin/vod/videoVisitor")
//已在gateway的config做统一跨域处理
//@CrossOrigin//解决跨域
public class VideoVisitorController {

    @Resource
    private VideoVisitorService videoVisitorService;

    //课程统计  开始时间  结束时间
    @GetMapping("findCount/{courseId}/{startDate}/{endDate}")
    public Result<Map<String, Object>> showChart(@PathVariable Long courseId,
                                                 @PathVariable String startDate,
                                                 @PathVariable String endDate){

        Map<String, Object> map = videoVisitorService.findCount(courseId, startDate, endDate);
        return Result.ok(map);
    }
}

