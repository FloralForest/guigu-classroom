package com.blossom.ggkt.live.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mtcloud")//配置文件中找到mtcloud值完成注入
public class MTCloudAccountConfig {

    private String openId;
    private String openToken;

}
