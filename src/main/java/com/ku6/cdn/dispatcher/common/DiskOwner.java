package com.ku6.cdn.dispatcher.common;

import com.ku6.cdn.dispatcher.common.entity.Disk;

public class DiskOwner {
	
	private Long groupId;
	private Integer storeType;
	private Disk disk;
	
	public DiskOwner() {}
	
	public DiskOwner(Long groupId, Integer storeType, Disk disk) {
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

	public Disk getDisk() {
		return disk;
	}

	public void setDisk(Disk disk) {
		this.disk = disk;
	}

}
