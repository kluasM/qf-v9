package com.qf.qfv9cartservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qf.v9.mapper")
@EnableDubbo
public class QfV9CartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QfV9CartServiceApplication.class, args);
	}

}
