package com.ku6.cdn.dispatcher.common.resource;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.hibernate.Session;

import com.google.common.collect.Maps;

public class TranscodeServerResource {

	private static final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
	private static final Session session = null;
	
	private static Map<String, Boolean> map;

	public TranscodeServerResource() {
		ses.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				Map<String, Boolean> exchange = Maps.newConcurrentMap();
				String hql = "select inUse, dispStatus, ip, ip2 "
						+ "from Server "
						+ "where svrType=1";
				Iterator iterator = session.createQuery(hql).list().iterator();
				while (iterator.hasNext()) {
					Object[] tuple = (Object[]) iterator.next();
					String host1 = (String) tuple[2];
					String host2 = (String) tuple[3];
					Integer inUse = (Integer) tuple[0];
					Integer dispStatus = (Integer) tuple[1];
					boolean flag = false;
					if (dispStatus == 0 || inUse == 0)
						flag = false;
					else
						flag = true;
					if (host1 != null) {
						exchange.put(host1, flag);
					}
					if (host2 != null) {
						exchange.put(host2, flag);
					}
					TranscodeServerResource.map = exchange;
				}
			}
		}, 0, 3, TimeUnit.MINUTES);
	}
	
	public static Boolean put(String key, Boolean value) {
		return TranscodeServerResource.map.put(key, value);
	}
	
	public static Boolean get(String key) {
		return TranscodeServerResource.map.get(key);
	}
	
	public static boolean containsKey(String key) {
		return TranscodeServerResource.map.containsKey(key);
	}

}
