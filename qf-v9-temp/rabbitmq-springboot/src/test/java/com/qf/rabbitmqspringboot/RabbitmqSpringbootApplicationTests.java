package com.qf.rabbitmqspringboot;

import com.qf.rabbitmqspringboot.publish.Sender;
import com.qf.rabbitmqspringboot.simple.SimpleSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringbootApplicationTests {

	@Autowired
	private Sender sender;

	@Test
	public void contextLoads() {
		sender.send("热烈庆祝SpringBoot整合RabbitMQ成功！");
	}

}
