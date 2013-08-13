package com.ku6.cdn.dispatcher.common;

import java.util.Set;

public class DispatchTask {
	
	private long pfid;
	private String md5;
	private long priority;
	private int num;
	private int taskType;
	private int opt;
	private String pfname;
	private boolean uniqIsp;
	private long timeout;
	private long mod;
	private long taskNum;
	private Set<Long> badSrc;
	
	public DispatchTask() {}

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

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getOpt() {
		return opt;
	}

	public void setOpt(int opt) {
		this.opt = opt;
	}

	public String getPfname() {
		return pfname;
	}

	public void setPfname(String pfname) {
		this.pfname = pfname;
	}

	public boolean isUniqIsp() {
		return uniqIsp;
	}

	public void setUniqIsp(boolean uniqIsp) {
		this.uniqIsp = uniqIsp;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getMod() {
		return mod;
	}

	public void setMod(long mod) {
		this.mod = mod;
	}

	public long getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(long taskNum) {
		this.taskNum = taskNum;
	}

	public Set<Long> getBadSrc() {
		return badSrc;
	}

	public void setBadSrc(Set<Long> badSrc) {
		this.badSrc = badSrc;
	}

}
