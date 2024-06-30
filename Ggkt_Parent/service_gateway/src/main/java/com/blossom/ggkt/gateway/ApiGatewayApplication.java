package com.blossom.ggkt.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//网关（Gateway）启动类
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
//开启注册功能使用nacos
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("--------寻找nacos----------------");
        System.out.println("ApiGatewayApplication---启动成功！");
    }
}
