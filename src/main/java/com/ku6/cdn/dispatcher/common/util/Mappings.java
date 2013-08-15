package com.ku6.cdn.dispatcher.common.util;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ku6.cdn.dispatcher.common.DiskOwner;
import com.ku6.cdn.dispatcher.common.TaskReference;
import com.ku6.cdn.dispatcher.common.collection.ConcretePair;
import com.ku6.cdn.dispatcher.common.entity.Disk;
import com.ku6.cdn.dispatcher.common.entity.Group;
import com.ku6.cdn.dispatcher.common.entity.HotServer;
import com.ku6.cdn.dispatcher.common.entity.Node;
import com.ku6.cdn.dispatcher.common.entity.PFidInfo;
import com.ku6.cdn.dispatcher.common.entity.Server;

public final class Mappings {
	
	public static final Map<String, Integer> IP2DISK = Maps.newConcurrentMap();
	public static final Map<Integer, Integer> DISK2ISP = Maps.newConcurrentMap();
	public static final Map<String, Boolean> SERVER_STATUS = Maps.newConcurrentMap();
	public static final Map<Integer, Integer> NODE2ISP = Maps.newConcurrentMap();
	public static final Map<Long, TaskReference> TASK_REFS = Maps.newConcurrentMap();
	public static final Map<Long, Long> HOT_DISKS = Maps.newConcurrentMap();
	public static final Set<Long> OK_DISKS = Sets.newHashSet();
	public static final Set<Long> BAD_DISKS = Sets.newHashSet();
	public static final Map<Long, DiskOwner> DISKS = Maps.newConcurrentMap();
	public static final Map<Long, DiskOwner> COLD_DISKS = Maps.newConcurrentMap();
	public static final Map<Long, Server> SERVERS = Maps.newConcurrentMap();
	public static final Map<Long, Group> GROUPS = Maps.newConcurrentMap();
	public static final Map<Long, Node> HOT_NODES = Maps.newConcurrentMap();
	public static final Map<Long, Node> COLD_NODES = Maps.newConcurrentMap();
	public static final Map<Long, Long> NODE_GROUP_MAP = Maps.newConcurrentMap();
	public static final Map<Long, HotServer> SVR_NODE_MAP = Maps.newConcurrentMap();
	public static final Map<Long, Map<Long, Long>> HOT_DISK_MAPS = Maps.newConcurrentMap();
	public static final Map<Long, Map<Long, Disk>> GROUP_DISKS_MAPS = Maps.newConcurrentMap();
	public static final Map<Long, Long> SVR2ISP = Maps.newConcurrentMap();
	public static final Set<Long> NO_SRC_NODES = Sets.newHashSet();
	public static final Map<ConcretePair<Long, Long>, Long> SPEED_MAP = Maps.newConcurrentMap();
	public static final Map<Long, PFidInfo> PFID_MAP = Maps.newConcurrentMap();

	public static boolean increaseTaskOkRef(Long key) {
		TaskReference taskRef = TASK_REFS.get(key);
		if (taskRef != null) {
			TASK_REFS.put(key, taskRef.setOkRef(taskRef.getOkRef() + 1));
			return true;
		}
		return false;
	}
	
	public static boolean increaseTaskCompleteRef(Long key) {
		TaskReference taskRef = TASK_REFS.get(key);
		if (taskRef != null) {
			TASK_REFS.put(key, taskRef.setCompleteRef(taskRef.getCompleteRef() + 1));
			return true;
		}
		return false;
	}
}
