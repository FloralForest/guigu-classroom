package com.blossom.ggkt.vod.service;

import com.blossom.ggkt.model.vod.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-05-04
 */
public interface SubjectService extends IService<Subject> {

    //课程列表（懒加载=》每次只查询一层数据）
    List<Subject> selectList(Long id);

    //导出
    void exportData(HttpServletResponse response);

    //导入
    void importDictData(MultipartFile file);
}
