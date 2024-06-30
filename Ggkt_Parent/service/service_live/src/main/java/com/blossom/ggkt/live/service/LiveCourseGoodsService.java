package com.blossom.ggkt.live.service;

import com.blossom.ggkt.model.live.LiveCourseGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 直播课程关联推荐表 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-07-27
 */
public interface LiveCourseGoodsService extends IService<LiveCourseGoods> {

    //查询商品列表
    List<LiveCourseGoods> getGoodsListCourseId(Long id);
}
