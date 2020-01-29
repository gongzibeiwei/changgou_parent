package com.changgou.consume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.changgou.consume.dao")
public class OrderConsumeApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderConsumeApplication.class, args);
    }
}
