package com.blossom.ggkt.live.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blossom.ggkt.model.live.LiveCourse;
import com.blossom.ggkt.vo.live.LiveCourseConfigVo;
import com.blossom.ggkt.vo.live.LiveCourseFormVo;
import com.blossom.ggkt.vo.live.LiveCourseVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播课程表 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-07-27
 */
public interface LiveCourseService extends IService<LiveCourse> {

    //分页
    IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam);

    //添加直播
    void save(LiveCourseFormVo liveCourseVo);

    //删除直播
    void removeLive(Long id);

    //查询和更新
    LiveCourseFormVo getLiveCourseFormVo(Long id);
    void updateLiveById(LiveCourseFormVo liveCourseVo);

    //获取直播配置信息
    LiveCourseConfigVo getCourseConfig(Long id);

    //修改直播配置
    void updateConfig(LiveCourseConfigVo liveCourseConfigVo);

    //获取最近5个直播信息
    List<LiveCourseVo> getLatelyList();

    //获取用户access_token
    JSONObject getAccessToken(Long id, Long userId);

    //根据id得到直播详情信息
    Map<String, Object> getInfoById(Long courseId);
}
