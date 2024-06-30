package com.blossom.ggkt.vod.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blossom.ggkt.model.vod.Subject;
import com.blossom.ggkt.vo.vod.SubjectEeVo;
import com.blossom.ggkt.vod.listener.SubjectListener;
import com.blossom.ggkt.vod.mapper.SubjectMapper;
import com.blossom.ggkt.vod.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author blossom
 * @since 2023-05-04
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    //课程列表（懒加载=》每次只查询一层数据）
    @Override
    public List<Subject> selectList(Long id) {
        //mybatis-plus中实现查询的对象封装操作类
        //使用QueryWrapper时需要写数据库字段例如：wrapper.eq("parent_id",id);
        LambdaQueryWrapper<Subject> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Subject::getParentId, id);
        List<Subject> selectList = baseMapper.selectList(lambdaQueryWrapper);
        //向list集合中遍历每个Subject对象判断是否有下一层数据，如果有设置hasChildren = true
        for (Subject subject : selectList) {
            //ParentId 是父级的Id当ParentId大于0，就说明他是某个父级的子级
            Long subjectId = subject.getId();
            boolean isChild = this.isChildren(subjectId);
            subject.setHasChildren(isChild);
        }

        return selectList;
    }

    //判断id下面是否有子节点
    private boolean isChildren(Long id) {
        //使用QueryWrapper时需要写数据库字段例如：wrapper.eq("parent_id",id);
        LambdaQueryWrapper<Subject> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Subject::getParentId, id);
        //得到查询记录数
        Integer integer = baseMapper.selectCount(lambdaQueryWrapper);
        return integer > 0;
    }

    //导出
    @Override
    public void exportData(HttpServletResponse response) {
        //设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName;
        try {
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            fileName = URLEncoder.encode("课程分类", "UTF-8");
            //设置头信息
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
            //查询课程分类所有数据
            List<Subject> subjectList = baseMapper.selectList(null);
            //把List<Subject>变成List<SubjectEeVo>
            List<SubjectEeVo> subjectEeVoList = new ArrayList<>(subjectList.size());
            for (Subject subject : subjectList) {
                SubjectEeVo subjectEeVo = new SubjectEeVo();
//                sjEv.setId(sjt.getId());
//                sjEv.setParentId(sjt.getParentId());
                //工具类把一个对象的值复制到另一个对象中去(名称一样才复制不一样就不会复制)
                BeanUtils.copyProperties(subject, subjectEeVo);
                subjectEeVoList.add(subjectEeVo);
            }

            EasyExcel.write(response.getOutputStream(), SubjectEeVo.class).sheet("课程分类").doWrite(subjectEeVoList);
        } catch (UnsupportedEncodingException e) {
            // log the exception and re-throw it
            throw new RuntimeException("Encoding error: " + e.getMessage(), e);
        } catch (IOException e) {
            // log the exception and re-throw it
            throw new RuntimeException("IO error: " + e.getMessage(), e);
        } catch (Exception e) {
            // log the exception and re-throw it
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }

    //导入
    @Resource
    private SubjectListener subjectListener;

    @Override
    public void importDictData(MultipartFile file) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        try {
            EasyExcel.read(file.getInputStream(), SubjectEeVo.class, subjectListener).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException("导入失败！");
        }
    }
}
