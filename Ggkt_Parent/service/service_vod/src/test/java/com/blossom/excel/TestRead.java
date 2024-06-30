package com.blossom.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestRead {
    public static void main(String[] args) {
        //设置文件名称和路径
        String fileName = "C:\\TestExcel\\guigu.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, User.class, new ExcelListener()).sheet().doRead();
    }
}
