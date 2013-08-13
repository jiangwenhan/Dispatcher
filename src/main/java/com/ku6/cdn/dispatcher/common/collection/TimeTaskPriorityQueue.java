package com.ku6.cdn.dispatcher.common.collection;

import java.util.Comparator;

import com.ku6.cdn.dispatcher.common.TimeTask;

public class TimeTaskPriorityQueue extends AbstractPriorityQueue<TimeTask> {
	
	protected TimeTaskPriorityQueue(Comparator<TimeTask> comparator) {
		super(comparator);
	}
	
}
