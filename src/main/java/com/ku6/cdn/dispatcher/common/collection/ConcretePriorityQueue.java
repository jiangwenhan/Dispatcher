package com.ku6.cdn.dispatcher.common.collection;

import java.util.Comparator;

public class ConcretePriorityQueue<T> extends AbstractPriorityQueue<T> {

	public ConcretePriorityQueue(Comparator<T> comparator) {
		super(comparator);
	}
	
}
