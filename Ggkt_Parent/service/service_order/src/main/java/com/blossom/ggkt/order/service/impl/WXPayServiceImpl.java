package com.blossom.ggkt.order.service.impl;

import com.blossom.ggkt.model.user.UserInfo;
import com.blossom.ggkt.order.service.WXPayService;
import com.blossom.ggkt.utils.HttpClientUtils;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//微信支付
@Service
@Slf4j
public class WXPayServiceImpl implements WXPayService {

    @Override
    public Map<String, Object> createJsapi(String orderNo) {
        //封装微信支付需要的参数
        Map<String, String> paramMap = new HashMap<>();
        //1、设置参数
        paramMap.put("appid", "wxf913bfa3a2c7eeeb");//公众号服务号id
        paramMap.put("mch_id", "1481962542");//商户号
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());//随机数 每次支付订单都不一样
        paramMap.put("body", "test");//微信显示内容
        paramMap.put("out_trade_no", orderNo);//订单号
        paramMap.put("total_fee", "1");//支付金额
        paramMap.put("spbill_create_ip", "127.0.0.1");//支付客户端ip
        paramMap.put("notify_url", "http://glkt.atguigu.cn/api/order/wxPay/notify");//支付完成后跳转
        paramMap.put("trade_type", "JSAPI");//支付类型

        /**
         * 设置参数值当前用户的openid
         * 目前实现逻辑-》根据订单号获取用户id，根据用户id获取openid  在微信授权中已经将用户openid存入数据库
         *
         * 测试直接手动填写
         */
//          UserInfo userInfo = userInfoFeignClient.getById(paymentInfo.getUserId());
        //略
//			paramMap.put("openid", "oepf36SawvvS8Rdqva-Cy4flFFg");
        paramMap.put("openid", "oQTXC56lAy3xMOCkKCImHtHoLL");//商户key
        //HTTPClient调用支付接口
        try {
            HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");//微信官方的支付路径
            //client设置参数 把Map转成XML格式
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, "MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9"));
            client.setHttps(true);
            client.post();//发送
            //返回微信的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);//XML转Map
            if (null != resultMap.get("result_code") && !"SUCCESS".equals(resultMap.get("result_code"))) {
                System.out.println("errorWX");
            }
            //封装参数
            Map<String, String> parameterMap = new HashMap<>();
            String prepayId = String.valueOf(resultMap.get("prepay_id"));
            String packages = "prepay_id=" + prepayId;
            parameterMap.put("appId", "wxf913bfa3a2c7eeeb");
            parameterMap.put("nonceStr", resultMap.get("nonce_str"));
            parameterMap.put("package", packages);
            parameterMap.put("signType", "MD5");
            parameterMap.put("timeStamp", String.valueOf(new Date().getTime()));
            String sign = WXPayUtil.generateSignature(parameterMap, "MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9");

            //返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("appId", "wxf913bfa3a2c7eeeb");
            result.put("timeStamp", parameterMap.get("timeStamp"));
            result.put("nonceStr", parameterMap.get("nonceStr"));
            result.put("signType", "MD5");
            result.put("paySign", sign);
            result.put("package", packages);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    //微信支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map<String,String> paramMap = new HashMap<>();
            paramMap.put("appid", "wxf913bfa3a2c7eeeb");
            paramMap.put("mch_id", "1481962542");
            paramMap.put("out_trade_no", orderNo);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, "MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            //返回
            return WXPayUtil.xmlToMap(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
