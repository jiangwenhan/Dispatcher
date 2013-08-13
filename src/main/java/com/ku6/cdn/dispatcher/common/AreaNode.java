package com.ku6.cdn.dispatcher.common;

import java.util.List;

public class AreaNode {

	private long areaId;
	private long modNum;
	private long leftSize;
	private long totalSize;
	private List<SvrNode> svrList;
	
	public AreaNode(long nodeId) {
		
	}
	
	public boolean init() {
		return false;
	}
	
	public void createTask() {
		
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public long getModNum() {
		return modNum;
	}

	public void setModNum(long modNum) {
		this.modNum = modNum;
	}

	public long getLeftSize() {
		return leftSize;
	}

	public void setLeftSize(long leftSize) {
		this.leftSize = leftSize;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
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
