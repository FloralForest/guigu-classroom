package com.blossom.ggkt.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blossom.ggkt.activity.mapper.CouponInfoMapper;
import com.blossom.ggkt.activity.service.CouponInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blossom.ggkt.activity.service.CouponUseService;
import com.blossom.ggkt.client.user.UserInfoFeignClient;
import com.blossom.ggkt.model.activity.CouponInfo;
import com.blossom.ggkt.model.activity.CouponUse;
import com.blossom.ggkt.model.user.UserInfo;
import com.blossom.ggkt.vo.activity.CouponUseQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author blossom
 * @since 2023-07-19
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {
    @Resource
    private CouponUseService couponUseService;
    @Resource
    private UserInfoFeignClient userInfoFeignClient;

    //获取已使用的优惠卷分页
    @Override
    public IPage<CouponUse> selectCouponUsePage(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo) {
        //获取条件值
        Long couponId = couponUseQueryVo.getCouponId();
        String couponStatus = couponUseQueryVo.getCouponStatus();
        String getTimeBegin = couponUseQueryVo.getGetTimeBegin();
        String getTimeEnd = couponUseQueryVo.getGetTimeEnd();
        //封装条件
        LambdaQueryWrapper<CouponUse> wrapper = new LambdaQueryWrapper<>();
        //判空封装
        if (!StringUtils.isEmpty(couponId)){
            wrapper.eq(CouponUse::getCouponId,couponId);
        }
        if (!StringUtils.isEmpty(couponStatus)){
            wrapper.eq(CouponUse::getCouponStatus,couponStatus);
        }
        if (!StringUtils.isEmpty(getTimeBegin)){
            wrapper.ge(CouponUse::getGetTime,getTimeBegin);
        }
        if (!StringUtils.isEmpty(getTimeEnd)){
            wrapper.le(CouponUse::getGetTime,getTimeEnd);
        }
        //调用方法查询---分页和条件
        IPage<CouponUse> page = couponUseService.page(pageParam,wrapper);
        //封装用户昵称和手机号
        List<CouponUse> couponUseList = page.getRecords();
        couponUseList.forEach(this::getUserInfoBycouponUse);
        return page;
    }

    //封装用户昵称和手机号
    private void getUserInfoBycouponUse(CouponUse couponUse) {
        //根据用户id远程调用获取用户信息
        Long userId = couponUse.getUserId();
        if (!StringUtils.isEmpty(userId)){
            //远程调用
            UserInfo userInfo = userInfoFeignClient.getById(userId);
            //用户不为空
            if (userInfo != null){
                couponUse.getParam().put("nickName",userInfo.getNickName());
                couponUse.getParam().put("phone",userInfo.getPhone());
            }
        }
    }

    //使用后更新优惠卷
    @Override
    public void updateCouponInfoUseStatus(Long couponUseId, Long orderId) {
        CouponUse couponUse = new CouponUse();
        couponUse.setId(couponUseId);
        couponUse.setOrderId(orderId);
        couponUse.setCouponStatus("1");
        couponUse.setUsingTime(new Date());
        couponUseService.updateById(couponUse);
    }
}
