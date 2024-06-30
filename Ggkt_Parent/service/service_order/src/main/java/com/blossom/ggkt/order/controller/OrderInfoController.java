package com.blossom.ggkt.order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blossom.ggkt.model.order.OrderInfo;
import com.blossom.ggkt.order.service.OrderInfoService;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vo.order.OrderInfoQueryVo;
import com.blossom.ggkt.vo.order.OrderInfoVo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 订单表 订单表 前端控制器
 *
 * @author blossom
 * @since 2023-07-16
 */
@RestController
@RequestMapping(value="/admin/order/orderInfo")
public class OrderInfoController {
    @Resource
    private OrderInfoService orderInfoService;

    //订单列表@PathVariable用于获取URL中路径的参数值
    @GetMapping("{page}/{limit}")
    public Result<Map<String, Object>> listOrder(@PathVariable Long page,
                            @PathVariable Long limit,
                            OrderInfoQueryVo orderInfoQueryVo){
        //创建分页对象
        Page<OrderInfo> pageParam = new Page<>(page,limit);
        Map<String,Object> map = orderInfoService.selectOrderInfoPage(pageParam,orderInfoQueryVo);
        return Result.ok(map);
    }
}

