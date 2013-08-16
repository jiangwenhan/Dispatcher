package com.ku6.cdn.dispatcher.common;

import java.sql.Date;

public class TccTask {

	private long taskId;
	private long pfid;
	private String md5;
	private String reportPath;
	private long destDiskId;
	private long destSvrId;
	private String destPath;
	private long srcSvrId;
	private String srcPath;
	private long altSvrId;
	private String altPath;
	private float weight;
	private long expSpeed;
	private long realSpeed;
	private long altExpSpeed;
	private long altRealSpeed;
	private long priority;
	private int operation;
	private int count;
	private long level;
	private int taskType;
	private int taskStatus;
	private String resultText;
	private long rspCode;
	private long rspSysErr;
	private Date createTime;
	private Date dispatchTime;
	private Date completeTime;
	private long expectionTime;
	private long realTime;
	private int isHot;
	
	public TccTask() {}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public long getPfid() {
		return pfid;
	}

	public void setPfid(long pfid) {
		this.pfid = pfid;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
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

	public String getDestPath() {
		return destPath;
	}

	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

	public long getSrcSvrId() {
		return srcSvrId;
	}

	public void setSrcSvrId(long srcSvrId) {
		this.srcSvrId = srcSvrId;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public long getAltSvrId() {
		return altSvrId;
	}

	public void setAltSvrId(long altSvrId) {
		this.altSvrId = altSvrId;
	}

	public String getAltPath() {
		return altPath;
	}

	public void setAltPath(String altPath) {
		this.altPath = altPath;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public long getExpSpeed() {
		return expSpeed;
	}

	public void setExpSpeed(long expSpeed) {
		this.expSpeed = expSpeed;
	}

	public long getRealSpeed() {
		return realSpeed;
	}

	public void setRealSpeed(long realSpeed) {
		this.realSpeed = realSpeed;
	}

	public long getAltExpSpeed() {
		return altExpSpeed;
	}

	public void setAltExpSpeed(long altExpSpeed) {
		this.altExpSpeed = altExpSpeed;
	}

	public long getAltRealSpeed() {
		return altRealSpeed;
	}

	public void setAltRealSpeed(long altRealSpeed) {
		this.altRealSpeed = altRealSpeed;
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getLevel() {
		return level;
	}

	public void setLevel(long level) {
		this.level = level;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getResultText() {
		return resultText;
	}

	public void setResultText(String resultText) {
		this.resultText = resultText;
	}

	public long getRspCode() {
		return rspCode;
	}

	public void setRspCode(long rspCode) {
		this.rspCode = rspCode;
	}

	public long getRspSysErr() {
		return rspSysErr;
	}

	public void setRspSysErr(long rspSysErr) {
		this.rspSysErr = rspSysErr;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public long getExpectionTime() {
		return expectionTime;
	}

	public void setExpectionTime(long expectionTime) {
		this.expectionTime = expectionTime;
	}

	public long getRealTime() {
		return realTime;
	}

	public void setRealTime(long realTime) {
		this.realTime = realTime;
	}

	public int getIsHot() {
		return isHot;
	}

	public void setIsHot(int isHot) {
		this.isHot = isHot;
	}
	
}
