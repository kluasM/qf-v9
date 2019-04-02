package com.qf.email;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailApplicationTests {

	@Autowired
	//private MailSender mailSender;
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;


	@Value("${mail.address}")
	private String from;

	@Test
	public void contextLoads() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo("2678383176@qq.com");
		message.setSubject("成功与网易公司建立深度合作关系");
		message.setText("合作关系初步达成，工程测试成功！请点击链接进行激活<a href='https://www.baidu.com'>激活</a>");
		mailSender.send(message);
	}

	@Test
	public void sendHTML() throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
		helper.setFrom(from);
		helper.setTo("2678383176@qq.com");
		helper.setSubject("成功与网易公司建立深度合作关系");
		helper.setText("请点击链接进行激活<a href='https://www.baidu.com'>激活</a>",true);
		mailSender.send(mimeMessage);
	}

	@Test
	public void sendWithAttachment() throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
		helper.setFrom(from);
		helper.setTo("2678383176@qq.com");
		helper.setSubject("成功与网易公司建立深度合作关系");
		helper.setText("请点击链接进行激活<a href='https://www.baidu.com'>激活</a>",true);
		//携带附件
		FileSystemResource resource = new FileSystemResource("D:\\qf-v9\\qf-v9-temp\\email\\ok.jpg");
		helper.addAttachment("ok.jpg",resource);
		//
		mailSender.send(mimeMessage);
	}

	@Test
	public void sendWithTemplate() throws MessagingException {
		Context context = new Context();
		context.setVariable("username","zhangsan");
		//获取到要发送的模板信息
		String content = templateEngine.process("active", context);

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
		helper.setFrom(from);
		helper.setTo("2678383176@qq.com");
		helper.setSubject("成功与网易公司建立深度合作关系");
		helper.setText(content,true);
		//携带附件
		FileSystemResource resource = new FileSystemResource("D:\\qf-v9\\qf-v9-temp\\email\\ok.jpg");
		helper.addAttachment("ok.jpg",resource);
		//
		mailSender.send(mimeMessage);
	}

}
