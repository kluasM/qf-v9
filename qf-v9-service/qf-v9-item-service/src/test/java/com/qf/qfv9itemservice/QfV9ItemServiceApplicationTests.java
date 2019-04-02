package com.qf.qfv9itemservice;

import com.qf.api.item.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QfV9ItemServiceApplicationTests {

	@Autowired
	private ItemService itemService;

	@Test
	public void contextLoads() {
		List<Long> ids = new ArrayList<>();
		for (long i = 1; i <= 8; i++) {
			ids.add(i);
		}
		itemService.batchCreateHtml(ids);
	}

}
