package com.qf.springbootredis;

import com.qf.springbootredis.entity.ProductType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRedisApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void contextLoads() {
		redisTemplate.opsForValue().set("k1","v1");
		System.out.println(redisTemplate.opsForValue().get("k1"));
	}

	@Test
	public void saveCodeToRedisTest(){
		//此处模拟保存验证码的关键业务逻辑
		//1，拿到用户的手机号
		String phone = "18999881668";
		//2，生成key
		StringBuilder redisKey = new StringBuilder("register:code:").append(phone);
		//3，生成一个随机的验证码
		String code = "6688";
		//4，将这个信息保存到Redis中
		redisTemplate.opsForValue().set(redisKey.toString(),code);
		//5，设置有效期
		redisTemplate.expire(redisKey.toString(),10, TimeUnit.MINUTES);
		//6, 发送短信验证码
		System.out.println("短信验证码发送成功！");
	}

	@Test
	public void checkCodeTest(){
		//1.获取手机号及用户填写的验证码
		String phone = "18999881668";
		String code = "6699";
		//2，生成key
		StringBuilder redisKey = new StringBuilder("register:code:").append(phone);
		//3.去redis查找
		String currentCode = (String) redisTemplate.opsForValue().get(redisKey.toString());
		//4.比较
		if(code.equals(currentCode)){
			System.out.println("验证码正确，可以注册！");
		}else{
			System.out.println("验证码错误！");
		}
	}

	@Test
	public void saveActivateCodeTest(){
		//1.用户记录生成成功，获取当前用户记录id
		long userId = 1L;
		//2.生成激活码
		String activateCode = UUID.randomUUID().toString();
		System.out.println(activateCode);
		//3.保存激活码跟用户id的关系，方便后续的激活操作
		StringBuilder redisKey = new StringBuilder("register:activate:").append(activateCode);
		//4.
		redisTemplate.opsForValue().set(redisKey.toString(),userId);
		redisTemplate.expire(redisKey.toString(),1,TimeUnit.DAYS);
		//5.发送激活邮件
		System.out.println("发送激活邮件！");
	}

	@Test
	public void activateUserTest(){
		//1.获取到激活链接中的激活码
		String activateCode = "fd92cc1d-462e-4e28-872c-1760211f00ea";
		//2.
		StringBuilder redisKey = new StringBuilder("register:activate:").append(activateCode);
		//3.根据key，查找userId
		Object o = redisTemplate.opsForValue().get(redisKey.toString());
		if(o == null){
			System.out.println("激活失败");
			return;
		}
		Long userId = (Long) o;
		//4.更新用户的状态
		System.out.println("更新"+userId+"的用户状态！");
		//5.删除掉这个激活码凭证信息
		redisTemplate.delete(redisKey.toString());
	}

	@Test
	public void cacheTest(){
		//1.需求：查看商品的分类信息
		StringBuilder redisKey = new StringBuilder("productType");
		//2.先从缓存中查找
		List<ProductType> list = (List<ProductType>) redisTemplate.opsForValue().get(redisKey.toString());
		//3.如果缓存没有，查数据库
		if(list == null || list.isEmpty()){
			//查数据库
			System.out.println("开始从数据库中查询.....");
			list = new ArrayList<>();
			list.add(new ProductType(1L,0L,"手机数码"));
			list.add(new ProductType(2L,0L,"高新科技"));
			System.out.println("从数据库查找结束....");
			//4,并将其保存到缓存中
			redisTemplate.opsForValue().set(redisKey.toString(),list);
			return;
		}

		System.out.println("从缓存中查找到数据....");
		for (ProductType productType : list) {
			System.out.println(productType);
		}
	}

}
