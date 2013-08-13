package com.ku6.cdn.dispatcher.common;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.List;

public class SvrNode {
	
	private long svrId;
	private String host;
	private long lower;
	private long higher;
	private long modNum;
	private long minDiskLeftSize;
	private boolean enable;
	private List<DiskNode> diskList;

	public SvrNode(long svrId) {
		this.svrId = svrId;
		this.host = "";
		this.lower = 0;
		this.higher = 0;
		this.modNum = 0;
		this.minDiskLeftSize = -1;
		this.enable = true;
	}
	
	public void update(long startNum, long minSize, int type) {
		if (minSize == 0) {
			enable = false;
			return;
		}
		long svrScope = 0, lastStart = startNum, scope;
		for (DiskNode diskNode : diskList) {
			if (diskNode.getSizeLeft() <= MIN_DISK_SIZE) {
				diskNode.setEnable(false);
				continue;
			} else if (type == LEFT_SIZE_INIT_TYPE) {
				scope = (long) (DEFAULT_MAX_EXTEND * 
						((double)diskNode.getSizeLeft() / minSize));
			} else {
				scope = 1;
			}
			svrScope += scope;
			lower = lastStart;
			higher = lower + scope - 1;
			lastStart += scope;
		}
		if (svrScope == 0) {
			enable = false;
		} else {
			lower = startNum;
			higher = startNum + svrScope - 1;
		}
		modNum = svrScope;
	}
	
	List<DiskNode> findDisk() {
		return null;
	}

	public long getSvrId() {
		return svrId;
	}

	public void setSvrId(long svrId) {
		this.svrId = svrId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public long getLower() {
		return lower;
	}

	public void setLower(long lower) {
		this.lower = lower;
	}

	public long getHigher() {
		return higher;
	}

	public void setHigher(long higher) {
		this.higher = higher;
	}

	public long getModNum() {
		return modNum;
	}

	public void setModNum(long modNum) {
		this.modNum = modNum;
	}

	public long getMinDiskLeftSize() {
		return minDiskLeftSize;
	}

	public void setMinDiskLeftSize(long minDiskLeftSize) {
		this.minDiskLeftSize = minDiskLeftSize;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public List<DiskNode> getDiskList() {
		return diskList;
	}

	public void setDiskList(List<DiskNode> diskList) {
		this.diskList = diskList;
	}
	
}
