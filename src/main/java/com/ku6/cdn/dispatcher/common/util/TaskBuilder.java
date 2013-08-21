package com.ku6.cdn.dispatcher.common.util;

import com.ku6.cdn.dispatcher.common.Task;

public class TaskBuilder implements Builder<Task> {
	
	private long _vid = 0;
	private long _taskId = 0;
	private long _size = 0;
	private int _priority = 0;
	private String _md5;
	private String _host;
	private String _path;

	@Override
	public Task build() {
		Task task = new Task();
		task.setVid(_vid);
		task.setTaskId(_taskId);
		task.setSize(_size);
		task.setPriority(_priority);
		task.setMd5(_md5);
		task.setHost(_host);
		task.setPath(_path);
		return task;
	}
	
	public TaskBuilder vid(long vid) {
		this._vid = vid;
		return this;
	}
	
	public TaskBuilder taskId(long taskId) {
		this._taskId = taskId;
		return this;
	}
	
	public TaskBuilder size(long size) {
		this._size = size;
		return this;
	}
	
	public TaskBuilder priority(int priority) {
		this._priority = priority;
		return this;
	}
	
	public TaskBuilder md5(String md5) {
		this._md5 = md5;
		return this;
	}
	
	public TaskBuilder host(String host) {
		this._host = host;
		return this;
	}
	
	public TaskBuilder path(String path) {
		this._path = path;
		return this;
	}

}
