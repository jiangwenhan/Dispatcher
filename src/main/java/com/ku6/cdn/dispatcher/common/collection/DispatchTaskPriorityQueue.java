package com.ku6.cdn.dispatcher.common.collection;

import java.util.Comparator;

import com.ku6.cdn.dispatcher.common.DispatchTask;

public class DispatchTaskPriorityQueue extends AbstractPriorityQueue<DispatchTask> {

	public DispatchTaskPriorityQueue(Comparator<DispatchTask> comparator) {
		super(comparator);
	}
	
}
