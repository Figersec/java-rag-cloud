package com.kailin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @Author chengpuhui
 * @Date 2021/12/15
 */
@SpringBootApplication(scanBasePackages = {"com.kailin.*","com.kailinjt.*"})
@EnableRetry
@EnableFeignClients(basePackages = {"com.kailin.*"})
@MapperScan(basePackages={"com.kailin.**.mapper"})
@EnableAspectJAutoProxy
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
