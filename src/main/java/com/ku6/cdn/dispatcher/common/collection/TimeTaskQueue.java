package com.ku6.cdn.dispatcher.common.collection;

import java.util.Queue;

import com.google.common.collect.Queues;
import com.ku6.cdn.dispatcher.common.TimeTask;

public class TimeTaskQueue {
	
	private static final Queue<TimeTask> TIME_TASKS = Queues.newConcurrentLinkedQueue();

	public static boolean push(TimeTask timeTask) {
		return TimeTaskQueue.TIME_TASKS.add(timeTask);
	}

	public static TimeTask front() {
		return TimeTaskQueue.TIME_TASKS.peek();
	}

	public static boolean pop() {
		if (TimeTaskQueue.TIME_TASKS.size() > 0) {
			synchronized (TimeTaskQueue.TIME_TASKS) {
				if (TimeTaskQueue.TIME_TASKS.poll() == null)
					return false;
			}
		}
		return true;
	}
	
	public static TimeTask poll() {
		return TimeTaskQueue.TIME_TASKS.poll();
	}
	
	public static void clear() {
		TimeTaskQueue.TIME_TASKS.clear();
	}
	
	public static boolean isEmpty() {
		return TimeTaskQueue.TIME_TASKS.isEmpty();
	}
	
}
