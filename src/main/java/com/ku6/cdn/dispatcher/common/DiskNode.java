package com.ku6.cdn.dispatcher.common;

public class DiskNode {
	
	private String mount;
	private long diskId;
	private long svrId;
	private long lower;
	private long higher;
	private long iNodeLeft;
	private long sizeTotal;
	private long sizeUsed;
	private long sizeLeft;
	private boolean enable;
	
	public DiskNode() {
		this.mount = "";
		this.diskId = 0;
		this.svrId = 0;
		this.lower = 0;
		this.higher = 0;
		this.iNodeLeft = 0;
		this.sizeTotal = 0;
		this.sizeUsed = 0;
		this.sizeLeft = 0;
		this.enable = true;
	}

	public String getMount() {
		return mount;
	}

	public void setMount(String mount) {
		this.mount = mount;
	}

	public long getDiskId() {
		return diskId;
	}

	public void setDiskId(long diskId) {
		this.diskId = diskId;
	}

	public long getSvrId() {
		return svrId;
	}

	public void setSvrId(long svrId) {
		this.svrId = svrId;
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

	public long getiNodeLeft() {
		return iNodeLeft;
	}

	public void setiNodeLeft(long iNodeLeft) {
		this.iNodeLeft = iNodeLeft;
	}

	public long getSizeTotal() {
		return sizeTotal;
	}

	public void setSizeTotal(long sizeTotal) {
		this.sizeTotal = sizeTotal;
	}

	public long getSizeUsed() {
		return sizeUsed;
	}

	public void setSizeUsed(long sizeUsed) {
		this.sizeUsed = sizeUsed;
	}

	public long getSizeLeft() {
		return sizeLeft;
	}

	public void setSizeLeft(long sizeLeft) {
		this.sizeLeft = sizeLeft;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
