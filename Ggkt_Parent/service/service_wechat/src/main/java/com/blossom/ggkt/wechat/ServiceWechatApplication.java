package com.blossom.ggkt.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
//实现远程调用
@EnableFeignClients(basePackages = "com.blossom")
@MapperScan("com.blossom.ggkt.wechat.mapper")
public class ServiceWechatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceWechatApplication.class, args);
        System.out.println("--------寻找nacos----------------");
        System.out.println("ServiceWechatApplication---启动成功！");
    }
}
