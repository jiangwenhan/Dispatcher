package com.ku6.cdn.dispatcher.common;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

public class SynTask {
	
	private long pfid;
	private long destDiskId;
	private long destSvrId;
	private long srcDiskId;
	private long srcSvrId;
	private int svrType;
	private long altSrcDiskId;
	private long altSrcSvrId;
	private long priority;
	private int opt;
	private int status;
	private int level;
	private int childNum;
	private int ispType;
	private float weight;
	private float altWeight;
	private int speed;
	private int altSpeed;

	public SynTask() {}

	public long getPfid() {
		return pfid;
	}

	public void setPfid(long pfid) {
		this.pfid = pfid;
	}

	public long getDestDiskId() {
		return destDiskId;
	}

	public void setDestDiskId(long destDiskId) {
		this.destDiskId = destDiskId;
	}

	public long getDestSvrId() {
		return destSvrId;
	}

	public void setDestSvrId(long destSvrId) {
		this.destSvrId = destSvrId;
	}

	public long getSrcDiskId() {
		return srcDiskId;
	}

	public void setSrcDiskId(long srcDiskId) {
		this.srcDiskId = srcDiskId;
	}

	public long getSrcSvrId() {
		return srcSvrId;
	}

	public void setSrcSvrId(long srcSvrId) {
		this.srcSvrId = srcSvrId;
	}

	public int getSvrType() {
		return svrType;
	}

	public void setSvrType(int svrType) {
		this.svrType = svrType;
	}

	public long getAltSrcDiskId() {
		return altSrcDiskId;
	}

	public void setAltSrcDiskId(long altSrcDiskId) {
		this.altSrcDiskId = altSrcDiskId;
	}

	public long getAltSrcSvrId() {
		return altSrcSvrId;
	}

	public void setAltSrcSvrId(long altSrcSvrId) {
		this.altSrcSvrId = altSrcSvrId;
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public int getOpt() {
		return opt;
	}

	public void setOpt(int opt) {
		this.opt = opt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getChildNum() {
		return childNum;
	}

	public void setChildNum(int childNum) {
		this.childNum = childNum;
	}

	public int getIspType() {
		return ispType;
	}

	public void setIspType(int ispType) {
		this.ispType = ispType;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getAltWeight() {
		return altWeight;
	}

	public void setAltWeight(float altWeight) {
		this.altWeight = altWeight;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getAltSpeed() {
		return altSpeed;
	}

	public void setAltSpeed(int altSpeed) {
		this.altSpeed = altSpeed;
	}
	
}
