package com.blossom.ggkt.vod.controller;


import com.blossom.ggkt.model.vod.Chapter;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vo.vod.ChapterVo;
import com.blossom.ggkt.vod.service.ChapterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * 章节
 * </p>
 *
 * @author blossom
 * @since 2023-05-07
 */
@RestController
@RequestMapping(value="/admin/vod/chapter")
//已在gateway的config做统一跨域处理
//@CrossOrigin//解决跨域
public class ChapterController {

    @Resource
    private ChapterService chapterService;

    //大纲列表
    @GetMapping("getNestedTreeList/{courseId}")
    public Result<List<ChapterVo>> getNestedTreeList(@PathVariable Long courseId){
        List<ChapterVo> chapterVoList = chapterService.getNestedTreeList(courseId);
        return Result.ok(chapterVoList);
    }

    //2 添加章节
    @PostMapping("save")
    public Result<Object> save(@RequestBody Chapter chapter) {
        chapterService.save(chapter);
        return Result.ok(null);
    }

    //3 修改-根据id查询
    @GetMapping("get/{id}")
    public Result<Chapter> get(@PathVariable Long id) {
        Chapter chapter = chapterService.getById(id);
        return Result.ok(chapter);
    }

    //4 修改-最终实现
    @PutMapping("update")
    public Result<Object> update(@RequestBody Chapter chapter) {
        chapterService.updateById(chapter);
        return Result.ok(null);
    }

    //5 删除章节
    @DeleteMapping("remove/{id}")
    public Result<Object> remove(@PathVariable Long id) {
        chapterService.removeById(id);
        return Result.ok(null);
    }
}

