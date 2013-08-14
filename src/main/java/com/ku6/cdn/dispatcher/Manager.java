package com.ku6.cdn.dispatcher;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ku6.cdn.dispatcher.common.AreaNode;
import com.ku6.cdn.dispatcher.common.DiskOwner;
import com.ku6.cdn.dispatcher.common.DispatchTask;
import com.ku6.cdn.dispatcher.common.GroupNode;
import com.ku6.cdn.dispatcher.common.SynTask;
import com.ku6.cdn.dispatcher.common.collection.DispatchTaskPriorityQueue;
import com.ku6.cdn.dispatcher.common.collection.FidDiskIdPair;
import com.ku6.cdn.dispatcher.common.collection.FidSynTaskMap;
import com.ku6.cdn.dispatcher.common.entity.Disk;
import com.ku6.cdn.dispatcher.common.entity.Group;
import com.ku6.cdn.dispatcher.common.entity.HotServer;
import com.ku6.cdn.dispatcher.common.entity.Node;
import com.ku6.cdn.dispatcher.common.entity.Server;
import com.ku6.cdn.dispatcher.common.thread.TaskConsumerCallable;
import com.ku6.cdn.dispatcher.common.util.Mappings;
import com.ku6.cdn.dispatcher.common.util.SynTaskBuilder;


@Component
public class Manager implements InitializingBean {
	
	private static SessionFactory cdnSystemSessionFactory;
	private static SessionFactory utccSessionFactory;
	
	private final ExecutorService es = Executors.newFixedThreadPool(20);
	
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
	
	private final Map<Long, FidSynTaskMap> waitSrcMap = Maps.newConcurrentMap();
	private final Map<Long, FidSynTaskMap> waitDispatchMap = Maps.newConcurrentMap();
	private final Map<Long, FidSynTaskMap> completeMap = Maps.newConcurrentMap();
	private final Map<Long, Set<Long>> storeDiskMap = Maps.newConcurrentMap();
	
	private final Map<Long, Long> pfidSrcSrvMap = Maps.newConcurrentMap();
	private final Map<Long, Long> pfidSrcDiskMap = Maps.newConcurrentMap();
	private final Map<Long, Map<Long, SynTask>> map = Maps.newConcurrentMap();
	
	public Manager() {
		init();
		run();
	}
	
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
	
	public boolean createSynTask(DispatchTask task, List<SynTask> retTasks) {
		Map<Long, Set<Long>> mapSet = getSvrMap(task, task.getTaskType());
		return createSynTask(task, mapSet, retTasks);
	}
	
	public boolean deleteSynTask(DispatchTask task) {
		return false;
	}
	
	public boolean checkMap(long pfid) {
		return false;
	}
	
	public boolean doTask(long pfid, int type) {
		return false;
	}
	
	private boolean createSynTask(DispatchTask task, Map<Long, Set<Long>> mapSet, List<SynTask> retTasks) {
		int iCount = 0;
		if (task.getTaskType() == COMMON_NODE_HOT || task.getTaskType() == COMMON_NODE_COLD) {
			Map<Long, AreaNode> areaNodes = 
					task.getTaskType() == COMMON_NODE_HOT ? hotAreaNodes : coldAreaNodes;
			for (Entry<Long, AreaNode> entry : areaNodes.entrySet()) {
				List<SynTask> synTasks = null;
				int num = task.getNum() - mapSet.get(entry.getValue().getAreaId()).size();
				if (num <= 0) {
					++iCount;
				} else {
					// TODO: Create Task
					entry.getValue().createTask();
				}
				if (synTasks == null || synTasks.size() == 0) {
					continue;
				}
				retTasks.addAll(synTasks);
				if (iCount == coldAreaNodes.size())
					return true;
			}
		} else if (task.getTaskType() == COMMON_GROUP_COLD) {
			
		}
		return false;
	}
	
	private Map<Long, Set<Long>> getSvrMap(DispatchTask task, int type) {
		String nodeInfo = getValueFromMemcached(StringUtils.stripStart(task.getPfname(), "/"));
		String[] strings = nodeInfo.split(",");
		for (String each : strings) {
			String[] eachItems = each.split(":");
			long nodeId = Long.valueOf(eachItems[0]);
			String[] disks = eachItems[1].split("/");
			for (String disk : disks) {
				long diskId = nodeId * 10000L + Long.valueOf(disk);
				if (Mappings.DISKS.containsKey(diskId) 
						&& Mappings.OK_DISKS.contains(diskId)) {
					// TODO: insert to result map
					SynTask synTask = new SynTaskBuilder()
										.pfid(task.getPfid())
										.opt(task.getOpt())
										.destDiskId(diskId)
										.destSvrId(Mappings.DISKS.get(diskId).getDisk().getSvrId())
										.priority(task.getPriority())
										.status(TASK_COMPLETE)
										.childNum(0)
										.weight(-1)
										.altWeight(-1)
										.ispType(0)
										.level(1)
										.svrType(SVR_TYPE_CDN_SRC)
										.build();
					if (!completeMap.containsKey(task.getPfid())
							|| !completeMap.get(task.getPfid()).contains(diskId)) {
						if (!dispatchTaskMap.containsKey(task.getPfid())
								|| !dispatchTaskMap.get(task.getPfid()).getBadSrc().contains(diskId)) {
							FidSynTaskMap fidSynTaskMap;
							if (completeMap.containsKey(task.getPfid())) {
								fidSynTaskMap = completeMap.get(task.getPfid());
							} else {
								fidSynTaskMap = new FidSynTaskMap();
							}
							fidSynTaskMap.put(diskId, synTask);
							completeMap.put(task.getPfid(), fidSynTaskMap);
						}
					}
					
					if (waitSrcMap.containsKey(task.getPfid())) {
						waitSrcMap.remove(task.getPfid());
					}
					if (waitDispatchMap.containsKey(task.getPfid())) {
						waitDispatchMap.remove(task.getPfid());
					}
					if (storeDiskMap.containsKey(task.getPfid())) {
						storeDiskMap.remove(task.getPfid());
					}
				}
			}
		}
		
		Map<Long, Set<Long>> mapSvr = Maps.newConcurrentMap();
		Set<FidDiskIdPair> delSet = Sets.newHashSet();
		if (waitSrcMap.containsKey(task.getPfid())) {
			FidSynTaskMap fidSynTaskMap = waitSrcMap.get(task.getPfid());
			for (Entry<Long, SynTask> entry : fidSynTaskMap.entrySet()) {
				SynTask synTask = entry.getValue();
				if (synTask.getSvrType() != SVR_TYPE_INIT_SRC
						&& Mappings.DISKS.containsKey(synTask.getDestDiskId())) {
					long nodeId = synTask.getDestDiskId() / 10000L;
					long key, value;
					if (synTask.getOpt() == OPT_SYN
							&& !delSet.contains(new FidDiskIdPair(task.getPfid(), synTask.getDestDiskId()))) {
						if (type == COMMON_GROUP_COLD && Mappings.NODE_GROUP_MAP.containsKey(nodeId)) {
							key = Mappings.NODE_GROUP_MAP.get(nodeId);
						} else {
							key = nodeId;
						}
						value = Mappings.DISKS.get(synTask.getDestDiskId()).getDisk().getSvrId();
						insert(mapSvr, key, value);
					} else if (synTask.getOpt() == OPT_DEL
							&& mapSvr.get(nodeId).contains(Mappings.DISKS.get(synTask.getDestDiskId()).getDisk().getSvrId())) {
						if (type == COMMON_GROUP_COLD && Mappings.NODE_GROUP_MAP.containsKey(nodeId)) {
							key = Mappings.NODE_GROUP_MAP.get(nodeId);
						} else {
							key = nodeId;
						}
						value = Mappings.DISKS.get(synTask.getDestDiskId()).getDisk().getSvrId();
						remove(mapSvr, key, value);
						delSet.add(new FidDiskIdPair(task.getPfid(), synTask.getDestDiskId()));
					} else if (synTask.getOpt() == OPT_DEL) {
						delSet.add(new FidDiskIdPair(task.getPfid(), synTask.getDestDiskId()));
					}
				}
			}
		}
		
		if (waitDispatchMap.containsKey(task.getPfid())) {
			
		}
		
		if (completeMap.containsKey(task.getPfid())) {
			
		}
		
		return mapSvr;
	}
	
	private void insert(Map<Long, Set<Long>> mapSvr, long key, long value) {
		if (mapSvr == null) {
			return;
		}
		
		Set<Long> set;
		if (mapSvr.containsKey(key)) {
			set = mapSvr.get(key);
		} else {
			set = Sets.newHashSet();
		}
		
		set.add(value);
		mapSvr.put(key, set);
	}
	
	private void remove(Map<Long, Set<Long>> mapSvr, long key, long value) {
		if (mapSvr == null) {
			return;
		}
		
		if (mapSvr.containsKey(key)) {
			mapSvr.get(key).remove(value);
		}
	}
	
	private String getValueFromMemcached(String key) {
		String value = key;
		return value;
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
	
	public boolean containsKeyInDispatchMap(long pfid) {
		return dispatchTaskMap.containsKey(pfid);
	}
	
	public boolean dispatchMapInsert(long pfid, DispatchTask dispatchTask) {
		return dispatchTaskMap.put(pfid, dispatchTask) == null ? false : true;
	}
	
	public DispatchTask dispatchMapGet(long pfid) {
		return dispatchTaskMap.get(pfid);
	}
	
	public boolean containsKeyInCompleteMap(long pfid) {
		return completeMap.containsKey(pfid);
	}
	
	public boolean containsKeyInWaitSrcMap(long pfid) {
		return waitSrcMap.containsKey(pfid);
	}
	
	public boolean waitSrcMapInsert(long pfid, FidSynTaskMap fidSynTaskMap) {
		return waitSrcMap.put(pfid, fidSynTaskMap) == null ? false : true;
	}
	
	public FidSynTaskMap waitSrcMapGet(long pfid) {
		return waitSrcMap.get(pfid);
	}
	
	public boolean constainsKeyInStoreDiskMap(long pfid) {
		return storeDiskMap.containsKey(pfid);
	}
	
	public boolean storeDiskMapInsert(long pfid, Set<Long> set) {
		return storeDiskMap.put(pfid, set) == null ? false : true;
	}
	
	public Set<Long> storeDiskMapGet(long pfid) {
		return storeDiskMap.get(pfid);
	}
	
	private void run() {
		while (true) {
			while (!dispatchTaskQueue.isEmpty()) {
				DispatchTask dispatchTask = dispatchTaskQueue.poll();
				es.submit(new TaskConsumerCallable(this, dispatchTask));
			}
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.init();
	}
	
}
