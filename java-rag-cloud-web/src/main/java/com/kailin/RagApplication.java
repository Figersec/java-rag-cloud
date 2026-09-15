package com.kailin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.util.StopWatch;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;

/**
 * @Author chengpuhui
 * @Date 2021/12/15
 */
@SpringBootApplication(scanBasePackages = "com.kailin", exclude = {
        DataSourceAutoConfiguration.class,
        DruidDataSourceAutoConfigure.class
})
@EnableRetry
@EnableFeignClients(basePackages = "com.kailin")
@MapperScan(basePackages = {"com.kailin.**.mapper"})
@EnableAspectJAutoProxy
@Slf4j
@EnableConfigurationProperties
public class RagApplication {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ConfigurableApplicationContext run = SpringApplication.run(RagApplication.class, args);
        Environment environment = run.getBean(Environment.class);
        String port = environment.getProperty("server.port");
        String contextPath = environment.getProperty("server.servlet.context-path");
        stopWatch.stop();

        log.info("API接口文档地址: http://127.0.0.1:{}{}/doc.html", port, contextPath);
        log.info("应用程序启动成功，总共花费【{}】秒,当前使用的命名空间(多个配置则互补):{}", stopWatch.getTotalTimeSeconds(), environment.getActiveProfiles());
    }


}
