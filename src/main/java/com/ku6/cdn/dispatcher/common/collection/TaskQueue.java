package com.ku6.cdn.dispatcher.common.collection;

import java.util.Queue;

import com.google.common.collect.Queues;
import com.ku6.cdn.dispatcher.common.Task;


public final class TaskQueue {

	private static final Queue<Task> TASKS = Queues.newConcurrentLinkedQueue();

	public static boolean push(Task task) {
		return TaskQueue.TASKS.add(task);
	}

	public static Task front() {
		return TaskQueue.TASKS.peek();
	}

	public static boolean pop() {
		if (TaskQueue.TASKS.size() > 0) {
			synchronized (TaskQueue.TASKS) {
				if (TaskQueue.TASKS.poll() == null)
					return false;
			}
		}
		return true;
	}
	
	public static Task poll() {
		return TaskQueue.TASKS.poll();
	}
	
	public static void clear() {
		TaskQueue.TASKS.clear();
	}
	
	public static boolean isEmpty() {
		return TaskQueue.TASKS.isEmpty();
	}

}
