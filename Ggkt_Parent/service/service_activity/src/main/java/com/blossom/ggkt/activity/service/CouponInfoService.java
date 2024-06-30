package com.blossom.ggkt.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blossom.ggkt.model.activity.CouponInfo;
import com.blossom.ggkt.model.activity.CouponUse;
import com.blossom.ggkt.vo.activity.CouponUseQueryVo;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-07-19
 */
public interface CouponInfoService extends IService<CouponInfo> {

    //获取已使用的优惠卷分页
    IPage<CouponUse> selectCouponUsePage(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo);

    //使用后更新优惠卷
    void updateCouponInfoUseStatus(Long couponUseId, Long orderId);
}
