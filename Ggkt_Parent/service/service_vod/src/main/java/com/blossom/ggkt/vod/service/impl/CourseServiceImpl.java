package com.blossom.ggkt.vod.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blossom.ggkt.model.vod.Course;
import com.blossom.ggkt.model.vod.CourseDescription;
import com.blossom.ggkt.model.vod.Subject;
import com.blossom.ggkt.model.vod.Teacher;
import com.blossom.ggkt.vo.vod.*;
import com.blossom.ggkt.vod.mapper.CourseMapper;
import com.blossom.ggkt.vod.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author blossom
 * @since 2023-05-07
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Resource
    private TeacherService teacherService;//讲师

    @Resource
    private SubjectService subjectService;//课程

    @Resource
    private CourseDescriptionService descriptionService;//描述

    @Resource
    private ChapterService chapterService;//章节

    @Resource
    private VideoService videoService;//小节

    //视频点播查询
    @Override
    public Map<String, Object> findPageCourse(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        //获取条件值
        String title = courseQueryVo.getTitle();
        Long subjectParentId = courseQueryVo.getSubjectParentId();//一级分类
        Long subjectId = courseQueryVo.getSubjectId();//二级分类
        Long teacherId = courseQueryVo.getTeacherId();

        //封装条件
        //使用QueryWrapper时需要写数据库字段例如：wrapper.eq("parent_id",id);
        LambdaQueryWrapper<Course> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(title)) {
            lambdaQueryWrapper.like(Course::getTitle, title);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            lambdaQueryWrapper.eq(Course::getSubjectId, subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            lambdaQueryWrapper.eq(Course::getSubjectParentId, subjectParentId);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            lambdaQueryWrapper.eq(Course::getTeacherId, teacherId);
        }
        //调用方法查询(进行条件查询带分页)
        Page<Course> page = baseMapper.selectPage(pageParam, lambdaQueryWrapper);
        long total = page.getTotal();//总记录数
        long totalPage = page.getPages();//总页数
        long currentPage = page.getCurrent();//当前页
        long size = page.getSize();//每页记录数
        List<Course> records = page.getRecords();//每页数据集合
        //遍历封装讲师和分类名称(根据id查询名称最终显示)
//        records.stream().forEach(flor ->{
//            this.getNameById(flor);
//        });
        records.forEach(this::getNameById);

        //封装返回数据
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", total);
        map.put("totalPage", totalPage);
        map.put("records", records);
        return map;
    }

    ////获取讲师和分类名称
    private void getNameById(Course flor) {
        //查询讲师名称
        Teacher teacher = teacherService.getById(flor.getTeacherId());
        if (teacher != null) {
            flor.getParam().put("teacherName", teacher.getName());
        }

        //查询分类名称
        Subject subjectServiceById = subjectService.getById(flor.getSubjectParentId());
        if (subjectServiceById != null) {
            flor.getParam().put("subjectParentTitle", subjectServiceById.getTitle());
        }
        Subject serviceById = subjectService.getById(flor.getSubjectId());
        if (serviceById != null) {
            flor.getParam().put("subjectTitle", serviceById.getTitle());
        }
    }

    //新增
    @Override
    public Long saveCourseInfo(CourseFormVo courseFormVo) {
        //添加课程信息，操作course表
        Course course = new Course();
        //工具类把一个对象的值复制到另一个对象中去(名称一样才复制不一样就不会复制)
        BeanUtils.copyProperties(courseFormVo, course);
        baseMapper.insert(course);

        //添加课程描述
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseFormVo.getDescription());//课程描述

        //设置课程id与主键一致
        courseDescription.setCourseId(course.getId());
        descriptionService.save(courseDescription);
        return course.getId();
    }

    //修改（获取id）
    @Override
    public CourseFormVo getCourseFormVoById(Long id) {
        //从course表中取数据
        Course course = baseMapper.selectById(id);
        if (course == null) {
            return null;
        }
        //从course_description表中取数据
        /*
        1.getOne() 方法是 MyBatis-Plus 中的一个查询方法，用于查询符合条件的一条记录，如果有多条符合条件的记录，那么只会返回第一条记录。
        2.rappers.<CourseDescription>lambdaQuery() 创建了一个 LambdaQueryWrapper 对象，该对象用于构建查询条件。
        这里指定泛型 <CourseDescription> 是为了让 LambdaQueryWrapper 对象知道查询的表和实体类
        3. .eq是指查询条件为 CourseDescription 实体类的 courseId 属性等于参数 course 对象的 id 属性。
         */
        CourseDescription description = descriptionService.getOne(
                Wrappers.lambdaQuery(CourseDescription.class)
                        .eq(CourseDescription::getCourseId, course.getId()));
        //创建courseInfoForm对象(封装)
        CourseFormVo courseFormVo = new CourseFormVo();
        //工具类把一个对象的值复制到另一个对象中去(名称一样才复制不一样就不会复制)
        BeanUtils.copyProperties(course, courseFormVo);
        if (description != null) {
            courseFormVo.setDescription(description.getDescription());
        }
        return courseFormVo;
    }

    //修改
    @Override
    public void updateCourseById(CourseFormVo courseFormVo) {
        //修改基本信息
        Course course = new Course();
        //工具类把一个对象的值复制到另一个对象中去(名称一样才复制不一样就不会复制)
        BeanUtils.copyProperties(courseFormVo, course);
        baseMapper.updateById(course);

        //修改描述信息(通过基本信息id获取描述id)
        CourseDescription courseDescription = descriptionService.getOne(
                Wrappers.<CourseDescription>lambdaQuery()
                        .eq(CourseDescription::getCourseId, course.getId()));
//        CourseDescription description = new LambdaQueryChainWrapper<>(descriptionService.getBaseMapper())
//                .eq(CourseDescription::getCourseId, course.getId())
//                .one();
//        CourseDescription description = descriptionService.getOne(
//                new LambdaQueryWrapper<CourseDescription>()
//                        .eq(CourseDescription::getCourseId, course.getId()));

        courseDescription.setDescription(courseFormVo.getDescription());
        courseDescription.setCourseId(course.getId());
        descriptionService.updateById(courseDescription);
    }

    //课程发布(根据id获取课程发布信息)
    @Override
    public CoursePublishVo getCoursePublishVo(Long id) {
        return baseMapper.selectCoursePublishVoById(id);
    }
    //课程发布(根据id发布课程)
    @Override
    public boolean publishCourseById(Long id) {
        Course course = new Course();
        course.setId(id);
        course.setPublishTime(new Date());//发布时间
        course.setStatus(1);//发布状态
        return this.updateById(course);
    }

    //删除
    @Override
    public void removeCourseById(Long id) {
        //根据课程id删除小节
        videoService.removeVideoByCourseId(id);
        //根据课程id删除章节
        chapterService.removeChapterByCourseId(id);
        //根据课程id删除描述
        descriptionService.removeById(id);
        //根据课程id删除课程
        baseMapper.deleteById(id);
    }

    //公众号根据课程分类查询相关课程信息
    @Override
    public Map<String,Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        //获取条件值
        String title = courseQueryVo.getTitle();//名称
        Long subjectId = courseQueryVo.getSubjectId();//二级分类
        Long subjectParentId = courseQueryVo.getSubjectParentId();//一级分类
        Long teacherId = courseQueryVo.getTeacherId();//讲师
        //判空
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(title)){
            wrapper.like(Course::getTitle,title);
        }
        if(!StringUtils.isEmpty(subjectId)) {
            wrapper.eq(Course::getSubjectId,subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq(Course::getSubjectParentId,subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            wrapper.eq(Course::getTeacherId,teacherId);
        }
        //分页
        Page<Course> pages = baseMapper.selectPage(pageParam, wrapper);
        long totalCount = pages.getTotal();//总记录数
        long totalPage = pages.getPages();//总页数
        long currentPage = pages.getCurrent();//当前页
        long size = pages.getSize();//每页记录数

        //每页数据集合
        List<Course> records = pages.getRecords();
        records.forEach(this::getTeacherOrSubjectName);

        Map<String,Object> map = new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("records",records);

        return map;
    }

    //获取讲师和分类名称
    private void getTeacherOrSubjectName(Course course) {
        //查询讲师名称
        Teacher teacher = teacherService.getById(course.getTeacherId());
        if (teacher != null){
            course.getParam().put("teacherName",teacher.getName());
        }
        //查询课程分类父级名称
        Subject subjectOne = subjectService.getById(course.getSubjectParentId());
        if(subjectOne != null) {
            course.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }
        //查询课程分类名称
        Subject subjectTwo = subjectService.getById(course.getSubjectId());
        if(subjectTwo != null) {
            course.getParam().put("subjectTitle",subjectTwo.getTitle());
        }
    }

    //公众号根据id查询课程详细信息
    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        //更新浏览量
        Course course = baseMapper.selectById(courseId);
        //得到数据库数量加一
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
        //查询课程信息
        Map<String, Object> map = new HashMap<>();
        CourseVo courseVo = baseMapper.selectCourseVoById(courseId);//详情
        List<ChapterVo> chapterVoList = chapterService.getNestedTreeList(courseId);//小节
        CourseDescription courseDescription = descriptionService.getById(courseId);//描述
        Teacher teacher = teacherService.getById(course.getTeacherId());//讲师
        //封装返回
        //TODO后续完善
        Boolean isBuy = false;

        map.put("courseVo", courseVo);
        map.put("chapterVoList", chapterVoList);
        map.put("description", null != courseDescription ?
                courseDescription.getDescription() : "");//描述可能为空的情况这里判断是否返回空，而不是null
        map.put("teacher", teacher);
        map.put("isBuy", isBuy);//是否购买
        return map;
    }

    //直播查询所有课程
    @Override
    public List<Course> findlist() {
        List<Course> list = baseMapper.selectList(null);
        list.forEach(this::getTeacherOrSubjectName);
        return list;
    }
}
