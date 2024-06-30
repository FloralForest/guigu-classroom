package com.blossom.ggkt.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blossom.ggkt.model.vod.Chapter;
import com.blossom.ggkt.model.vod.Video;
import com.blossom.ggkt.vo.vod.ChapterVo;
import com.blossom.ggkt.vo.vod.VideoVo;
import com.blossom.ggkt.vod.mapper.ChapterMapper;
import com.blossom.ggkt.vod.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blossom.ggkt.vod.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author blossom
 * @since 2023-05-07
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Resource
    private VideoService videoService;//小节

    //大纲列表
    @Override
    public List<ChapterVo> getNestedTreeList(Long courseId) {
        //定义一个最终集合(遇到集合要么遍历要么return)
        List<ChapterVo> finalChapterVoList = new ArrayList<>();
        //根据courseId获取所有章节
        LambdaQueryWrapper<Chapter> wrapperChapter = new LambdaQueryWrapper<>();
        wrapperChapter.eq(Chapter::getCourseId, courseId);
        List<Chapter> chapterList = baseMapper.selectList(wrapperChapter);

        //根据courseId获取所有小节
        LambdaQueryWrapper<Video> wrapperVideo = new LambdaQueryWrapper<>();
        wrapperVideo.eq(Video::getCourseId, courseId);
        List<Video> videoList = videoService.list(wrapperVideo);

        //封装
        //遍历章节
        for (Chapter chapter : chapterList) {

            ChapterVo chapterVo = new ChapterVo();
            //封装章节的小节,填充列表数据：Video列表
            List<VideoVo> videoVoList = new ArrayList<>();
            for (Video video : videoList) {
                //判断小节属于那个章节,getChapterId是小节表中对应章节的id
                if (chapter.getId().equals(video.getChapterId())) {
                    VideoVo videoVo = new VideoVo();
                    //工具类把一个对象的值复制到另一个对象中去(名称一样才复制不一样就不会复制)
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoList.add(videoVo);
                }
            }

            //工具类把一个对象的值复制到另一个对象中去(名称一样才复制不一样就不会复制)
            BeanUtils.copyProperties(chapter, chapterVo);
            //小节丢给章节
            chapterVo.setChildren(videoVoList);
            //放入集合
            finalChapterVoList.add(chapterVo);
        }

        return finalChapterVoList;
    }

    //根据课程id删除章节
    @Override
    public void removeChapterByCourseId(Long id) {
        LambdaQueryWrapper<Chapter> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Chapter::getCourseId,id);
        baseMapper.delete(lambdaQueryWrapper);
    }
}
