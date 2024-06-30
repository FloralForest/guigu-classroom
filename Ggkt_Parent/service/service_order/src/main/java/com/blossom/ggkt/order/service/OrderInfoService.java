package com.blossom.ggkt.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blossom.ggkt.model.order.OrderInfo;
import com.blossom.ggkt.vo.order.OrderFormVo;
import com.blossom.ggkt.vo.order.OrderInfoQueryVo;
import com.blossom.ggkt.vo.order.OrderInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-07-16
 */
public interface OrderInfoService extends IService<OrderInfo> {

    //订单列表
    Map<String, Object> selectOrderInfoPage(Page<OrderInfo> pageParam, OrderInfoQueryVo orderInfoQueryVo);

    //生成课程订单
    Long submitOrder(OrderFormVo orderFormVo, HttpServletRequest request);

    //订单详情接口
    OrderInfoVo getOrderInfoVoById(Long id);

    //更新支付状态
    void updateOrderStatus(String out_trade_no);
}
