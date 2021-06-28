package com.stylefeng.guns;

import com.stylefeng.guns.order.OrderApplication;
import com.stylefeng.guns.order.common.utils.FTPUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class GunsRestApplicationTests {

	@Autowired
	private FTPUtils ftpUtils;

	@Test
	public void contextLoads() {
	}

}
