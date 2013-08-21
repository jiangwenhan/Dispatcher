package com.ku6.cdn.dispatcher.common.util;

import com.ku6.cdn.dispatcher.common.TimeTask;

public class TimeTaskBuilder implements Builder<TimeTask> {
	
	private long _taskId = 0;
	private String _path = "";
	private long _sid = 0;
	private long _vid = 0;
	private int _type = 0;
	private int _priority = 0;
	private int _status = 0;
	private int _ri = 0;
	private int _diskBmp = 0;
	private String _rv = "";
	private long _runTs = 0;
	private long _startTime = 0;
	private long _endTime = 0;
	private int _delay = 0;
	private boolean _bRunstat = false;

	@Override
	public TimeTask build() {
		TimeTask timeTask = new TimeTask();
		timeTask.setTaskId(_taskId);
		timeTask.setPath(_path);
		timeTask.setSid(_sid);
		timeTask.setVid(_vid);
		timeTask.setType(_type);
		timeTask.setPriority(_priority);
		timeTask.setStatus(_status);
		timeTask.setRi(_ri);
		timeTask.setDiskBmp(_diskBmp);
		timeTask.setRv(_rv);
		timeTask.setRunTs(_runTs);
		timeTask.setStartTime(_startTime);
		timeTask.setEndTime(_endTime);
		timeTask.setDelay(_delay);
		timeTask.setbRunstat(_bRunstat);
		return timeTask;
	}
	
	public TimeTaskBuilder taskId(long taskId) {
		this._taskId = taskId;
		return this;
	}
	
	public TimeTaskBuilder path(String path) {
		this._path = path;
		return this;
	}
	
	public TimeTaskBuilder sid(long sid) {
		this._sid = sid;
		return this;
	}
	
	public TimeTaskBuilder vid(long vid) {
		this._vid = vid;
		return this;
	}
	
	public TimeTaskBuilder type(int type) {
		this._type = type;
		return this;
	}
	
	public TimeTaskBuilder priority(int priority) {
		this._priority = priority;
		return this;
	}
	
	public TimeTaskBuilder status(int status) {
		this._status = status;
		return this;
	}
	
	public TimeTaskBuilder ri(int ri) {
		this._ri = ri;
		return this;
	}
	
	public TimeTaskBuilder diskBmp(int diskBmp) {
		this._diskBmp = diskBmp;
		return this;
	}
	
	public TimeTaskBuilder rv(String rv) {
		this._rv = rv;
		return this;
	}
	
	public TimeTaskBuilder runTs(long runTs) {
		this._runTs = runTs;
		return this;
	}
	
	public TimeTaskBuilder startTime(long startTime) {
		this._startTime = startTime;
		return this;
	}
	
	public TimeTaskBuilder endTime(long endTime) {
		this._endTime = endTime;
		return this;
	}
	
	public TimeTaskBuilder delay(int delay) {
		this._delay = delay;
		return this;
	}
	
	public TimeTaskBuilder bRunstat(boolean bRunstat) {
		this._bRunstat = bRunstat;
		return this;
	}

}
