package com.ku6.cdn.dispatcher.common;

public class TaskTimeout {
	
	public long pfid;
	public long diskId;
	public long timeout;
	public long count;
	
	public TaskTimeout() {}

	public long getPfid() {
		return pfid;
	}

	public void setPfid(long pfid) {
		this.pfid = pfid;
	}

	public long getDiskId() {
		return diskId;
	}

	public void setDiskId(long diskId) {
		this.diskId = diskId;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
	
}
