package com.lyx;

import com.lyx.process.service.MyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppTests
{
	@Autowired
	@Qualifier("myServiceImpl")
	private MyService myService;

	@Test
	void test1()
	{
		myService.getCode("222");
	}
}
