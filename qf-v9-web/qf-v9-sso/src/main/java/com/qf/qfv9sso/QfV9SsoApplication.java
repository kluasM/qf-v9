package com.qf.qfv9sso;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class QfV9SsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(QfV9SsoApplication.class, args);
	}

}
