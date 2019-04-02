package com.qf.qfv9userservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.qf.v9.mapper")
public class QfV9UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QfV9UserServiceApplication.class, args);
	}

}
