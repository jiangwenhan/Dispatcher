package com.ku6.cdn.dispatcher.common.collection;

public interface PriorityQueue<T> {

	public boolean add(T object);
	
	public T peekFirst();
	
	public T peekLast();
	
	public T peek();
	
}
