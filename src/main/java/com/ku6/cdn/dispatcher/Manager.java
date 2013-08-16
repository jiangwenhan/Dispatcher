package com.ku6.cdn.dispatcher;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.ku6.cdn.dispatcher.common.AreaNode;
import com.ku6.cdn.dispatcher.common.DiskOwner;
import com.ku6.cdn.dispatcher.common.DispatchTask;
import com.ku6.cdn.dispatcher.common.GroupNode;
import com.ku6.cdn.dispatcher.common.SynTask;
import com.ku6.cdn.dispatcher.common.TaskStatus;
import com.ku6.cdn.dispatcher.common.collection.ConcretePriorityQueue;
import com.ku6.cdn.dispatcher.common.collection.ConcretePair;
import com.ku6.cdn.dispatcher.common.collection.FidSynTaskMap;
import com.ku6.cdn.dispatcher.common.entity.Disk;
import com.ku6.cdn.dispatcher.common.entity.Group;
import com.ku6.cdn.dispatcher.common.entity.HostSpeed;
import com.ku6.cdn.dispatcher.common.entity.HotServer;
import com.ku6.cdn.dispatcher.common.entity.Node;
import com.ku6.cdn.dispatcher.common.entity.Server;
import com.ku6.cdn.dispatcher.common.thread.TaskConsumerCallable;
import com.ku6.cdn.dispatcher.common.util.Mappings;
import com.ku6.cdn.dispatcher.common.util.SynTaskBuilder;
import com.ku6.cdn.dispatcher.common.util.TaskStatusBuilder;


@Component
public class Manager implements InitializingBean {
	
	private static SessionFactory cdnSystemSessionFactory;
	private static SessionFactory cdnDeliverySessionFactory;
	private static SessionFactory utccSessionFactory;
	
	private static int confFileNum = 80;
	private static int confFileRate = 80;
	private static int confFileLimitSpeed = 0;
	
	private final ExecutorService es = Executors.newFixedThreadPool(20);
	
	private SourceSelector srcSelector;
	
	// node maps
	private Map<Long, GroupNode> groupNodes;
	private Map<Long, AreaNode> hotAreaNodes;
	private Map<Long, AreaNode> coldAreaNodes;
	
	private final Map<Long, DispatchTask> dispatchTaskMap = Maps.newConcurrentMap();
	private final ConcretePriorityQueue<DispatchTask> dispatchTaskQueue 
		= new ConcretePriorityQueue<DispatchTask>(new Comparator<DispatchTask>() {

			@Override
			public int compare(DispatchTask o1, DispatchTask o2) {
				return (int)(o1.getPriority() - o2.getPriority());
			}
			
		});
	private final ConcretePriorityQueue<SynTask> synTaskQueue
		= new ConcretePriorityQueue<SynTask>(new Comparator<SynTask>() {

			@Override
			public int compare(SynTask o1, SynTask o2) {
				return (int)(o1.getPriority() - o2.getPriority());
			}
			
		});
	
	private final Map<Long, FidSynTaskMap> waitSrcMap = Maps.newConcurrentMap();
	private final Map<Long, FidSynTaskMap> waitDispatchMap = Maps.newConcurrentMap();
	private final Map<Long, FidSynTaskMap> completeMap = Maps.newConcurrentMap();
	private final Queue<TaskStatus> completeQueue = Queues.newConcurrentLinkedQueue();
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
		initSpeed();
		srcSelector = new SourceSelector();
		srcSelector.setConfLimitSpeed(confFileLimitSpeed);
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
			 if (disk.getDiskStatus() == COMMON_DISK_BAD
					 || disk.getInUse() != COMMON_DISK_IN_USE
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
			 if (disk.getInUse() == COMMON_DISK_IN_USE
					 && disk.getDiskStatus() == COMMON_DISK_OK
					 && server.getInUse() == COMMON_SVR_IN_USE
					 && server.getNginxStatus() == COMMON_SVR_OK
					 && server.getDispStatus() == COMMON_SVR_OK) {
				 if (!Mappings.HOT_DISKS.containsKey(disk.getDiskId())) {
					 Mappings.COLD_DISKS.put(disk.getDiskId(), new DiskOwner(group.getGroupId(), 
																			 group.getGroupType(), 
																			 disk));
				 }
			 }
			 Mappings.DISKS.put(disk.getDiskId(), new DiskOwner(group.getGroupId(), 
					 											group.getGroupType(), 
					 											disk));
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void initNodesMaps() {
		Iterator iterator = cdnSystemSessionFactory.getCurrentSession()
												   .createQuery("from Group "
												   		+ "where inUse = 1")
												   .list()
												   .iterator();
		while (iterator.hasNext()) {
			Group group = (Group) iterator.next();
			if (group.getGroupType() == COMMON_NORMAL_NODE_TYPE) {
				Mappings.GROUPS.put(group.getGroupId(), group);
			}
			Mappings.SVR2ISP.put(group.getGroupId(), group.getIspId());
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
		
		iterator = cdnSystemSessionFactory.getCurrentSession()
										  .createQuery("from Node "
										  		+ "where isDispatchSource = 0")
										  .list()
										  .iterator();
		while (iterator.hasNext()) {
			Node node = (Node) iterator.next();
			Mappings.NO_SRC_NODES.add(node.getNodeId());
		}
	}
	
	private void initGroupNodes() {
		
	}
	
	@SuppressWarnings("rawtypes")
	private void initSpeed() {
		Iterator iterator = cdnSystemSessionFactory.getCurrentSession()
				   								   .createQuery("from HostSpeed")
				   								   .list()
				   								   .iterator();
		while (iterator.hasNext()) {
			HostSpeed hostSpeed = (HostSpeed) iterator.next();
			Mappings.SPEED_MAP.put(new ConcretePair<Long, Long>(hostSpeed.getDestId(), 
																hostSpeed.getSrcId()), 
																hostSpeed.getSpeed());
		}
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
		Set<ConcretePair<Long, Long>> delSet = Sets.newHashSet();
		
		String nodeInfo = getValueFromMemcached(StringUtils.stripStart(task.getPfname(), "/"));
		String[] strings = nodeInfo.split(",");
		for (String each : strings) {
			String[] eachItems = each.split(":");
			long nodeId = Long.valueOf(eachItems[0]);
			String[] disks = eachItems[1].split("/");
			for (String disk : disks) {
				long diskId = nodeId * 10000L + Long.valueOf(disk);
				if (Mappings.DISKS.containsKey(diskId)) {
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
//												.level(1)
												.svrType(SVR_TYPE_CDN_SRC)
												.build();
					synTaskQueue.add(synTask);
					waitDispatchMap.get(task.getPfid()).put(diskId, synTask);
					delSet.add(new ConcretePair<Long, Long>(task.getPfid(), diskId));
				}
			}
		}
		waitSrcMap.remove(task.getPfid());
		completeMap.remove(task.getPfid());
		
		return true;
	}
	
	public boolean checkMap(long pfid, int state) {
		if (state != 0 || 
				(!waitDispatchMap.containsKey(pfid) || waitDispatchMap.get(pfid).size() == 0)
				&& (!waitSrcMap.containsKey(pfid) || waitSrcMap.get(pfid).size() == 0)) {
			if (dispatchTaskMap.containsKey(pfid)) {
				if (dispatchTaskMap.get(pfid).getTaskNum() == 0) {
					// TODO: do some log
					return true;
				}
				completeQueue.add(new TaskStatusBuilder()
											.pfid(pfid)
											.status(state)
											.build());
				dispatchTaskMap.remove(pfid);
			} else {
				// TODO: do some log
			}
			waitDispatchMap.remove(pfid);
			waitSrcMap.remove(pfid);
			completeMap.remove(pfid);
			return true;
		}
		
		int count = 0;
		if (completeMap.containsKey(pfid)) {
			count = completeMap.get(pfid).size();
		}
		if (count >= confFileNum) {
			long totalNum = waitSrcMap.get(pfid).size() + waitDispatchMap.get(pfid).size() + count;
			// TODO: do some log
			if (count * 100 / totalNum >= confFileRate
					&& (!storeDiskMap.containsKey(pfid) || storeDiskMap.get(pfid).size() == 0)) {
				if (dispatchTaskMap.containsKey(pfid)) {
					if (dispatchTaskMap.get(pfid).getTaskNum() == 0) {
						// TODO: do some log
						return true;
					}
					completeQueue.add(new TaskStatusBuilder()
												.pfid(pfid)
												.status(state)
												.build());
					// TODO: do some log
					dispatchTaskMap.remove(pfid);
				} else {
					// TODO: do some log
				}
				waitDispatchMap.remove(pfid);
				waitSrcMap.remove(pfid);
				completeMap.remove(pfid);
				return true;
			}
			if (dispatchTaskMap.containsKey(pfid)) {
				if (dispatchTaskMap.get(pfid).getPriority() != 3) {
					dispatchTaskMap.get(pfid).setPriority(3);
					dispatchTaskQueue.add(dispatchTaskMap.get(pfid));
				}
				waitSrcMap.remove(pfid);
			}
		}
		return true;
	}
	
	public void doTask(long pfid, int type) {
		if (type == COMMON_NORMAL_DO_TASK) {
			FidSynTaskMap srcTaskMap = completeMap.get(pfid);
			if (!waitSrcMap.containsKey(pfid) || waitSrcMap.get(pfid).size() == 0) {
				// TODO: do some log 
				return;
			}
			FidSynTaskMap destTaskMap = waitSrcMap.get(pfid);
			List<SynTask> destTask = Lists.newLinkedList();
			List<SynTask> srcTask = Lists.newLinkedList();
			List<SynTask> srcInit = Lists.newLinkedList();
			for (Entry<Long, SynTask> entry : srcTaskMap.entrySet()) {
				if (entry.getValue().getSvrType() == SVR_TYPE_INIT_SRC
						&& entry.getValue().getChildNum() < srcSelector.getChildNum()) {
					srcInit.add(entry.getValue());
				} else if (entry.getValue().getChildNum() < srcSelector.getChildNum()
						&& !Mappings.NO_SRC_NODES.contains(entry.getValue().getDestDiskId() / GLOBAL_MOD)) {
					srcTask.add(entry.getValue());
				}
			}
			for (Entry<Long, SynTask> entry : destTaskMap.entrySet()) {
				destTask.add(entry.getValue());
			}
			if (srcTask.isEmpty() && srcInit.isEmpty()) {
				// TODO: do some log
				return;
			}
			if (dispatchTaskMap.containsKey(pfid)) {
				if (srcTask.isEmpty()) {
					srcSelector.FindSrc(destTask, srcInit, dispatchTaskMap.get(pfid).isUniqIsp());
				} else {
					srcSelector.FindSrc(destTask, srcTask, dispatchTaskMap.get(pfid).isUniqIsp());
				}
			} else {
				if (srcTask.isEmpty()) {
					srcSelector.FindSrc(destTask, srcInit);
				} else {
					srcSelector.FindSrc(destTask, srcTask);
				}
			}
			
			if (!srcTask.isEmpty()) {
				for (SynTask synTask : srcTask) {
					if (completeMap.get(pfid).contains(synTask.getDestDiskId())) {
						completeMap.get(pfid).put(synTask.getDestDiskId(), synTask);
					}
				}
			} else {
				for (SynTask synTask : srcInit) {
					if (completeMap.get(pfid).contains(synTask.getDestDiskId())) {
						completeMap.get(pfid).put(synTask.getDestDiskId(), synTask);
					}
				}
			}
			
			// TODO: do some log
			for (SynTask synTask : destTask) {
				if (synTask.getSrcDiskId() == 0
						|| synTask.getSrcSvrId() == 0) {
					if (Mappings.PFID_MAP.containsKey(pfid)) {
						synTask.setSrcSvrId(Mappings.PFID_SRC_SRV_MAP.get(pfid));
						synTask.setSrcDiskId(Mappings.PFID_SRC_DISK_MAP.get(pfid));
					}
					// TODO: do some log
					waitSrcMap.get(pfid).remove(synTask.getDestDiskId());
					waitDispatchMap.get(pfid).put(synTask.getDestDiskId(), synTask);
					synTaskQueue.add(synTask);
				}
			}
			
		} else if (type == COMMON_FULL_DO_TASK) {
			
		}
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
		Set<ConcretePair<Long, Long>> delSet = Sets.newHashSet();
		if (waitSrcMap.containsKey(task.getPfid())) {
			FidSynTaskMap fidSynTaskMap = waitSrcMap.get(task.getPfid());
			for (Entry<Long, SynTask> entry : fidSynTaskMap.entrySet()) {
				SynTask synTask = entry.getValue();
				if (synTask.getSvrType() != SVR_TYPE_INIT_SRC
						&& Mappings.DISKS.containsKey(synTask.getDestDiskId())) {
					long nodeId = synTask.getDestDiskId() / 10000L;
					long key, value;
					if (synTask.getOpt() == OPT_SYN
							&& !delSet.contains(new ConcretePair<Long, Long>(task.getPfid(), synTask.getDestDiskId()))) {
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
						delSet.add(new ConcretePair<Long, Long>(task.getPfid(), synTask.getDestDiskId()));
					} else if (synTask.getOpt() == OPT_DEL) {
						delSet.add(new ConcretePair<Long, Long>(task.getPfid(), synTask.getDestDiskId()));
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
	
	public static SessionFactory getCdnDeliverySessionFactory() {
		return cdnDeliverySessionFactory;
	}

	public static void setCdnDeliverySessionFactory(
			SessionFactory cdnDeliverySessionFactory) {
		Manager.cdnDeliverySessionFactory = cdnDeliverySessionFactory;
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
	
	public static int getConfFileNum() {
		return confFileNum;
	}

	public static void setConfFileNum(int confFileNum) {
		Manager.confFileNum = confFileNum;
	}

	public static int getConfFileRate() {
		return confFileRate;
	}

	public static void setConfFileRate(int confFileRate) {
		Manager.confFileRate = confFileRate;
	}

	public static int getConfFileLimitSpeed() {
		return confFileLimitSpeed;
	}

	public static void setConfFileLimitSpeed(int confFileLimitSpeed) {
		Manager.confFileLimitSpeed = confFileLimitSpeed;
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
