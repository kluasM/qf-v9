package com.qf.qfv9index;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class QfV9IndexApplication {

	public static void main(String[] args) {

		SpringApplication.run(QfV9IndexApplication.class, args);
		
	}

}
