package com.ku6.cdn.dispatcher.common.util;

import com.ku6.cdn.dispatcher.common.TaskStatus;

public class TaskStatusBuilder implements Builder<TaskStatus> {
	
	private long _pfid;
	private int _status;
	
	@Override
	public TaskStatus build() {
		TaskStatus taskStatus = new TaskStatus();
		taskStatus.setPfid(_pfid);
		taskStatus.setStatus(_status);
		return taskStatus;
	}
	
	public TaskStatusBuilder pfid(long pfid) {
		this._pfid = pfid;
		return this;
	}
	
	public TaskStatusBuilder status(int status) {
		this._status = status;
		return this;
	}
	
}
