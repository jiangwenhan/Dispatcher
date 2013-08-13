package com.ku6.cdn.dispatcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler {
	
	private final ExecutorService es = Executors.newCachedThreadPool();
	
	public Scheduler() {}
	
}
