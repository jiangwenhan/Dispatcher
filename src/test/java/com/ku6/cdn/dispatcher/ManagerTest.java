package com.ku6.cdn.dispatcher;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ku6.cdn.dispatcher.common.collection.AbstractPair;

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
		System.out.println(manager.getUtccSessionFactory());
	}
	
	public class MyPair extends AbstractPair<Long, Boolean> {

		public MyPair(Long first, Boolean second) {
			super(first, second);
		}
		
	}
	
	@Test
	public void fuck() {
		
	}

}
