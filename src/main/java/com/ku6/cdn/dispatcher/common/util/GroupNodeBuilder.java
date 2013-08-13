package com.ku6.cdn.dispatcher.common.util;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.ku6.cdn.dispatcher.common.GroupNode;
import com.ku6.cdn.dispatcher.common.SvrNode;
import com.ku6.cdn.dispatcher.common.entity.Group;
import com.ku6.cdn.dispatcher.common.entity.Server;

public class GroupNodeBuilder implements Builder<GroupNode> {
	
	private long _groupId;
	private long _groupType = -1L;
	private long _ispType = 0L;
	private long _modNum = 0L;
	private List<SvrNode> _svrList;

	@Override
	public GroupNode build() {
		if (!Mappings.GROUPS.containsKey(_groupId)) {
			return null;
		}
		GroupNode groupNode = new GroupNode();
		Group group = Mappings.GROUPS.get(_groupId);
		_groupType = group.getGroupType();
		_ispType = group.getIspId();
		generateSvrNodeList(_groupId);
		long minSpace = MIN_DISK_SIZE;
		Iterator<SvrNode> iterator = _svrList.iterator();
		while (iterator.hasNext()) {
			long leftSize = iterator.next().getMinDiskLeftSize();
			if (leftSize <= MIN_DISK_SIZE && minSpace > MIN_DISK_SIZE) {
				minSpace = MIN_DISK_SIZE;
			} else if (leftSize > MIN_DISK_SIZE) {
				minSpace = leftSize;
			}
		}
		return groupNode;
	}
	
	private void generateSvrNodeList(long groupId) {
		for (Entry<Long, Server> entry : Mappings.SERVERS.entrySet()) {
			Server server = entry.getValue();
			if (server.getInUse() == COMMON_SVR_IN_USE
					&& server.getNginxStatus() == COMMON_SVR_OK
					&& server.getDispStatus() == COMMON_SVR_OK
					&& server.getSvrType() == COMMON_SVR_CDN
					&& server.getIsCache() == 0) {
				if ((server.getGroupId() == 190 && server.getNodeId() == 101)
						|| (server.getGroupId() == 146 && server.getNodeId() == 228)) {
					_svrList.add(new SvrNodeBuilder()
										.svrId(server.getSvrId())
										.build());
				}
			}
		}
	}

	public GroupNodeBuilder groupId(long groupId) {
		this._groupId = groupId;
		return this;
	}
	
	public GroupNodeBuilder groupType(long groupType){
		this._groupType = groupType;
		return this;
	}
	
	public GroupNodeBuilder ispType(long ispType) {
		this._ispType = ispType;
		return this;
	}
	
	public GroupNodeBuilder modNum(long modNum) {
		this._modNum = modNum;
		return this;
	}

}
