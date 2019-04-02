package com.qf.qfv9itemservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.qf.v9.mapper")
public class QfV9ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QfV9ItemServiceApplication.class, args);
	}

}
