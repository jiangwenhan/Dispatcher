package com.ku6.cdn.dispatcher.common.util;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.ku6.cdn.dispatcher.common.AreaNode;
import com.ku6.cdn.dispatcher.common.DiskOwner;
import com.ku6.cdn.dispatcher.common.GroupNode;
import com.ku6.cdn.dispatcher.common.TaskReference;
import com.ku6.cdn.dispatcher.common.collection.ConcretePair;
import com.ku6.cdn.dispatcher.common.collection.Pair;
import com.ku6.cdn.dispatcher.common.entity.system.DiskInfo;
import com.ku6.cdn.dispatcher.common.entity.system.GroupInfo;
import com.ku6.cdn.dispatcher.common.entity.system.HotServer;
import com.ku6.cdn.dispatcher.common.entity.system.NodeInfo;
import com.ku6.cdn.dispatcher.common.entity.system.PfidInfo;
import com.ku6.cdn.dispatcher.common.entity.system.ServerInfo;

public final class Mappings {
	
	public static final Map<String, Long> IP2DISK = Maps.newConcurrentMap();
	public static final Map<Long, Long> DISK2ISP = Maps.newConcurrentMap();
	public static final Map<String, Boolean> SERVER_STATUS = Maps.newConcurrentMap();
	public static final Map<Long, Long> NODE2ISP = Maps.newConcurrentMap();
	public static final Map<Long, TaskReference> TASK_REFS = Maps.newConcurrentMap();
	public static final Map<Long, Long> HOT_DISKS = Maps.newConcurrentMap();
	public static final Set<Long> OK_DISKS = Sets.newHashSet();
	public static final Set<Long> BAD_DISKS = Sets.newHashSet();
	
	public static final Map<Long, DiskOwner> COLD_DISKS = Maps.newConcurrentMap();
	
	public static Map<Long, GroupInfo> GROUPS = Maps.newConcurrentMap();
	public static Map<Long, NodeInfo> NODES = Maps.newConcurrentMap();
	public static Map<Long, ServerInfo> SERVERS = Maps.newConcurrentMap();
	public static Map<Long, HotServer> HOT_SERVERS = Maps.newConcurrentMap();
	public static Map<Long, DiskOwner> DISKS = Maps.newConcurrentMap();
	public static Multimap<Long, Long> NODE2SERVER = ArrayListMultimap.create();
	public static Multimap<Long, Long> GROUP2SERVER = ArrayListMultimap.create();
	public static Multimap<Long, Long> SERVER2DISK = ArrayListMultimap.create();
	
	public static final Map<Long, NodeInfo> HOT_NODES = Maps.newConcurrentMap();
	public static final Map<Long, NodeInfo> COLD_NODES = Maps.newConcurrentMap();
	public static final Map<Long, Long> NODE_GROUP_MAP = Maps.newConcurrentMap();
	public static final Map<Long, HotServer> SVR_NODE_MAP = Maps.newConcurrentMap();
	public static final Map<Long, Map<Long, Long>> HOT_DISK_MAPS = Maps.newConcurrentMap();
	public static final Map<Long, Map<Long, DiskInfo>> GROUP_DISKS_MAPS = Maps.newConcurrentMap();
	public static final Map<Long, Long> SVR2ISP = Maps.newConcurrentMap();
	public static final Set<Long> NO_SRC_NODES = Sets.newHashSet();
	public static final Map<ConcretePair<Long, Long>, Long> SPEED_MAP = Maps.newConcurrentMap();
	
	public static final Map<Long, PfidInfo> PFID_MAP = Maps.newConcurrentMap();
	
	public static final Map<Long, Long> PFID_SRC_SRV_MAP = Maps.newConcurrentMap();
	public static final Map<Long, Long> PFID_SRC_DISK_MAP = Maps.newConcurrentMap();
	public static final Multimap<String, Pair<Long, Long>> IP_SVR_DISK = ArrayListMultimap.create();
	
	public static Map<Long, GroupNode> GROUP_NODES = Maps.newConcurrentMap();
	public static Map<Long, AreaNode> HOT_AREA_NODES = Maps.newConcurrentMap();
	public static Map<Long, AreaNode> COLD_AREA_NODES = Maps.newConcurrentMap();

}
