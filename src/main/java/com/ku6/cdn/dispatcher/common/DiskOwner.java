package com.ku6.cdn.dispatcher.common;

import com.ku6.cdn.dispatcher.common.entity.Disk;

public class DiskOwner {
	
	private Integer storeType;
	private Disk disk;
	
	public DiskOwner() {}
	
	public DiskOwner(Integer storeType, Disk disk) {
		this.storeType = storeType;
		this.disk = disk;
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
