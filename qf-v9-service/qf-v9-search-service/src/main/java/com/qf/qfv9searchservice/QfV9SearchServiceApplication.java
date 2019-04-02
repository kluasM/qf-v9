package com.qf.qfv9searchservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.qf.v9.mapper")
public class QfV9SearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QfV9SearchServiceApplication.class, args);
	}

}
