package com.ku6.cdn.dispatcher.common.thread;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.ku6.cdn.dispatcher.Manager;
import com.ku6.cdn.dispatcher.common.AreaNode;
import com.ku6.cdn.dispatcher.common.DiskNode;
import com.ku6.cdn.dispatcher.common.DiskOwner;
import com.ku6.cdn.dispatcher.common.GroupNode;
import com.ku6.cdn.dispatcher.common.SvrNode;
import com.ku6.cdn.dispatcher.common.entity.system.DiskInfo;
import com.ku6.cdn.dispatcher.common.entity.system.GroupInfo;
import com.ku6.cdn.dispatcher.common.entity.system.HotServer;
import com.ku6.cdn.dispatcher.common.entity.system.NodeInfo;
import com.ku6.cdn.dispatcher.common.entity.system.ServerInfo;
import com.ku6.cdn.dispatcher.common.util.Configure;
import com.ku6.cdn.dispatcher.common.util.Mappings;
import com.ku6.cdn.dispatcher.common.util.builder.impl.DiskNodeBuilder;
import com.ku6.cdn.dispatcher.common.util.builder.impl.GroupNodeBuilder;
import com.ku6.cdn.dispatcher.common.util.builder.impl.SvrNodeBuilder;

public class NodesUpdaterThread implements Runnable {
	
	private final Manager manager;
	
	public NodesUpdaterThread(Manager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		Session session = manager.getCdnSystemSessionFactory().openSession();
		updateGroupInfos(session);
		updateNodeInfos(session);
		updateServerInfos(session);
		updateHotServers(session);
		updateDiskInfos(session);
		updateGroupNodes();
		updateAreaNodes();
	}
	
	@SuppressWarnings("unchecked")
	private void updateGroupInfos(Session session) {
		Iterator<GroupInfo> iterator = session.createQuery("from GroupInfo")
											  .list()
											  .iterator();
		Map<Long, GroupInfo> groupInfos = Maps.newConcurrentMap();
		while (iterator.hasNext()) {
			GroupInfo groupInfo = iterator.next();
			if (groupInfo.getInUse() == 1) {
				groupInfos.put(groupInfo.getGroupId(), groupInfo);
			}
		}
		Mappings.GROUPS = groupInfos;
	}
	
	@SuppressWarnings("unchecked")
	private void updateNodeInfos(Session session) {
		Iterator<NodeInfo> iterator = session.createQuery("from NodeInfo")
											   .list()
											   .iterator();
		Map<Long, NodeInfo> nodeInfos = Maps.newConcurrentMap();
		Map<Long, AreaNode> hotAreaNodes = Maps.newConcurrentMap();
		Map<Long, AreaNode> coldAreaNodes = Maps.newConcurrentMap();
		while (iterator.hasNext()) {
			NodeInfo nodeInfo = iterator.next();
			if (nodeInfo.getInUse() == 1 && nodeInfo.getNeedDispatch() == 1) {
				nodeInfos.put(nodeInfo.getNodeId(), nodeInfo);
				hotAreaNodes.put(nodeInfo.getNodeId(), new AreaNode(nodeInfo.getNodeId()));
				coldAreaNodes.put(nodeInfo.getNodeId(), new AreaNode(nodeInfo.getNodeId()));
			}
		}
		Mappings.NODES = nodeInfos;
		Mappings.HOT_AREA_NODES = hotAreaNodes;
		Mappings.COLD_AREA_NODES = coldAreaNodes;
	}
	
	@SuppressWarnings("unchecked")
	private void updateServerInfos(Session session) {
		Iterator<ServerInfo> iterator = session.createQuery("from ServerInfo")
											   .list()
											   .iterator();
		Map<Long, ServerInfo> serverInfos = Maps.newConcurrentMap();
		Multimap<Long, Long> node2Server = ArrayListMultimap.create();
		Multimap<Long, Long> group2Server = ArrayListMultimap.create();
		while (iterator.hasNext()) {
			ServerInfo serverInfo = iterator.next();
			if (serverInfo.getInUse() == 1) {
				serverInfos.put(serverInfo.getSvrId(), serverInfo);
				node2Server.put(serverInfo.getNodeId(), serverInfo.getSvrId());
				group2Server.put(serverInfo.getGroupId(), serverInfo.getSvrId());
			}
		}
		Mappings.SERVERS = serverInfos;
		Mappings.NODE2SERVER = node2Server;
		Mappings.GROUP2SERVER = group2Server;
	}
	
	@SuppressWarnings("unchecked")
	private void updateHotServers(Session session) {
		Iterator<HotServer> iterator = session.createQuery("from HotServer")
											  .list()
											  .iterator();
		Map<Long, HotServer> hotServers = Maps.newConcurrentMap();
		while (iterator.hasNext()) {
			HotServer hotServer = iterator.next();
			hotServers.put(hotServer.getSvrId(), hotServer);
		}
		Mappings.HOT_SERVERS = hotServers;
	}
	
	@SuppressWarnings("unchecked")
	private void updateDiskInfos(Session session) {
		Iterator<DiskInfo> iterator = session.createQuery("from DiskInfo")
											 .list()
											 .iterator();
		Map<Long, DiskOwner> disks = Maps.newConcurrentMap();
		Multimap<Long, Long> server2Disk = ArrayListMultimap.create();
		while (iterator.hasNext()) {
			DiskInfo diskInfo = iterator.next();
			Long groupId = Mappings.SERVERS.get(diskInfo.getSvrId()).getGroupId();
			Integer groupType = Mappings.GROUPS.get(groupId).getGroupType();
			disks.put(diskInfo.getDiskId(), new DiskOwner(groupId, groupType, diskInfo));
			server2Disk.put(diskInfo.getSvrId(), diskInfo.getDiskId());
		}
		Mappings.DISKS = disks;
		Mappings.SERVER2DISK = server2Disk;
	}
	
	private void updateGroupNodes() {
		Map<Long, GroupNode> groupNodes = Maps.newConcurrentMap();
		for (Entry<Long, GroupInfo> entry : Mappings.GROUPS.entrySet()) {
			GroupInfo groupInfo = entry.getValue();
			List<SvrNode> svrList = Lists.newArrayList();
			if (entry.getKey() == 190 || entry.getKey() == 146) {
				long nodeId = entry.getKey() == 190 ? 101L : 228L;
				Iterator<Long> iterator = Mappings.NODE2SERVER.get(nodeId).iterator();
				while (iterator.hasNext()) {
					ServerInfo serverInfo = Mappings.SERVERS.get(iterator.next());
					if (serverInfo.getInUse() == COMMON_SVR_IN_USE && 
							serverInfo.getNginxStatus() == COMMON_SVR_OK &&
							serverInfo.getDispStatus() == COMMON_SVR_OK && 
							serverInfo.getSvrType() == COMMON_SVR_CDN && 
							serverInfo.getIsCache() == 0) {
						svrList.add(new SvrNodeBuilder().svrId(serverInfo.getSvrId())
														.build());
					}
				}
			} else {
				Iterator<Long> iterator = Mappings.GROUP2SERVER.get(entry.getKey()).iterator();
				while (iterator.hasNext()) {
					ServerInfo serverInfo = Mappings.SERVERS.get(iterator.next());
					NodeInfo nodeInfo = Mappings.NODES.get(serverInfo.getNodeId());
					if (nodeInfo.getInUse() == COMMON_NODE_IN_USE && 
							nodeInfo.getNeedDispatch() == COMMON_NODE_NEED_DISPATCH && 
							serverInfo.getInUse() == COMMON_SVR_IN_USE && 
							serverInfo.getNginxStatus() == COMMON_SVR_OK && 
							serverInfo.getDispStatus() == COMMON_SVR_OK && 
							serverInfo.getSvrType() == COMMON_SVR_CDN) {
						svrList.add(new SvrNodeBuilder().svrId(serverInfo.getSvrId())
														.build());
					}
				}
			}
			long minSpace = updateServerList(svrList);
			GroupNode groupNode = new GroupNodeBuilder().groupId(groupInfo.getGroupId())
														.groupType(groupInfo.getGroupType())
														.ispType(groupInfo.getIspId())
														.svrList(svrList)
														.build();
			groupNode.update(minSpace);
			groupNodes.put(entry.getKey(), groupNode);
		}
		Mappings.GROUP_NODES = groupNodes;
	}
	
	private void updateAreaNodes() {
		updateAreaNodes(Mappings.HOT_AREA_NODES, SQUARE_INIT_TYPE);
		updateAreaNodes(Mappings.COLD_AREA_NODES, LEFT_SIZE_INIT_TYPE);
	}
	
	private void updateAreaNodes(Map<Long, AreaNode> areadNodeMap, Integer type) {
		for (Entry<Long, AreaNode> entry : areadNodeMap.entrySet()) {
			Long nodeId = entry.getKey();
			NodeInfo nodeInfo = Mappings.NODES.get(nodeId);
			List<SvrNode> svrList = Lists.newArrayList();
			Iterator<Long> iterator = Mappings.NODE2SERVER.get(nodeId).iterator();
			while (iterator.hasNext()) {
				ServerInfo serverInfo = Mappings.SERVERS.get(iterator.next());
				if (nodeInfo.getInUse() == COMMON_NODE_IN_USE && 
						nodeInfo.getNeedDispatch() == COMMON_NODE_NEED_DISPATCH && 
						serverInfo.getInUse() == COMMON_SVR_IN_USE && 
						serverInfo.getNginxStatus() == COMMON_SVR_OK && 
						serverInfo.getDispStatus() == COMMON_SVR_OK && 
						serverInfo.getSvrType() == COMMON_SVR_CDN) {
					 svrList.add(new SvrNodeBuilder().svrId(serverInfo.getSvrId())
							 						 .build());
				}
			}
			long minSpace = updateServerList(svrList);
			entry.getValue().setSvrList(svrList);
			entry.getValue().update(minSpace, type);
		}
	}
	
	private long updateServerList(List<SvrNode> svrList) {
		long minSpace = Configure.DEFAULT_MIN_SIZE;
		for (SvrNode svrNode : svrList) {
			Iterator<Long> iterator = Mappings.SERVER2DISK.get(svrNode.getSvrId()).iterator();
			List<DiskNode> diskList = Lists.newArrayList();
			while (iterator.hasNext()) {
				Long diskId = iterator.next();
				DiskInfo diskInfo = Mappings.DISKS.get(diskId).getDisk();
				if (Mappings.HOT_SERVERS.containsKey(svrNode.getSvrId()) && 
						diskInfo.getIsCache() == 1 && 
						diskInfo.getInUse() == COMMON_DISK_IN_USE && 
						diskInfo.getDiskStatus() == COMMON_DISK_OK && 
						diskInfo.getDiskSize() >= diskInfo.getUsedSize() + 500) {
					DiskNode diskNode = new DiskNodeBuilder().mount(diskInfo.getMount())
															 .diskId(diskInfo.getDiskId())
															 .svrId(diskInfo.getSvrId())
															 .sizeTotal(diskInfo.getDiskSize() / 1024)
															 .sizeUsed(diskInfo.getUsedSize() / 1024)
															 .build();
					if (svrNode.getMinDiskLeftSize() > diskNode.getSizeLeft()) {
						svrNode.setMinDiskLeftSize(diskNode.getSizeLeft());
					}
					diskList.add(diskNode);
					svrNode.setDiskList(diskList);
				}
			}
			minSpace = svrNode.getMinDiskLeftSize() <= Configure.DEFAULT_MIN_SIZE ? 
					Configure.DEFAULT_MIN_SIZE : svrNode.getMinDiskLeftSize();
		}
		
		return minSpace;
	}

}
