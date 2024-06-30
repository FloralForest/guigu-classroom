package com.blossom.ggkt.vod.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.blossom.ggkt.model.vod.Subject;
import com.blossom.ggkt.vo.vod.SubjectEeVo;
import com.blossom.ggkt.vod.mapper.SubjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//创建读取监听器（excel）
@Component//交给spring管理
public class SubjectListener extends AnalysisEventListener<SubjectEeVo> {

    //注入mapper
    @Resource
    private SubjectMapper subjectMapper;

    //一行一行读取excel内容，把内容封装到user（但是机器认为第一行是表头会从第二行读取）
    @Override
    public void invoke(SubjectEeVo subjectEeVo, AnalysisContext analysisContext) {
        //调用方法添加数据库
        Subject subject = new Subject();
        //subjectEeVo变成subject
        //工具类把一个对象的值复制到另一个对象中去(名称一样才复制不一样就不会复制)
        BeanUtils.copyProperties(subjectEeVo,subject);
        subjectMapper.insert(subject);
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
