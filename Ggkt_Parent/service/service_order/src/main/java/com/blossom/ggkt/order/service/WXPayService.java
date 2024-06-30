package com.blossom.ggkt.order.service;

import java.util.Map;

//微信支付
public interface WXPayService {
    Map<String,Object> createJsapi(String orderNo);

    //微信支付状态
    Map<String, String> queryPayStatus(String orderNo);
}
