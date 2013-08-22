package com.ku6.cdn.dispatcher.common.util.builder.impl;

import com.ku6.cdn.dispatcher.common.TaskTimeout;
import com.ku6.cdn.dispatcher.common.util.builder.Builder;

public class TaskTimeoutBuilder implements Builder<TaskTimeout> {
	
	public long _pfid = 0L;
	public long _diskId = 0L;
	public long _timeout = 0L;
	public long _count = 0L;

	@Override
	public TaskTimeout build() {
		TaskTimeout taskTimeout = new TaskTimeout();
		taskTimeout.setPfid(_pfid);
		taskTimeout.setDiskId(_diskId);
		taskTimeout.setTimeout(_timeout);
		taskTimeout.setCount(_count);
		return taskTimeout;
	}
	
	public TaskTimeoutBuilder pfid(long pfid) {
		this._pfid = pfid;
		return this;
	}
	
	public TaskTimeoutBuilder diskId(long diskId) {
		this._diskId = diskId;
		return this;
	}
	
	public TaskTimeoutBuilder timeout(long timeout) {
		this._timeout = timeout;
		return this;
	}
	
	public TaskTimeoutBuilder count(long count) {
		this._count = count;
		return this;
	}

}
