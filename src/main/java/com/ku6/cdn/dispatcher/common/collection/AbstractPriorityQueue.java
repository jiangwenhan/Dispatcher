package com.ku6.cdn.dispatcher.common.collection;

import java.util.Comparator;

import com.google.common.collect.MinMaxPriorityQueue;

public abstract class AbstractPriorityQueue<T> implements PriorityQueue<T> {
	
	protected MinMaxPriorityQueue<T> minMaxHeap;

	protected AbstractPriorityQueue(Comparator<T> comparator) {
		this.minMaxHeap = MinMaxPriorityQueue.orderedBy(comparator).create();
	}
	
	public boolean add(T t) {
		return minMaxHeap.add(t);
	}
	
	public T peekFirst() {
		return minMaxHeap.peekFirst();
	}
	
	public T peekLast() {
		return minMaxHeap.peekLast();
	}
	
	public T peek() {
		return minMaxHeap.peek();
	}
	
}
