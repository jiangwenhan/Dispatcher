package com.ku6.cdn.dispatcher;

import static com.ku6.cdn.dispatcher.common.Constrants.*;
import static com.ku6.cdn.dispatcher.common.util.TypeUtil.castToLong;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
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
import com.ku6.cdn.dispatcher.common.Task;
import com.ku6.cdn.dispatcher.common.TaskStatus;
import com.ku6.cdn.dispatcher.common.TaskTimeout;
import com.ku6.cdn.dispatcher.common.TimeTask;
import com.ku6.cdn.dispatcher.common.collection.ConcretePriorityQueue;
import com.ku6.cdn.dispatcher.common.collection.ConcretePair;
import com.ku6.cdn.dispatcher.common.collection.FidSynTaskMap;
import com.ku6.cdn.dispatcher.common.collection.PriorityQueue;
import com.ku6.cdn.dispatcher.common.entity.system.DiskInfo;
import com.ku6.cdn.dispatcher.common.entity.system.GroupInfo;
import com.ku6.cdn.dispatcher.common.entity.system.HostSpeed;
import com.ku6.cdn.dispatcher.common.entity.system.HotServer;
import com.ku6.cdn.dispatcher.common.entity.system.NodeInfo;
import com.ku6.cdn.dispatcher.common.entity.system.ServerInfo;
import com.ku6.cdn.dispatcher.common.thread.DispatchTaskProducerThread;
import com.ku6.cdn.dispatcher.common.thread.TaskConsumerThread;
import com.ku6.cdn.dispatcher.common.thread.TaskProducerThread;
import com.ku6.cdn.dispatcher.common.util.Mappings;
import com.ku6.cdn.dispatcher.common.util.builder.impl.SynTaskBuilder;


@Component
public class Manager implements InitializingBean {
	
	private SessionFactory cdnSystemSessionFactory;
	private SessionFactory cdnDeliverySessionFactory;
	private SessionFactory utccSessionFactory;
	
	private int confFileNum = 80;
	private int confFileRate = 80;
	private int confFileLimitSpeed = 0;
	private List<Long> confNodes = Lists.newLinkedList();
	
	private final ExecutorService es = Executors.newFixedThreadPool(20);
	private final ExecutorService tces = Executors.newFixedThreadPool(1);
	private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(20);
	private final ScheduledExecutorService dtpSes = Executors.newScheduledThreadPool(1);
	
	private SourceSelector srcSelector;
	
	private final PriorityQueue<DispatchTask> dispatchTaskQueue 
		= new ConcretePriorityQueue<DispatchTask>(new Comparator<DispatchTask>() {

			@Override
			public int compare(DispatchTask o1, DispatchTask o2) {
				return (int)(o1.getPriority() - o2.getPriority());
			}
			
		});
	private final PriorityQueue<SynTask> synTaskQueue
		= new ConcretePriorityQueue<SynTask>(new Comparator<SynTask>() {

			@Override
			public int compare(SynTask o1, SynTask o2) {
				return (int)(o1.getPriority() - o2.getPriority());
			}
			
		});
	private final PriorityQueue<TimeTask> timeTaskPriorityQueue
		= new ConcretePriorityQueue<TimeTask>(new Comparator<TimeTask>() {

			@Override
			public int compare(TimeTask o1, TimeTask o2) {
				return (int)(o1.getRunTs() - o2.getRunTs());
			}
		});
	private final PriorityQueue<TaskTimeout> taskTimeoutPriorityQueue
		= new ConcretePriorityQueue<TaskTimeout>(new Comparator<TaskTimeout>() {

			@Override
			public int compare(TaskTimeout o1, TaskTimeout o2) {
				return (int)(o2.getTimeout() - o1.getTimeout());
			}
		});
	private final Queue<TimeTask> timeTaskQueue = Queues.newConcurrentLinkedQueue();
	private final Queue<Task> taskQueue = Queues.newConcurrentLinkedQueue();
	private final Queue<TaskStatus> completeQueue = Queues.newConcurrentLinkedQueue();
	
	private final Map<Long, DispatchTask> dispatchTaskMap = Maps.newConcurrentMap();
	private final Map<Long, FidSynTaskMap> waitSrcMap = Maps.newConcurrentMap();
	private final Map<Long, FidSynTaskMap> waitDispatchMap = Maps.newConcurrentMap();
	private final Map<Long, FidSynTaskMap> completeMap = Maps.newConcurrentMap();
	private final Map<Long, Set<Long>> storeDiskMap = Maps.newConcurrentMap();
	private final Map<Long, Long> pfidSrcSrvMap = Maps.newConcurrentMap();
	private final Map<Long, Long> pfidSrcDiskMap = Maps.newConcurrentMap();
	
	private void init(int sessionCount, Session... sessions) {
		initIpDiskMapping(sessions[0]);
		initIspMapping(sessions[0]);
		initServerMapping(sessions[0]);
//		srcSelector = new SourceSelector();
//		srcSelector.setConfLimitSpeed(confFileLimitSpeed);
		run();
	}

	@SuppressWarnings("rawtypes")
	private void initIpDiskMapping(final Session session) {
		ses.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				Iterator iterator = session.createQuery("select diskInfo.diskId, serverInfo.ip "
						  							  + "from ServerInfo serverInfo, DiskInfo diskInfo "
						  							  + "where serverInfo.svrType = 1 "
						  							  + "and diskInfo.svrId = serverInfo.svrId")
						  				   .list()
						  				   .iterator();
				while (iterator.hasNext()) {
					Object[] items = (Object[]) iterator.next();
					if(items[0] != null && items[1] != null) {
						Mappings.IP2DISK.put((String)items[1], (Long)items[0]);
					}
				}
			}
		}, 0, 5, TimeUnit.MINUTES);
	}

	@SuppressWarnings("rawtypes")
	private void initIspMapping(final Session session) {
		ses.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				Iterator iterator = session.createQuery("select groupInfo.ispId, diskInfo.diskId, serverInfo.nodeId "
													  + "from GroupInfo groupInfo, NodeInfo nodeInfo, ServerInfo serverInfo, DiskInfo diskInfo "
													  + "where diskInfo.svrId = serverInfo.svrId "
													  + "and serverInfo.nodeId = nodeInfo.nodeId "
													  + "and nodeInfo.groupId = groupInfo.groupId "
													  + "and ("
													  + "groupInfo.ispId = 1 "
													  + "or groupInfo.ispId = 2 "
													  + ")")
										   .list()
										   .iterator();
				while (iterator.hasNext()) {
					Object[] items = (Object[]) iterator.next();
					Mappings.DISK2ISP.put(castToLong(items[0]), castToLong(items[1]));
					Mappings.NODE2ISP.put(castToLong(items[2]), castToLong(items[1]));
				}
			}
		}, 0, 5, TimeUnit.MINUTES);
	}

	@SuppressWarnings("unchecked")
	private void initServerMapping(final Session session) {
		ses.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				Iterator<ServerInfo> iterator = session.createQuery("from ServerInfo")
													   .list()
													   .iterator();
				while (iterator.hasNext()) {
					ServerInfo serverInfo = iterator.next();
					if (serverInfo.getSvrType() == 1) {
						if (serverInfo.getDispStatus() == 0 || serverInfo.getInUse() == 0) {
							if (serverInfo.getIp() != null) {
								Mappings.SERVER_STATUS.put(serverInfo.getIp(), false);
							}
							if (serverInfo.getIp2() != null) {
								Mappings.SERVER_STATUS.put(serverInfo.getIp2(), false);
							}
						} else {
							if (serverInfo.getIp() != null) {
								Mappings.SERVER_STATUS.put(serverInfo.getIp(), true);
							}
							if (serverInfo.getIp2() != null) {
								Mappings.SERVER_STATUS.put(serverInfo.getIp2(), true);
							}
						}
					}
				}
			}
		}, 0, 5, TimeUnit.MINUTES);
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
			 DiskInfo disk = (DiskInfo) row[0];
			 ServerInfo server = (ServerInfo) row[1];
			 NodeInfo node = (NodeInfo) row[2];
			 GroupInfo group = (GroupInfo) row[3];
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
			GroupInfo group = (GroupInfo) iterator.next();
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
			NodeInfo node = (NodeInfo) iterator.next();
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
			NodeInfo node = (NodeInfo) iterator.next();
			Mappings.NO_SRC_NODES.add(node.getNodeId());
		}
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
	
	private boolean createSynTask(DispatchTask task, Map<Long, Set<Long>> mapSet, List<SynTask> retTasks) {
		int iCount = 0;
		if (task.getTaskType() == COMMON_NODE_HOT || task.getTaskType() == COMMON_NODE_COLD) {
			Map<Long, AreaNode> areaNodes = 
					task.getTaskType() == COMMON_NODE_HOT ? Mappings.HOT_AREA_NODES : Mappings.COLD_AREA_NODES;
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
					// TODO: do some log
					continue;
				}
				retTasks.addAll(synTasks);
			}
			if (iCount == areaNodes.size())
				return true;
		} else if (task.getTaskType() == COMMON_GROUP_COLD) {
			for (Entry<Long, GroupNode> entry : Mappings.GROUP_NODES.entrySet()) {
				List<SynTask> synTasks = null;
				int num = task.getNum() - mapSet.get(entry.getValue().getGroupId()).size();
				if (num <= 0) {
					++iCount;
				} else {
					// TODO: Create Task
				}
				if (synTasks == null || synTasks.size() == 0) {
					// TODO: do some log
					continue;
				}
				retTasks.addAll(synTasks);
			}
			if (iCount == Mappings.GROUP_NODES.size()) {
				return true;
			}
			operateConfNode(task, retTasks);
		}
		return false;
	}
	
	private void operateConfNode(DispatchTask task, List<SynTask> retTasks) {
		Map<Long, Set<Long>> mapSet = getSvrMap(task, COMMON_NODE_HOT);
		for (Long each : confNodes) {
			List<SynTask> synTasks = null;
			int num = task.getNum() - mapSet.get(each).size();
			if (num > 0 && Mappings.COLD_AREA_NODES.containsKey(each)) {
				// TODO: Create Task
			} else {
				continue;
			}
			if (synTasks == null || synTasks.size() == 0) {
				// TODO: do some log
				continue;
			}
			retTasks.addAll(synTasks);
		}
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
	
	// contains
	public boolean containsKeyInDispatchMap(long pfid) {
		return dispatchTaskMap.containsKey(pfid);
	}
	
	public boolean containsKeyInCompleteMap(long pfid) {
		return completeMap.containsKey(pfid);
	}
	
	public boolean containsKeyInWaitSrcMap(long pfid) {
		return waitSrcMap.containsKey(pfid);
	}
	
	public boolean constainsKeyInStoreDiskMap(long pfid) {
		return storeDiskMap.containsKey(pfid);
	}
	
	// inserts 
	public boolean dispatchMapInsert(long pfid, DispatchTask dispatchTask) {
		return dispatchTaskMap.put(pfid, dispatchTask) == null ? false : true;
	}
	
	public boolean waitSrcMapInsert(long pfid, FidSynTaskMap fidSynTaskMap) {
		return waitSrcMap.put(pfid, fidSynTaskMap) == null ? false : true;
	}
	
	public boolean storeDiskMapInsert(long pfid, Set<Long> set) {
		return storeDiskMap.put(pfid, set) == null ? false : true;
	}
	
	// gets
	public DispatchTask dispatchMapGet(long pfid) {
		return dispatchTaskMap.get(pfid);
	}
	
	public FidSynTaskMap waitSrcMapGet(long pfid) {
		return waitSrcMap.get(pfid);
	}
	
	public Set<Long> storeDiskMapGet(long pfid) {
		return storeDiskMap.get(pfid);
	}
	
	// Execution
	private void run() {
		ses.scheduleWithFixedDelay(new TaskProducerThread(this), 0, 30, TimeUnit.HOURS);
		dtpSes.scheduleWithFixedDelay(new DispatchTaskProducerThread(this), 0, 30, TimeUnit.SECONDS);
		while (true) {
			while (!taskQueue.isEmpty()) {
				tces.submit(new TaskConsumerThread(this));
			}
		}
	}
	
	// Getter
	public SessionFactory getCdnSystemSessionFactory() {
		return cdnSystemSessionFactory;
	}
	
	public SessionFactory getCdnDeliverySessionFactory() {
		return cdnDeliverySessionFactory;
	}
	
	public SessionFactory getUtccSessionFactory() {
		return utccSessionFactory;
	}
	
	public int getConfFileNum() {
		return confFileNum;
	}
	
	public int getConfFileRate() {
		return confFileRate;
	}

	public int getConfFileLimitSpeed() {
		return confFileLimitSpeed;
	}
	
	public List<Long> getConfNodes() {
		return confNodes;
	}

	public SourceSelector getSrcSelector() {
		return srcSelector;
	}
	
	public PriorityQueue<DispatchTask> getDispatchTaskQueue() {
		return dispatchTaskQueue;
	}
	
	public PriorityQueue<SynTask> getSynTaskQueue() {
		return synTaskQueue;
	}
	
	public PriorityQueue<TimeTask> getTimeTaskPriorityQueue() {
		return timeTaskPriorityQueue;
	}
	
	public PriorityQueue<TaskTimeout> getTaskTimeoutPriorityQueue() {
		return taskTimeoutPriorityQueue;
	}
	
	public Queue<TimeTask> getTimeTaskQueue() {
		return timeTaskQueue;
	}
	
	public Queue<Task> getTaskQueue() {
		return taskQueue;
	}
	
	public Queue<TaskStatus> getCompleteQueue() {
		return completeQueue;
	}
	
	public Map<Long, DispatchTask> getDispatchTaskMap() {
		return dispatchTaskMap;
	}
	
	public Map<Long, FidSynTaskMap> getWaitSrcMap() {
		return waitSrcMap;
	}
	
	public Map<Long, FidSynTaskMap> getWaitDispatchMap() {
		return waitDispatchMap;
	}
	
	public Map<Long, FidSynTaskMap> getCompleteMap() {
		return completeMap;
	}

	public Map<Long, Set<Long>> getStoreDiskMap() {
		return storeDiskMap;
	}

	public Map<Long, Long> getPfidSrcSrvMap() {
		return pfidSrcSrvMap;
	}

	public Map<Long, Long> getPfidSrcDiskMap() {
		return pfidSrcDiskMap;
	}
	
	// Setter
	public void setCdnSystemSessionFactory(
			SessionFactory cdnSystemSessionFactory) {
		this.cdnSystemSessionFactory = cdnSystemSessionFactory;
	}

	public void setCdnDeliverySessionFactory(
			SessionFactory cdnDeliverySessionFactory) {
		this.cdnDeliverySessionFactory = cdnDeliverySessionFactory;
	}
	
	public void setUtccSessionFactory(SessionFactory utccSessionFactory) {
		this.utccSessionFactory = utccSessionFactory;
	}
	
	public void setConfFileNum(int confFileNum) {
		this.confFileNum = confFileNum;
	}

	public void setConfFileRate(int confFileRate) {
		this.confFileRate = confFileRate;
	}

	public void setConfFileLimitSpeed(int confFileLimitSpeed) {
		this.confFileLimitSpeed = confFileLimitSpeed;
	}
	
	public void setConfNodes(List<Long> confNodes) {
		this.confNodes = confNodes;
	}
	
	public void setSrcSelector(SourceSelector srcSelector) {
		this.srcSelector = srcSelector;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Session cdnSystemSession = cdnSystemSessionFactory.openSession();
		Session cdnDeliverySession = cdnDeliverySessionFactory.openSession();
		Session utccSession = utccSessionFactory.openSession();
		this.init(3, cdnSystemSession, cdnDeliverySession, utccSession);
	}

}
