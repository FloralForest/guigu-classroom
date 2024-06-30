package com.blossom.ggkt.vod.service.impl;

import com.blossom.ggkt.model.vod.VideoVisitor;
import com.blossom.ggkt.vo.vod.VideoVisitorCountVo;
import com.blossom.ggkt.vod.mapper.VideoVisitorMapper;
import com.blossom.ggkt.vod.service.VideoVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频来访者记录表 服务实现类
 * </p>
 *
 * @author blossom
 * @since 2023-05-13
 */
@Service
public class VideoVisitorServiceImpl extends ServiceImpl<VideoVisitorMapper, VideoVisitor> implements VideoVisitorService {

    //课程统计
    @Override
    public Map<String, Object> findCount(Long courseId, String startDate, String endDate) {
        //查询数据
        List<VideoVisitorCountVo> visitorCountVoList = baseMapper.findCount(courseId, startDate, endDate);

        //创建map
        Map<String, Object> map = new HashMap<>();
        //创建两个list 一个装所有日期，一个装该日期对应用户的数量
        //封装使用stream流遍历visitorCountVoList
        //stream() 方法将 visitorCountVoList 转换为一个 Stream 对象，这是一个支持连续操作的数据流。
        // 接着，map() 方法将 Stream 中的每个 VideoVisitorCountVo 对象映射为它的 joinTime 属性，
        // 这样就得到了一个由 joinTime 组成的新的 Stream。最后，collect() 方法将 Stream 中的元素收集到一个新的 List 中，作为返回值。
        List<String> dateList = visitorCountVoList
                .stream().map(VideoVisitorCountVo::getJoinTime)
                .collect(Collectors.toList());

        //代表日期对应数量
        List<Integer> countList = visitorCountVoList
                .stream().map(VideoVisitorCountVo::getUserCount)
                .collect(Collectors.toList());

        map.put("xData",dateList);
        map.put("yData",countList);
        return map;
    }
}
