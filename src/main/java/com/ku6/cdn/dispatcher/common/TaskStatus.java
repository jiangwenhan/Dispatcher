package com.ku6.cdn.dispatcher.common;

public class TaskStatus {
	
	private long pfid;
	private int status;
	
	public TaskStatus() {}

	public long getPfid() {
		return pfid;
	}

	public void setPfid(long pfid) {
		this.pfid = pfid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
