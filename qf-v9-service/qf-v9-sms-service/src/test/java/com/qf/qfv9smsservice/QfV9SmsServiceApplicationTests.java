package com.qf.qfv9smsservice;

import com.qf.api.sms.ISMS;
import com.qf.api.sms.pojo.ResponseCode;
import com.qf.api.sms.pojo.SMSResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QfV9SmsServiceApplicationTests {

	@Autowired
	private ISMS sms;

	@Test
	public void contextLoads() {
		SMSResponse smsResponse = sms.sendCode("13691986691", "666");
		System.out.println(smsResponse.getRespCode());
		if(ResponseCode.SUCCESS.equals(smsResponse.getRespCode())){
			System.out.println("发送成功！");
		}
	}

}
