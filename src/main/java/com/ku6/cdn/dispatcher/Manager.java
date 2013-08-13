package com.ku6.cdn.dispatcher;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.ku6.cdn.dispatcher.common.AreaNode;
import com.ku6.cdn.dispatcher.common.DiskOwner;
import com.ku6.cdn.dispatcher.common.DispatchTask;
import com.ku6.cdn.dispatcher.common.GroupNode;
import com.ku6.cdn.dispatcher.common.collection.DispatchTaskPriorityQueue;
import com.ku6.cdn.dispatcher.common.entity.Disk;
import com.ku6.cdn.dispatcher.common.entity.Group;
import com.ku6.cdn.dispatcher.common.entity.HotServer;
import com.ku6.cdn.dispatcher.common.entity.Node;
import com.ku6.cdn.dispatcher.common.entity.Server;
import com.ku6.cdn.dispatcher.common.util.Mappings;


@Component
public class Manager implements InitializingBean {
	
	private static SessionFactory cdnSystemSessionFactory;
	private static SessionFactory utccSessionFactory;
	
	// node maps
	private Map<Long, GroupNode> groupNodes;
	private Map<Long, AreaNode> hotAreaNodes;
	private Map<Long, AreaNode> coldAreaNodes;
	
	private final Map<Long, DispatchTask> dispatchTaskMap = Maps.newConcurrentMap();
	private final DispatchTaskPriorityQueue dispatchTaskQueue 
		= new DispatchTaskPriorityQueue(new Comparator<DispatchTask>() {

			@Override
			public int compare(DispatchTask o1, DispatchTask o2) {
				return (int)(o1.getPriority() - o2.getPriority());
			}
			
		});
	
	private final Map<Long, Long> pfidSrcSrvMap = Maps.newConcurrentMap();
	private final Map<Long, Long> pfidSrcDiskMap = Maps.newConcurrentMap();
	
	private void init() {
		initDiskMaps();
		initNodesMaps();
	}
	
	@SuppressWarnings("rawtypes")
	private void initDiskMaps() {
		Iterator iterator = cdnSystemSessionFactory
									.openSession()
									.createQuery("from HotServer "
											+ "where svrId > 0 "
											+ "and nodeId is not null "
											+ "and diskIds is not null")
									.list()
									.iterator();
		while (iterator.hasNext()) {
			HotServer hotServer = (HotServer) iterator.next();
			String[] hotDisks = hotServer.getDiskIds().split(",");
			for (String hotDisk : hotDisks) {
				Mappings.HOT_DISKS.put(Long.valueOf(hotDisk), hotServer.getNodeId());
			}
		}
		
		iterator = cdnSystemSessionFactory.getCurrentSession()
										  .createQuery("select disk, server, node, group "
										  		+ "from Disk disk, Server server, Node, Group group"
										  		+ "where disk.svrId = server.svrId "
										  		+ "and server.nodeId = node.nodeId "
										  		+ "and node.groupId = group.groupId")
										  .list()
										  .iterator();
		while (iterator.hasNext()) {
			 Object[] row = (Object[]) iterator.next();
			 Disk disk = (Disk) row[0];
			 Server server = (Server) row[1];
			 Node node = (Node) row[2];
			 Group group = (Group) row[3];
			 if (disk.getDiskStatus() == COMMON_DISK_IN_USE
					 || server.getDispStatus() == COMMON_SVR_BAD
					 || server.getNginxStatus() == COMMON_SVR_BAD
					 || server.getInUse() != COMMON_SVR_IN_USE
					 || node.getInUse() != COMMON_NODE_IN_USE
					 || node.getNeedDispatch() != COMMON_NODE_NEED_DISPATCH
					 || group.getInUse() != COMMON_GROUP_IN_USE) {
				 Mappings.BAD_DISKS.add(disk.getDiskId());
			 } else {
				 Mappings.OK_DISKS.add(disk.getDiskId());
			 }
			 Mappings.DISKS.put(disk.getDiskId(), new DiskOwner(group.getGroupType(), disk));
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void initNodesMaps() {
		Iterator iterator = cdnSystemSessionFactory.getCurrentSession()
												   .createQuery("from Group "
												   		+ "where inUse = 1 "
												   		+ "and groupType = " 
												   		+ COMMON_NORMAL_NODE_TYPE)
												   .list()
												   .iterator();
		while (iterator.hasNext()) {
			Group group = (Group) iterator.next();
			Mappings.GROUPS.put(group.getGroupId(), group);
			// TODO: need more init
		}
		
		iterator = cdnSystemSessionFactory.getCurrentSession()
				   						  .createQuery("from Node "
				   						  		+ "where inUse = "
				   								+ COMMON_NODE_IN_USE
				   								+ " and needDispatch = "
				   								+ COMMON_NODE_NEED_DISPATCH)
				   						  .list()
				   						  .iterator();
		while (iterator.hasNext()) {
			Node node = (Node) iterator.next();
			Mappings.HOT_NODES.put(node.getNodeId(), node);
			Mappings.COLD_NODES.put(node.getNodeId(), node);
			Mappings.NODE_GROUP_MAP.put(node.getNodeId(), node.getGroupId());
			// TODO: need more init
		}
	}
	
	private void initGroupNodes() {
		
	}
	
	private boolean addTask(DispatchTask task, long srcSvr, long srcDisk) {
		checkNotNull(task, "DispatchTask");
		if (task.getMd5() == null) {
			return false;
		}
		// TODO: add into syn_task
		
		pfidSrcSrvMap.put(task.getPfid(), srcSvr);
		pfidSrcDiskMap.put(task.getPfid(), srcDisk);
		
		dispatchTaskQueue.add(task);
		dispatchTaskMap.put(task.getPfid(), task);
		
		return true;
	}
	
	public static SessionFactory getCdnSystemSessionFactory() {
		return cdnSystemSessionFactory;
	}

	public static void setCdnSystemSessionFactory(
			SessionFactory cdnSystemSessionFactory) {
		Manager.cdnSystemSessionFactory = cdnSystemSessionFactory;
	}

	public static SessionFactory getUtccSessionFactory() {
		return utccSessionFactory;
	}

	public static void setUtccSessionFactory(SessionFactory utccSessionFactory) {
		Manager.utccSessionFactory = utccSessionFactory;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.init();
	}
	
}
