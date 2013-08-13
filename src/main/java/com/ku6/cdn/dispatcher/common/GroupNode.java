package com.ku6.cdn.dispatcher.common;

import java.util.List;


public class GroupNode {

	private long groupId;
	private long groupType;
	private long ispType;
	private long modNum;
	private List<SvrNode> svrList;
	
	public void createTask() {
		
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getGroupType() {
		return groupType;
	}

	public void setGroupType(long groupType) {
		this.groupType = groupType;
	}

	public long getIspType() {
		return ispType;
	}

	public void setIspType(long ispType) {
		this.ispType = ispType;
	}

	public long getModNum() {
		return modNum;
	}

	public void setModNum(long modNum) {
		this.modNum = modNum;
	}

	public List<SvrNode> getSvrList() {
		return svrList;
	}

	public void setSvrList(List<SvrNode> svrList) {
		this.svrList = svrList;
	}
	
	public long getSvrNum() {
		return this.svrList.size();
	}
	
}
