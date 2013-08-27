package com.ku6.cdn.dispatcher.common;

import static com.ku6.cdn.dispatcher.common.Constrants.LEFT_SIZE_INIT_TYPE;

import java.util.List;

public class AreaNode {

	private long areaId;
	private long modNum;
	private long leftSize;
	private long totalSize;
	private List<SvrNode> svrList;
	
	public AreaNode(long nodeId) {
		this.areaId = nodeId;
		modNum = 0;
	}
	
	public void createTask() {
		
	}
	
	public void update(long modSize, Integer type) {
		long startNum = 1;
		for (SvrNode svrNode : svrList) {
			svrNode.update(startNum, modSize, type);
			if (svrNode.isEnable()) {
				startNum = svrNode.getHigher() + 1;
				modNum += (svrNode.getHigher() - svrNode.getLower() + 1);
			}
		}
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
