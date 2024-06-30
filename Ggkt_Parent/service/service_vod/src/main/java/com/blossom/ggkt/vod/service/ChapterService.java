package com.blossom.ggkt.vod.service;

import com.blossom.ggkt.model.vod.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blossom.ggkt.vo.vod.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-05-07
 */
public interface ChapterService extends IService<Chapter> {

    //大纲列表
    List<ChapterVo> getNestedTreeList(Long courseId);

    //根据课程id删除章节
    void removeChapterByCourseId(Long id);
}
