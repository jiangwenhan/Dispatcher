package com.ku6.cdn.dispatcher.common;

import com.ku6.cdn.dispatcher.common.entity.DiskInfo;

public class DiskOwner {
	
	private Long groupId;
	private Integer storeType;
	private DiskInfo disk;
	
	public DiskOwner() {}
	
	public DiskOwner(Long groupId, Integer storeType, DiskInfo disk) {
		this.groupId = groupId;
		this.storeType = storeType;
		this.disk = disk;
	}
	
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Integer getStoreType() {
		return storeType;
	}

	public void setStoreType(Integer storeType) {
		this.storeType = storeType;
	}

	public DiskInfo getDisk() {
		return disk;
	}

	public void setDisk(DiskInfo disk) {
		this.disk = disk;
	}

}
