package com.qf.timer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimerApplicationTests {

	@Test
	public void contextLoads() throws IOException {
		System.out.println(new Date());
		System.in.read();
	}

}
