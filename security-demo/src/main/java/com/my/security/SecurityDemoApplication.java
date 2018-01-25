package com.my.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author hzheng
 * @Date 2017/10/25
 */
@SpringBootApplication
@MapperScan("com.my.security.mapper")
public class SecurityDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityDemoApplication.class,args);
    }
}
