package com.qf.qfv9cartservice;

import com.qf.api.cart.ICartService;
import com.qf.api.cart.vo.CartItemVO;
import com.qf.v9.common.pojo.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QfV9CartServiceApplicationTests {

	@Autowired
	private ICartService cartService;

	@Test
	public void listTest() {
		String key = "user:cart:666";
		ResultBean resultBean = cartService.list(key);
		//
		if(resultBean.getStatusCode() == 200){
			List<CartItemVO> vos = (List<CartItemVO>) resultBean.getData();
			for (CartItemVO vo : vos) {
				System.out.println(vo);
			}
		}else{
			System.out.println(resultBean.getMsg());
		}
	}

	@Test
	public void addTest(){
		String key = "user:cart:666";
		ResultBean resultBean = cartService.add(key, 1L, 100);
		if(resultBean.getStatusCode() == 200){
			System.out.println("添加成功！");
		}
	}



}
