package com.mooc.house;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mooc.house.common.model.User;
import com.mooc.house.service.UserService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HouseApplicationTests {
	@Autowired
	private UserService userService;
	@Test
	public void tesAuthor() {
		User auth = userService.auth("spring_boot@163.com", "111111");
		assert auth !=null;
	}
}