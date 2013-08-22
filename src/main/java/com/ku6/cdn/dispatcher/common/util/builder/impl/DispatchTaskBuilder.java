package com.ku6.cdn.dispatcher.common.util.builder.impl;

import java.util.Set;

import com.ku6.cdn.dispatcher.common.DispatchTask;
import com.ku6.cdn.dispatcher.common.util.builder.Builder;

public class DispatchTaskBuilder implements Builder<DispatchTask> {
	
	private long _pfid = 0;
	private String _md5;
	private long _priority = 0L;
	private int _num = 0;
	private int _taskType = 0;
	private int _opt = 0;
	private String _pfname;
	private boolean _uniqIsp = false;
	private long _timeout = 0L;
	private long _mod = 1L;
	private long _taskNum = 0L;
	private Set<Long> _badSrc;

	@Override
	public DispatchTask build() {
		DispatchTask dispatchTask = new DispatchTask();
		dispatchTask.setPfid(_pfid);
		dispatchTask.setMd5(_md5);
		dispatchTask.setPriority(_priority);
		dispatchTask.setNum(_num);
		dispatchTask.setTaskType(_taskType);
		dispatchTask.setOpt(_opt);
		dispatchTask.setPfname(_pfname);
		dispatchTask.setUniqIsp(_uniqIsp);
		dispatchTask.setTimeout(_timeout);
		dispatchTask.setMod(_mod);
		dispatchTask.setTaskNum(_taskNum);
		dispatchTask.setBadSrc(_badSrc);
		return dispatchTask;
	}
	
	public DispatchTaskBuilder pfid(long pfid) {
		this._pfid = pfid;
		return this;
	}
	
	public DispatchTaskBuilder md5(String md5) {
		this._md5 = md5;
		return this;
	}
	
	public DispatchTaskBuilder priority(long priority) {
		this._priority = priority;
		return this;
	}
	
	public DispatchTaskBuilder num(int num) {
		this._num = num;
		return this;
	}
	
	public DispatchTaskBuilder taskType(int taskType) {
		this._taskType = taskType;
		return this;
	}
	
	public DispatchTaskBuilder opt(int opt) {
		this._opt = opt;
		return this;
	}
	
	public DispatchTaskBuilder pfname(String pfname) {
		this._pfname = pfname;
		return this;
	}
	
	public DispatchTaskBuilder uniqIsp(boolean uniqIsp) {
		this._uniqIsp = uniqIsp;
		return this;
	}
	
	public DispatchTaskBuilder timeout(long timeout) {
		this._timeout = timeout;
		return this;
	}
	
	public DispatchTaskBuilder mod(long mod) {
		this._mod = mod;
		return this;
	}
	
	public DispatchTaskBuilder taskNum(long taskNum) {
		this._taskNum = taskNum;
		return this;
	}
	
	public DispatchTaskBuilder badSrc(Set<Long> badSrc) {
		this._badSrc = badSrc;
		return this;
	}

}
