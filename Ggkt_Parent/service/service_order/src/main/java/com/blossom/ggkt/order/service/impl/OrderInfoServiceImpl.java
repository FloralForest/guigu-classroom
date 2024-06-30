package com.blossom.ggkt.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blossom.ggkt.client.activity.CouponInfoFeignClient;
import com.blossom.ggkt.client.course.CourseFeignClient;
import com.blossom.ggkt.client.user.UserInfoFeignClient;
import com.blossom.ggkt.interfacle.ResultCodeEnum;
import com.blossom.ggkt.model.order.OrderDetail;
import com.blossom.ggkt.model.order.OrderInfo;
import com.blossom.ggkt.model.user.UserInfo;
import com.blossom.ggkt.model.vod.Course;
import com.blossom.ggkt.order.mapper.OrderInfoMapper;
import com.blossom.ggkt.order.service.OrderDetailService;
import com.blossom.ggkt.order.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blossom.ggkt.utils.AuthContextHolder;
import com.blossom.ggkt.utils.JwtHelper;
import com.blossom.ggkt.utils.OrderNoUtils;
import com.blossom.ggkt.vo.order.OrderFormVo;
import com.blossom.ggkt.vo.order.OrderInfoQueryVo;
import com.blossom.ggkt.vo.order.OrderInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.rmi.activation.ActivationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author blossom
 * @since 2023-07-16
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    //订单详情
    @Resource
    private OrderDetailService orderDetailService;

    //订单使用到的远程调用
    @Resource
    private CourseFeignClient courseFeignClient;

    @Resource
    private UserInfoFeignClient userInfoFeignClient;

    @Resource
    private CouponInfoFeignClient couponInfoFeignClient;

    @Override
    public Map<String, Object> selectOrderInfoPage(Page<OrderInfo> pageParam, OrderInfoQueryVo orderInfoQueryVo) {

        //orderInfoQueryVo获取查询条件
        Long userId = orderInfoQueryVo.getUserId();
        String outTradeNo = orderInfoQueryVo.getOutTradeNo();
        String phone = orderInfoQueryVo.getPhone();
        String createTimeEnd = orderInfoQueryVo.getCreateTimeEnd();
        String createTimeBegin = orderInfoQueryVo.getCreateTimeBegin();
        Integer orderStatus = orderInfoQueryVo.getOrderStatus();
        //判断条件是否为空，进行封装
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        //不为空
        if (!StringUtils.isEmpty(userId)){
            wrapper.eq(OrderInfo::getUserId,userId);
        }
        if (!StringUtils.isEmpty(outTradeNo)){
            wrapper.eq(OrderInfo::getOutTradeNo,outTradeNo);
        }
        if (!StringUtils.isEmpty(phone)){
            wrapper.eq(OrderInfo::getPhone,phone);
        }
        //ge和le方法是QueryWrapper对象中的方法，用于添加大于等于和小于等于的查询条件。
        if (!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge(OrderInfo::getCreateTime,createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)){
            wrapper.le(OrderInfo::getCreateTime,createTimeEnd);
        }
        if (!StringUtils.isEmpty(orderStatus)){
            wrapper.eq(OrderInfo::getOrderStatus,orderStatus);
        }
        //实现分页
        Page<OrderInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        long totalCount = pages.getTotal();//总记录数
        long pagesCount = pages.getPages();//总页数
        List<OrderInfo> records = pages.getRecords();//每页数据的集合
        //根据id查询详情数据，进行封装
//        records.stream().forEach(item ->{
//            this.getOrderDetail(item);
//        });
        records.forEach(this::getOrderDetail);
        //所有数据存入map集合返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("total",totalCount);
        map.put("pagesCount",pagesCount);
        map.put("records",records);
        return map;
    }

    //查询订单详情数据
    private void getOrderDetail(OrderInfo orderInfo) {
        //订单id
        Long id = orderInfo.getId();
        //查询详情
        OrderDetail orderDetail = orderDetailService.getById(id);
        if (orderDetail != null){
            String courseName = orderDetail.getCourseName();
            orderInfo.getParam().put("courseName",courseName);
        }
    }

    //生成课程订单
    @Override
    public Long submitOrder(OrderFormVo orderFormVo,HttpServletRequest request) {
        //获取订单条件值
//        Long userId = AuthContextHolder.getUserId();//工具类获取id
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        Long courseId = orderFormVo.getCourseId();//课程
        Long couponId = orderFormVo.getCouponId();//优惠卷
        //判断当前用户是否已经生成订单
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getCourseId, courseId);
        wrapper.eq(OrderDetail::getUserId, userId);
        OrderDetail orderDetailExist = orderDetailService.getOne(wrapper);
        if (orderDetailExist != null){
            return orderDetailExist.getId(); //如果订单已存在，则直接返回订单id
        }
        //查询课程信息
        Course course = courseFeignClient.getById(courseId);
        if (course == null){
            throw new ClassCastException(ResultCodeEnum.DATA_ERROR.getCode().toString()
                    + ": " + ResultCodeEnum.DATA_ERROR.getMessage());
        }
        //查询用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);
        if (userInfo == null) {
            throw new ClassCastException(ResultCodeEnum.DATA_ERROR.getCode().toString()
                    + ": " + ResultCodeEnum.DATA_ERROR.getMessage());
        }
        //查询优惠卷信息
        BigDecimal decimal = new BigDecimal(0);
        if(couponId != null) {
            decimal = couponInfoFeignClient.getById(couponId).getAmount();
        }
        //封装
        //创建订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setNickName(userInfo.getNickName());
        orderInfo.setPhone(userInfo.getPhone());
        orderInfo.setProvince(userInfo.getProvince());
        orderInfo.setOriginAmount(course.getPrice());
        orderInfo.setCouponReduce(decimal);
        //生成订单的总价减去优惠卷的价格得到最终金额
        orderInfo.setFinalAmount(orderInfo.getOriginAmount().subtract(orderInfo.getCouponReduce()));
        orderInfo.setOutTradeNo(OrderNoUtils.getOrderNo());
        orderInfo.setTradeBody(course.getTitle());
        orderInfo.setOrderStatus("0");
        baseMapper.insert(orderInfo);

        //订单详情表
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderInfo.getId());
        orderDetail.setUserId(userId);
        orderDetail.setCourseId(courseId);
        orderDetail.setCourseName(course.getTitle());
        orderDetail.setCover(course.getCover());
        orderDetail.setOriginAmount(course.getPrice());
        orderDetail.setCouponReduce(new BigDecimal(0));
        orderDetail.setFinalAmount(orderDetail.getOriginAmount().subtract(orderDetail.getCouponReduce()));
        orderDetailService.save(orderDetail);
        //更新优惠卷
        if(null != orderFormVo.getCouponUseId()) {
            couponInfoFeignClient.updateCouponInfoUseStatus(orderFormVo.getCouponUseId(), orderInfo.getId());
        }
        return orderInfo.getId();
    }

    //订单详情接口
    @Override
    public OrderInfoVo getOrderInfoVoById(Long id) {
        //查询
        OrderInfo orderInfo = this.getById(id);
        OrderDetail orderDetail = orderDetailService.getById(id);
        //封装
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        BeanUtils.copyProperties(orderInfo, orderInfoVo);
        orderInfoVo.setCourseId(orderDetail.getCourseId());
        orderInfoVo.setCourseName(orderDetail.getCourseName());
        return orderInfoVo;
    }

    //更新支付状态
    @Override
    public void updateOrderStatus(String out_trade_no) {
        //根据out_trade_no查询订单
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOutTradeNo,out_trade_no);
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);
        //更新订单状态 1 已经支付
        orderInfo.setOrderStatus("1");
        baseMapper.updateById(orderInfo);
    }
}
