package com.ku6.cdn.dispatcher.common.util.builder.impl;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import com.ku6.cdn.dispatcher.common.SynTask;
import com.ku6.cdn.dispatcher.common.util.builder.Builder;

public class SynTaskBuilder implements Builder<SynTask> {

	private long _pfid = 0L;
	private long _destDiskId = 0L;
	private long _destSvrId = 0L;
	private long _srcDiskId = 0L;
	private long _srcSvrId = 0L;
	private int _svrType = 0;
	private long _altSrcDiskId = 0L;
	private long _altSrcSvrId = 0L;
	private long _priority = 0L;
	private int _opt = OPT_SYN;
	private int _status = TASK_UNKNOWN;
	private int _level = 0;
	private int _childNum = 0;
	private int _ispType = 0;
	private float _weight = -1;
	private float _altWeight = -1;
	private int _speed = 0;
	private int _altSpeed = 0;

	@Override
	public SynTask build() {
		SynTask synTask = new SynTask();
		
		synTask.setPfid(_pfid);
		synTask.setDestDiskId(_destDiskId);
		synTask.setDestSvrId(_destSvrId);
		synTask.setSrcDiskId(_srcDiskId);
		synTask.setSrcSvrId(_srcSvrId);
		synTask.setSvrType(_svrType);
		synTask.setAltSrcDiskId(_altSrcDiskId);
		synTask.setAltSrcSvrId(_altSrcSvrId);
		synTask.setPriority(_priority);
		synTask.setOpt(_opt);
		synTask.setStatus(_status);
		synTask.setLevel(_level);
		synTask.setChildNum(_childNum);
		synTask.setIspType(_ispType);
		synTask.setWeight(_weight);
		synTask.setAltWeight(_altWeight);
		synTask.setSpeed(_speed);
		synTask.setAltSpeed(_altSpeed);
		
		return synTask;
	}

	public SynTaskBuilder pfid(long pfid) {
		this._pfid = pfid;
		return this;
	}

	public SynTaskBuilder destDiskId(long destDiskId) {
		this._destDiskId = destDiskId;
		return this;
	}

	public SynTaskBuilder destSvrId(long destSvrId) {
		this._destSvrId = destSvrId;
		return this;
	}

	public SynTaskBuilder srcDiskId(long srcDiskId) {
		this._srcDiskId = srcDiskId;
		return this;
	}

	public SynTaskBuilder srcSvrId(long srcSvrId) {
		this._srcSvrId = srcSvrId;
		return this;
	}

	public SynTaskBuilder svrType(int svrType) {
		this._svrType = svrType;
		return this;
	}

	public SynTaskBuilder altSrcDiskId(long altSrcDiskId) {
		this._altSrcDiskId = altSrcDiskId;
		return this;
	}

	public SynTaskBuilder altSrcSvrId(long altSrcSvrId) {
		this._altSrcSvrId = altSrcSvrId;
		return this;
	}

	public SynTaskBuilder priority(long priority) {
		this._priority = priority;
		return this;
	}

	public SynTaskBuilder opt(int opt) {
		this._opt = opt;
		return this;
	}

	public SynTaskBuilder status(int status) {
		this._status = status;
		return this;
	}

	public SynTaskBuilder level(int level) {
		this._level = level;
		return this;
	}

	public SynTaskBuilder childNum(int childNum) {
		this._childNum = childNum;
		return this;
	}

	public SynTaskBuilder ispType(int ispType) {
		this._ispType = ispType;
		return this;
	}

	public SynTaskBuilder weight(float weight) {
		this._weight = weight;
		return this;
	}

	public SynTaskBuilder altWeight(float altWeight) {
		this._altWeight = altWeight;
		return this;
	}

	public SynTaskBuilder speed(int speed) {
		this._speed = speed;
		return this;
	}

	public SynTaskBuilder altSpeed(int altSpeed) {
		this._altSpeed = altSpeed;
		return this;
	}

}
