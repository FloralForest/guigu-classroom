package com.blossom.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ExcelListener extends AnalysisEventListener<User> {

    //创建list集合封装最终的数据
    List<User> list = new ArrayList<>();

    //一行一行读取excel内容，把内容封装到user（但是机器认为第一行是表头会从第二行读取）
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        System.out.println("==="+user);
        list.add(user);
    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
