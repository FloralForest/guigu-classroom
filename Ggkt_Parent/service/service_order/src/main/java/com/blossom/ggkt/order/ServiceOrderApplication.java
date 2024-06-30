package com.blossom.ggkt.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//开启注册功能使用nacos
@EnableDiscoveryClient
//实现远程调用
@EnableFeignClients(basePackages = "com.blossom")
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class, args);
        System.out.println("--------寻找nacos----------------");
        System.out.println("ServiceOrderApplication---启动成功！");
    }
}
