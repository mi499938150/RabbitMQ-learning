package com.mi.send;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author : Rong
 * @date : 2021/4/30
 * @Desc:
 */
@SpringBootApplication
@MapperScan(value = "com.mi.send",annotationClass = Mapper.class)
@ComponentScan("com.mi.send")
public class SendApplication {
    public static void main(String args[]) {
        SpringApplication.run(SendApplication.class, args);
    }
}