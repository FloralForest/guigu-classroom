package com.blossom.ggkt.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//开启注册功能使用nacos
@EnableDiscoveryClient
//实现远程调用
@EnableFeignClients(basePackages = "com.blossom")
public class ServiceActivityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceActivityApplication.class, args);
        System.out.println("--------寻找nacos----------------");
        System.out.println("ServiceActivityApplication---启动成功！");
    }
}
