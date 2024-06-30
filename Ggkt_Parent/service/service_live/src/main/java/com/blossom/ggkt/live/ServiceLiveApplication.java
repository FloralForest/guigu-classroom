package com.blossom.ggkt.live;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient//开启注册功能使用nacos
@EnableFeignClients(basePackages = "com.blossom")//实现远程调用
@ComponentScan(basePackages = "com.blossom")//包扫描
@MapperScan("com.blossom.ggkt.live.mapper")//mapper扫描
public class ServiceLiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceLiveApplication.class, args);
        System.out.println("--------寻找nacos----------------");
        System.out.println("ServiceLiveApplication---启动成功！");
    }

}
