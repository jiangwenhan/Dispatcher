package com.ku6.cdn.dispatcher;

import java.util.List;

import com.ku6.cdn.dispatcher.common.SynTask;


public class SourceSelector {
	
	private long childNum;
	private long confLimitSpeed;
	
	public SourceSelector() {}

	public boolean FindSrc(List<SynTask> destTask, List<SynTask> srcTask, boolean uniqueIsp) { return true; }
	
	public boolean FindSrc(List<SynTask> destTask, List<SynTask> srcTask) { 
		return FindSrc(destTask, srcTask, true); 
	}

	public long getChildNum() {
		return childNum;
	}

	public void setChildNum(long childNum) {
		this.childNum = childNum;
	}

	public long getConfLimitSpeed() {
		return confLimitSpeed;
	}

	public void setConfLimitSpeed(long confLimitSpeed) {
		this.confLimitSpeed = confLimitSpeed;
	}
	
}
