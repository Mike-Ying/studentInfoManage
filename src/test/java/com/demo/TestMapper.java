package com.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.bean.UserBean;
import com.demo.mapper.UserMapper;
import com.demo.controller.UserController;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestMapper {
	@Autowired
    private UserMapper userMapper;
	
	@Order(1)
	@Test
	public void setUp() {
		userMapper.clearUser();
	}
	
	@Order(2)
	@Test
	public void testSelectZero() {
		assertEquals(0,userMapper.getUser().size());
	}
	
	@Order(3)
	@Test
	public void testInsertTwo() {
		
		Date sqlDate = UserController.strToDate("2018-09-01");
		
		UserBean user1 = new UserBean("应文灿",sqlDate,"185099","13911682123","统招全日制");
		userMapper.insertUser(user1);
		assertEquals(1,userMapper.getUser().size());
		UserBean user2 = new UserBean("Mike",sqlDate,"000001","13910310110","成人高考");
		userMapper.insertUser(user2);
		assertEquals(2,userMapper.getUser().size());
	}
	
	@Order(4)
	@Test
	public void testUpdate() {
		
		Date sqlDate = UserController.strToDate("2018-08-30");
		
		UserBean userUp = new UserBean("应文灿",sqlDate,"185099","13911682123","统招全日制");
		userMapper.updateUser(userUp);
		Date date = null;
		for(UserBean user:userMapper.getUser()) {
			if (user.getName().equals("应文灿")) {
				date = user.getDate();
			}
		}
		assertEquals("2018-08-30",date.toString());
	}
	
	@Order(5)
	@Test
	public void testDelete() {
		userMapper.deleteUser("应文灿");
		assertEquals(1,userMapper.getUser().size());
		boolean tag = true;
		for(UserBean user:userMapper.getUser()) {
			if (user.getName().equals("应文灿")) {
				tag = false;
			}
		}
		assertTrue(tag);
	}
	
	@Order(6)
	@Test
	public void endWith() {
		userMapper.clearUser();
	}
}
