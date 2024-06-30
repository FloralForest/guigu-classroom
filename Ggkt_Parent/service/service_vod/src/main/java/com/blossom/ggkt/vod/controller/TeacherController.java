package com.blossom.ggkt.vod.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blossom.ggkt.model.vod.Teacher;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vo.vod.TeacherQueryVo;
import com.blossom.ggkt.vod.service.TeacherService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author blossom
 * @since 2023-03-23
 */
@RestController //返回json数据
@RequestMapping(value = "/admin/vod/teacher/")
//已在gateway的config做统一跨域处理
//@CrossOrigin //后端解决跨域
public class TeacherController {

    @Resource
    private TeacherService teacherService;

    //查询所有讲师
    @GetMapping("findAll")
    public Result<List<Teacher>> findAll(){
        //List<Teacher> list = teacherService.list();
        //return list;
        return Result.ok(teacherService.list()).code(20000).message("查询讲师数据成功！");
    }

    //分页条件查询
    @PostMapping("findQueryPage/{current}/{limit}")
    //当前页和每页显示个数 @RequestBody接收json数据 @ResponseBody返回json数据 @PathVariable用于获取URL中路径的参数值
    public Result<IPage<Teacher>> findPage(@PathVariable long current,
                                           @PathVariable long limit,
                                           @RequestBody(required = false) TeacherQueryVo teacherQueryVo){
        //创建page（分页）对象
        Page<Teacher> pageParam = new Page<>(current, limit);
        if (teacherQueryVo == null){//查询全部 没有条件
            IPage<Teacher> pageModel = teacherService.page(pageParam,null);
            return Result.ok(pageModel);
        }else {
            //获取条件值
            String name = teacherQueryVo.getName();
            Integer level = teacherQueryVo.getLevel();
            String joinDateBegin = teacherQueryVo.getJoinDateBegin();
            String joinDateEnd = teacherQueryVo.getJoinDateEnd();
            //非空判断，条件封装
            QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
            if (!StringUtils.isEmpty(name)){
                wrapper.like("name",name);
            }
            if (!StringUtils.isEmpty(level)){
                wrapper.eq("level",level);
            }
            if (!StringUtils.isEmpty(joinDateBegin)){
                wrapper.ge("join_date",joinDateBegin);//ge大于等于
            }
            if (!StringUtils.isEmpty(joinDateEnd)){
                wrapper.le("join_date",joinDateEnd);//le小于等于
            }
            //调用方法得到分页查询结果
            IPage<Teacher> pageModel = teacherService.page(pageParam, wrapper);
            return Result.ok(pageModel);
        }
    }

    //逻辑删除(不删除数据库)
    @DeleteMapping("remove/{id}")
    public Result<Object> removeTeacher(@PathVariable Long id){
        boolean isSuccess = teacherService.removeById(id);
        if (isSuccess)
            return Result.ok(null).code(20000).message("删除成功！");
        else
            return Result.fail(null).code(20001).message("删除失败！");
    }

    //批量删除
    @DeleteMapping("removeBatch")
    public Result<Object> removeBatch(@RequestBody List<Long> idList){
        boolean isSuccess = teacherService.removeByIds(idList);
        if (isSuccess)
            return Result.ok(null).code(20000).message("批量删除成功！");
        else
            return Result.fail(null).code(20001).message("批量删除失败！");
    }

    //添加
    @PostMapping("saveTeacher")
    public Result<Object> saveTeacher(@RequestBody Teacher teacher){
        boolean isSuccess = teacherService.save(teacher);
        if (isSuccess)
            return Result.ok(null).code(20000).message("添加成功！");
        else
            return Result.fail(null).code(20001).message("添加失败！");
    }

    //修改
    @GetMapping("getTeacher/{id}")
    public Result<Teacher> getTeacher(@PathVariable Long id){
        Teacher teacher = teacherService.getById(id);
        return Result.ok(teacher);
    }
    @PostMapping("updateTeacher")
    public Result<Object> updateById(@RequestBody Teacher teacher){
        boolean isSuccess = teacherService.updateById(teacher);
        if (isSuccess)
            return Result.ok(null).code(20000).message("修改成功！");
        else
            return Result.fail(null).code(20001).message("修改失败！");
    }

    //直播获取讲师
    @GetMapping("inner/getTeacher/{id}")
    public Teacher getTeacherLive(@PathVariable Long id) {
        return teacherService.getById(id);
    }
}

