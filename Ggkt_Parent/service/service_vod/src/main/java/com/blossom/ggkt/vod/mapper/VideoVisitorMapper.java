package com.blossom.ggkt.vod.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blossom.ggkt.model.vod.VideoVisitor;
import com.blossom.ggkt.vo.vod.VideoVisitorCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 视频来访者记录表 Mapper 接口
 * </p>
 *
 * @author blossom
 * @since 2023-05-13
 */
public interface VideoVisitorMapper extends BaseMapper<VideoVisitor> {
    ////显示统计数据(字段多时推荐起别名)
    List<VideoVisitorCountVo> findCount(@Param("courseId") Long courseId,
                                        @Param("startDate")String startDate,
                                        @Param("endDate")String endDate);
}
