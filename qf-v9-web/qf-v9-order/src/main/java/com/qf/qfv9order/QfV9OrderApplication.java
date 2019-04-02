package com.qf.qfv9order;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class QfV9OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(QfV9OrderApplication.class, args);
	}

}
