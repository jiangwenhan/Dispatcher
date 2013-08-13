package com.ku6.cdn.dispatcher.common;

public class TimeTask {
	
	private long taskId;
	private String path;
	private long sid;
	private long vid;
	private int type;
	private int priority;
	private int status;
	private int ri;
	private int diskBmp;
	private String rv;
	private long runTs;
	private long startTime;
	private long endTime;
	public int delay;
	boolean bRunstat;
	
	public TimeTask() {
		this.taskId = -1;
		this.path = "";
		this.delay = 1;
		this.bRunstat = false;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getSid() {
		return sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}

	public long getVid() {
		return vid;
	}

	public void setVid(long vid) {
		this.vid = vid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRi() {
		return ri;
	}

	public void setRi(int ri) {
		this.ri = ri;
	}

	public int getDiskBmp() {
		return diskBmp;
	}

	public void setDiskBmp(int diskBmp) {
		this.diskBmp = diskBmp;
	}

	public String getRv() {
		return rv;
	}

	public void setRv(String rv) {
		this.rv = rv;
	}

	public long getRunTs() {
		return runTs;
	}

	public void setRunTs(long runTs) {
		this.runTs = runTs;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public boolean isbRunstat() {
		return bRunstat;
	}

	public void setbRunstat(boolean bRunstat) {
		this.bRunstat = bRunstat;
	}
	
}
