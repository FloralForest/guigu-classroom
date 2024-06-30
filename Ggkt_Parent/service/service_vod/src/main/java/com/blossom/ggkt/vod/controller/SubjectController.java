package com.blossom.ggkt.vod.controller;


import com.blossom.ggkt.model.vod.Subject;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vod.service.SubjectService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author blossom
 * @since 2023-05-04
 */
@RestController
@RequestMapping(value="/admin/vod/subject")
//已在gateway的config做统一跨域处理
//@CrossOrigin //解决跨域
public class SubjectController {
    @Resource
    private SubjectService subjectService;

    //课程列表（懒加载=》每次只查询一层数据）
    @GetMapping("getChildSubject/{id}")
    public Result<List<Subject>> getChildSubject(@PathVariable Long id){
        List<Subject> list = subjectService.selectList(id);
        return Result.ok(list);
    }

    //导出
    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response) {
        subjectService.exportData(response);
    }

    //导入
    @PostMapping("importData")
    public Result<Object> importData(MultipartFile file) {
        subjectService.importDictData(file);
        return Result.ok(null);
    }
}

