package com.niit.bd4project;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.niit.bd4project.mapper")
public class Bd4ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(Bd4ProjectApplication.class, args);
    }

}
