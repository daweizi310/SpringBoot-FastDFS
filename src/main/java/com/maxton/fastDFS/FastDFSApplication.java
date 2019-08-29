package com.maxton.fastDFS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description 启动类
 * @Author maxton.zhang
 * @Date 2019/7/5 9:53
 * @Version 1.0
 */
@SpringBootApplication
@EnableSwagger2
@ComponentScan(value = {"com.maxton"})
@EnableTransactionManagement
public class FastDFSApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastDFSApplication.class, args);
    }
}
