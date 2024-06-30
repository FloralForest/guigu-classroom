package com.blossom.ggkt.order.api;

import com.blossom.ggkt.order.service.OrderInfoService;
import com.blossom.ggkt.order.service.WXPayService;
import com.blossom.ggkt.result.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

//微信支付
@RestController
@RequestMapping("/api/order/wxPay")
public class WXPayController {
    @Resource
    private WXPayService wxPayService;

    @Resource
    private OrderInfoService orderInfoService;

    @GetMapping("/createJsapi/{orderNo}")
    public Result<Map<String,Object>> createJsapi(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) {
        return Result.ok(wxPayService.createJsapi(orderNo));
    }

    //支付后查询状态 注意订单号不是订单id
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result<Object> queryPayStatus(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) {

        System.out.println("orderNo:"+orderNo);
        //调用查询接口
        Map<String, String> resultMap = wxPayService.queryPayStatus(orderNo);
        if (resultMap == null) {//出错
            return Result.fail(null).message("支付出错");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) {//如果成功
            //更改订单状态，处理支付结果
            String out_trade_no = resultMap.get("out_trade_no");
            System.out.println("out_trade_no:"+out_trade_no);
            orderInfoService.updateOrderStatus(out_trade_no);
            return Result.ok(null).message("支付成功");
        }
        return Result.ok(null).message("支付中");
    }
}
