package com.ku6.cdn.dispatcher.common.collection;

import java.util.concurrent.ConcurrentHashMap;

import com.ku6.cdn.dispatcher.common.SynTask;

public class FidSynTaskMap extends ConcurrentHashMap<Long, SynTask> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FidSynTaskMap() {
		super();
	}
	
}
