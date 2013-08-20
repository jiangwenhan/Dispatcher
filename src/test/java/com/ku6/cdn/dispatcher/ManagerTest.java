package com.ku6.cdn.dispatcher;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ManagerTest {

	@Test
	public void test() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"common.rest.xml",
				"common.datasource.xml",
				"common.hibernate.xml",
				"common.dispatcher.xml");
		Manager manager = (Manager) context.getBean("manager");

		System.out.println(manager.getCdnSystemSessionFactory());
		System.out.println(manager.getCdnDeliverySessionFactory());
		System.out.println(manager.getUtccSessionFactory());
	}
	
}
