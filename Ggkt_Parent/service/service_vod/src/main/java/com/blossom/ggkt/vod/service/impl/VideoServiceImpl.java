package com.blossom.ggkt.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blossom.ggkt.model.vod.Video;
import com.blossom.ggkt.vod.mapper.VideoMapper;
import com.blossom.ggkt.vod.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blossom.ggkt.vod.service.VodService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author blossom
 * @since 2023-05-07
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Resource
    private VodService vodService;

    //根据课程id删除小节并且删除小节的视频
    @Override
    public void removeVideoByCourseId(Long id) {
        //根据课程id查询所有小节
        LambdaQueryWrapper<Video> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Video::getCourseId, id);
        List<Video> videoList = baseMapper.selectList(lambdaQueryWrapper);
        //遍历所有小节得到视频id
        for (Video video : videoList) {
            String videoSourceId = video.getVideoSourceId();
            //判断空是否删除视频
            if (!StringUtils.isEmpty(videoSourceId)) {
                vodService.removeVideo(videoSourceId);
            }
        }
        //最后删除课程
        baseMapper.delete(lambdaQueryWrapper);

    }

    //删除小节时删除视频
    @Override
    public void removeVideoById(Long id) {
        //判断小节中是否有视频，如果有先删除视频再删除小节
        Video video = baseMapper.selectById(id);
        //获取视频id
        String videoSourceId = video.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)) {
            //isEmpty() 方法用于判断一个字符串是否为 null 不为 null 删除
            vodService.removeVideo(videoSourceId);
        }

        //最后删除小节
        baseMapper.deleteById(id);
    }
}
