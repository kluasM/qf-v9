package com.qf.freemarker;

import com.qf.freemarker.entity.Product;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FreemarkerApplicationTests {

	@Autowired
	private Configuration configuration;

	@Test
	public void createHtml() throws IOException, TemplateException {
		//模板+数据=输出
		//1.获取到模板对象
		Template template = configuration.getTemplate("freemarker.ftl");
		//2.数据
		Map<String,Object> data = new HashMap<>();
		data.put("name","java1809");
		//对象
		Product product = new Product();
		product.setId(1);
		product.setName("超强电风扇");
		product.setBirthday(new Date());
		data.put("product",product);

		Product product2 = new Product();
		product2.setId(2);
		product2.setName("超强电冰箱");
		product2.setBirthday(new Date());

		List<Product> list = new ArrayList<>();
		list.add(product);
		list.add(product2);
		data.put("list",list);

		data.put("age",18);
		data.put("msg","用户名或密码错误");
		//3.两者结合，输出
		FileWriter writer = new FileWriter(
				"D:\\qf-v9\\qf-v9-temp\\freemarker\\src\\main\\resources\\templates\\freemarker.html");
		template.process(data,writer);
		System.out.println("生成静态页成功！");
	}

}
