package com.blossom.ggkt.order.api;

import com.blossom.ggkt.order.service.OrderInfoService;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vo.order.OrderFormVo;
import com.blossom.ggkt.vo.order.OrderInfoVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

//生成课程订单
@RestController
@RequestMapping("api/order/orderInfo")
public class OrderInfoApiController {
    @Resource
    private OrderInfoService orderInfoService;

    @PostMapping("submitOrder")
    public Result<Long> submitOrder(@RequestBody OrderFormVo orderFormVo, HttpServletRequest request) {
        //返回订单id
        Long orderId = orderInfoService.submitOrder(orderFormVo,request);
        return Result.ok(orderId);
    }

    //订单详情接口
    @GetMapping("getInfo/{id}")
    public Result<OrderInfoVo> getInfo(@PathVariable Long id) {
        OrderInfoVo orderInfoVo = orderInfoService.getOrderInfoVoById(id);
        return Result.ok(orderInfoVo);
    }
}
