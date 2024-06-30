package com.blossom.ggkt.wechat.service;

import java.util.Map;

public interface MessageService {
    //接收消息
    String receiveMessage(Map<String, String> param);

    //模板消息
    void pushPayMessage(long l);
}
