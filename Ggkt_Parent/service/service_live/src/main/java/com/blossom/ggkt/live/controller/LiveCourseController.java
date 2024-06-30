package com.blossom.ggkt.live.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blossom.ggkt.live.service.LiveCourseAccountService;
import com.blossom.ggkt.live.service.LiveCourseService;
import com.blossom.ggkt.model.live.LiveCourse;
import com.blossom.ggkt.model.live.LiveCourseAccount;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vo.live.LiveCourseConfigVo;
import com.blossom.ggkt.vo.live.LiveCourseFormVo;
import com.blossom.ggkt.vo.live.LiveCourseVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 直播课程表 前端控制器
 * </p>
 *
 * @author blossom
 * @since 2023-07-27
 */
@RestController
@RequestMapping(value="/admin/live/liveCourse")
public class LiveCourseController {

    @Resource
    private LiveCourseService liveCourseService;

    @Resource
    private LiveCourseAccountService liveCourseAccountService;

    //分页
    @GetMapping("{page}/{limit}")
    public Result<IPage<LiveCourse>> list(@PathVariable Long page,
                       @PathVariable Long limit){
        Page<LiveCourse> pageParam = new Page<>(page, limit);
        IPage<LiveCourse> pageModel = liveCourseService.selectPage(pageParam);
        return Result.ok(pageModel);
    }

    //增删改查
    @PostMapping("save")
    public Result<Object> save(@RequestBody LiveCourseFormVo liveCourseVo) {
        liveCourseService.save(liveCourseVo);
        return Result.ok(null);
    }

    @DeleteMapping("remove/{id}")
    public Result<Object> remove(@PathVariable Long id) {
        liveCourseService.removeLive(id);
        return Result.ok(null);
    }

    //查询课程和描述然后修改
    @GetMapping("get/{id}")
    public Result<LiveCourse> get(@PathVariable Long id) {
        LiveCourse liveCourse = liveCourseService.getById(id);
        return Result.ok(liveCourse);
    }

    @GetMapping("getInfo/{id}")
    public Result<LiveCourseFormVo> getInfo(@PathVariable Long id) {
        return Result.ok(liveCourseService.getLiveCourseFormVo(id));
    }

    @PutMapping("update")
    public Result<Object> updateById(@RequestBody LiveCourseFormVo liveCourseVo) {
        liveCourseService.updateLiveById(liveCourseVo);
        return Result.ok(null);
    }

    //查看账号信息
    @GetMapping("getLiveCourseAccount/{id}")
    public Result<LiveCourseAccount> getLiveCourseAccount(@PathVariable Long id) {
        return Result.ok(liveCourseAccountService.getByLiveCourseId(id));
    }
    //获取直播配置信息
    @GetMapping("getCourseConfig/{id}")
    public Result<LiveCourseConfigVo> getCourseConfig(@PathVariable Long id) {
        return Result.ok(liveCourseService.getCourseConfig(id));
    }

    //修改直播配置
    @PutMapping("updateConfig")
    public Result<Object> updateConfig(@RequestBody LiveCourseConfigVo liveCourseConfigVo) {
        liveCourseService.updateConfig(liveCourseConfigVo);
        return Result.ok(null);
    }

    //获取最近5个直播信息
    @GetMapping("findLatelyList")
    public Result<List<LiveCourseVo>> findLatelyList() {
        return Result.ok(liveCourseService.getLatelyList());
    }
}

