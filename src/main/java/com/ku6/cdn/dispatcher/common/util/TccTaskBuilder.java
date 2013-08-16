package com.ku6.cdn.dispatcher.common.util;

import java.sql.Date;

import com.ku6.cdn.dispatcher.common.TccTask;


public class TccTaskBuilder implements Builder<TccTask> {
	
	private Long _taskId;
	private Long _pfid;
	private String _md5;
	private String _reportPath;
	private Long _destDiskId;
	private Long _destSvrId;
	private String _destPath;
	private Long _srcSvrId;
	private String _srcPath;
	private Long _altSvrId;
	private String _altPath;
	private Float _weight;
	private Long _expSpeed;
	private Long _realSpeed;
	private Long _altExpSpeed;
	private Long _altRealSpeed;
	private Long _priority;
	private Integer _operation;
	private Integer _count;
	private Long _level;
	private Integer _taskType;
	private Integer _taskStatus;
	private String _resultText;
	private Long _rspCode;
	private Long _rspSysErr;
	private Date _createTime;
	private Date _dispatchTime;
	private Date _completeTime;
	private Long _expectionTime;
	private Long _realTime;
	private Integer _isHot;
	
	@Override
	public TccTask build() {
		TccTask tccTask = new TccTask();
		tccTask.setTaskId(_taskId);
		tccTask.setPfid(_pfid);
		tccTask.setMd5(_md5);
		tccTask.setReportPath(_reportPath);
		tccTask.setDestDiskId(_destDiskId);
		tccTask.setDestSvrId(_destSvrId);
		tccTask.setDestPath(_destPath);
		tccTask.setSrcSvrId(_srcSvrId);
		tccTask.setSrcPath(_srcPath);
		tccTask.setAltSvrId(_altSvrId);
		tccTask.setAltPath(_altPath);
		tccTask.setWeight(_weight);
		tccTask.setExpSpeed(_expSpeed);
		tccTask.setRealSpeed(_realSpeed);
		tccTask.setAltExpSpeed(_altExpSpeed);
		tccTask.setAltRealSpeed(_altRealSpeed);
		tccTask.setPriority(_priority);
		tccTask.setOperation(_operation);
		tccTask.setCount(_count);
		tccTask.setLevel(_level);
		tccTask.setTaskType(_taskType);
		tccTask.setTaskStatus(_taskStatus);
		tccTask.setResultText(_resultText);
		tccTask.setRspCode(_rspCode);
		tccTask.setRspSysErr(_rspSysErr);
		tccTask.setCreateTime(_createTime);
		tccTask.setDispatchTime(_dispatchTime);
		tccTask.setCompleteTime(_completeTime);
		tccTask.setExpectionTime(_expectionTime);
		tccTask.setRealTime(_realTime);
		tccTask.setIsHot(_isHot);
		return tccTask;
	}
	
	public TccTaskBuilder taskId(long taskId) {
		this._taskId = taskId;
		return this;
	}
	
	public TccTaskBuilder pfid(long pfid) {
		this._pfid = pfid;
		return this;
	}
	
	public TccTaskBuilder md5(String md5) {
		this._md5 = md5;
		return this;
	}
	
	public TccTaskBuilder reportPath(String reportPath) {
		this._reportPath = reportPath;
		return this;
	}
	
	public TccTaskBuilder destDiskId(long destDiskId) {
		this._destDiskId = destDiskId;
		return this;
	}
	
	public TccTaskBuilder destSvrId(long destSvrId) {
		this._destSvrId = destSvrId;
		return this;
	}
	
	public TccTaskBuilder destPath(String destPath) {
		this._destPath = destPath;
		return this;
	}
	
	public TccTaskBuilder srcSvrId(long srcSvrId) {
		this._srcSvrId = srcSvrId;
		return this;
	}
	
	public TccTaskBuilder srcPath(String srcPath) {
		this._srcPath = srcPath;
		return this;
	}
	
	public TccTaskBuilder altSvrId(long altSvrId) {
		this._altSvrId = altSvrId;
		return this;
	}
	
	public TccTaskBuilder altPath(String altPath) {
		this._altPath = altPath;
		return this;
	}
	
	public TccTaskBuilder weight(float weight) {
		this._weight = weight;
		return this;
	}
	
	public TccTaskBuilder expSpeed(long expSpeed) {
		this._expSpeed = expSpeed;
		return this;
	}
	
	public TccTaskBuilder realSpeed(long realSpeed) {
		this._realSpeed = realSpeed;
		return this;
	}
	
	public TccTaskBuilder altExpSpeed(long altExpSpeed) {
		this._altExpSpeed = altExpSpeed;
		return this;
	}
	
	public TccTaskBuilder altRealSpeed(long altRealSpeed) {
		this._altRealSpeed = altRealSpeed;
		return this;
	}
	
	public TccTaskBuilder priority(long priority) {
		this._priority = priority;
		return this;
	}
	
	public TccTaskBuilder operation(int operation) {
		this._operation = operation;
		return this;
	}
	
	public TccTaskBuilder count(int count) {
		this._count = count;
		return this;
	}
	
	public TccTaskBuilder level(long level) {
		this._level = level;
		return this;
	}
	
	public TccTaskBuilder taskType(int taskType) {
		this._taskType = taskType;
		return this;
	}
	
	public TccTaskBuilder taskStatus(int taskStatus) {
		this._taskStatus = taskStatus;
		return this;
	}
	
	public TccTaskBuilder resultText(String resultText) {
		this._resultText = resultText;
		return this;
	}
	
	public TccTaskBuilder rspCode(long rspCode) {
		this._rspCode = rspCode;
		return this;
	}
	
	public TccTaskBuilder rspSysErr(long rspSysErr) {
		this._rspSysErr = rspSysErr;
		return this;
	}
	
	public TccTaskBuilder createTime(Date createTime) {
		this._createTime = createTime;
		return this;
	}
	
	public TccTaskBuilder dispatchTime(Date dispatchTime) {
		this._dispatchTime = dispatchTime;
		return this;
	}
	
	public TccTaskBuilder completeTime(Date completeTime) {
		this._completeTime = completeTime;
		return this;
	}
	
	public TccTaskBuilder expectionTime(long expectionTime) {
		this._expectionTime = expectionTime;
		return this;
	}
	
	public TccTaskBuilder realTime(long realTime) {
		this._realTime = realTime;
		return this;
	}
	
	public TccTaskBuilder isHot(int isHot) {
		this._isHot = isHot;
		return this;
	}
	
}
