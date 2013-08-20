package com.ku6.cdn.dispatcher.common.util;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ku6.cdn.dispatcher.common.DiskNode;
import com.ku6.cdn.dispatcher.common.SvrNode;
import com.ku6.cdn.dispatcher.common.entity.system.DiskInfo;

public class SvrNodeBuilder implements Builder<SvrNode> {
	
	private long _svrId;
	private String _host = "";
	private long _lower = 0L;
	private long _higher = 0L;
	private long _modNum = 0L;
	private long _minDiskLeftSize = -1L;
	private boolean _enable = true;
	private List<DiskNode> _diskList;
	
	public SvrNode build() {
		if (_svrId == -1) {
			return null;
		}
		SvrNode svrNode = new SvrNode(_svrId);
		Map<Long, Long> hotDiskMap = Mappings.HOT_DISK_MAPS.get(_svrId);
		Map<Long, DiskInfo> groupDiskMap = Mappings.GROUP_DISKS_MAPS.get(_svrId);
		for (Entry<Long, DiskInfo> entry : groupDiskMap.entrySet()) {
			if (entry.getValue() == null)
				continue;
			if (entry.getValue().getInUse() == COMMON_DISK_IN_USE
					&& entry.getValue().getDiskStatus() == COMMON_DISK_OK
					&& entry.getValue().getDiskSize() >= (entry.getValue().getUsedSize() + 500)
					&& hotDiskMap.containsKey(entry.getValue().getDiskId())) {
				_diskList.add(generateDiskNode(entry.getValue()));
			}
		}
		svrNode.setHost(_host);
		svrNode.setLower(_lower);
		svrNode.setHigher(_higher);
		svrNode.setModNum(_modNum);
		svrNode.setMinDiskLeftSize(_minDiskLeftSize);
		svrNode.setEnable(_enable);
			
		return svrNode;
	}
	
	private DiskNode generateDiskNode(DiskInfo disk) {
		if (disk == null)
			return null;
		DiskNode diskNode = new DiskNode();
		diskNode.setDiskId(disk.getDiskId());
		diskNode.setSizeTotal(disk.getDiskSize() / 1024);
		diskNode.setSizeUsed(disk.getUsedSize() / 1024);
		diskNode.setSizeLeft(diskNode.getSizeTotal() - diskNode.getSizeUsed());
		diskNode.setSvrId(disk.getSvrId());
		if (_minDiskLeftSize > diskNode.getSizeLeft())
			_minDiskLeftSize = diskNode.getSizeLeft();
		if (disk.getMount() != null)
			diskNode.setMount(disk.getMount());
		return diskNode;
	}

	public SvrNodeBuilder svrId(long svrId) {
		this._svrId = svrId;
		return this;
	}
	
	public SvrNodeBuilder host(String host) {
		this._host = host;
		return this;
	}
	
	public SvrNodeBuilder lower(long lower) {
		this._lower = lower;
		return this;
	}
	
	public SvrNodeBuilder higher(long higher) {
		this._higher = higher;
		return this;
	}
	
	public SvrNodeBuilder modNum(long modNum) {
		this._modNum = modNum;
		return this;
	}
	
	public SvrNodeBuilder minDiskLeftSize(long minDiskLeftSize) {
		this._minDiskLeftSize = minDiskLeftSize;
		return this;
	}
	
	public SvrNodeBuilder enable(boolean enable) {
		this._enable = enable;
		return this;
	}
	
	public SvrNodeBuilder diskList(List<DiskNode> diskList) {
		this._diskList = diskList;
		return this;
	}
	
}
