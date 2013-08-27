package com.ku6.cdn.dispatcher.common.util.builder.impl;

import com.ku6.cdn.dispatcher.common.DiskNode;
import com.ku6.cdn.dispatcher.common.util.builder.Builder;

public class DiskNodeBuilder implements Builder<DiskNode> {
	
	private String _mount;
	private long _diskId = 0;
	private long _svrId = 0;
	private long _lower = 0;
	private long _higher = 0;
	private long _iNodeLeft = 0;
	private long _sizeTotal = 0;
	private long _sizeUsed = 0;
	private boolean _enable = false;

	@Override
	public DiskNode build() {
		DiskNode diskNode = new DiskNode();
		diskNode.setMount(_mount);
		diskNode.setDiskId(_diskId);
		diskNode.setSvrId(_svrId);
		diskNode.setLower(_lower);
		diskNode.setHigher(_higher);
		diskNode.setiNodeLeft(_iNodeLeft);
		diskNode.setSizeTotal(_sizeTotal);
		diskNode.setSizeUsed(_sizeUsed);
		diskNode.setSizeLeft(_sizeTotal - _sizeUsed);
		diskNode.setEnable(_enable);
		return diskNode;
	}
	
	public DiskNodeBuilder mount(String mount) {
		this._mount = mount;
		return this;
	}
	
	public DiskNodeBuilder diskId(long diskId) {
		this._diskId = diskId;
		return this;
	}
	
	public DiskNodeBuilder svrId(long svrId) {
		this._svrId = svrId;
		return this;
	}
	
	public DiskNodeBuilder lower(long lower) {
		this._lower = lower;
		return this;
	}
	
	public DiskNodeBuilder higher(long higher) {
		this._higher = higher;
		return this;
	}
	
	public DiskNodeBuilder iNodeLeft(long iNodeLeft) {
		this._iNodeLeft = iNodeLeft;
		return this;
	}
	
	public DiskNodeBuilder sizeTotal(long sizeTotal) {
		this._sizeTotal = sizeTotal;
		return this;
	}
	
	public DiskNodeBuilder sizeUsed(long sizeUsed) {
		this._sizeUsed = sizeUsed;
		return this;
	}
	
	public DiskNodeBuilder enable(boolean enable) {
		this._enable = enable;
		return this;
	}

}
