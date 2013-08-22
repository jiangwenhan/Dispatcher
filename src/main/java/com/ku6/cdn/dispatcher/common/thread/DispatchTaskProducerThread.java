package com.ku6.cdn.dispatcher.common.thread;

import static com.ku6.cdn.dispatcher.common.Constrants.*;
import static com.ku6.cdn.dispatcher.common.util.TypeUtil.*;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.ku6.cdn.dispatcher.Manager;
import com.ku6.cdn.dispatcher.common.DispatchTask;
import com.ku6.cdn.dispatcher.common.collection.ConcretePair;
import com.ku6.cdn.dispatcher.common.collection.FidSynTaskMap;
import com.ku6.cdn.dispatcher.common.collection.Pair;
import com.ku6.cdn.dispatcher.common.entity.system.DiskInfo;
import com.ku6.cdn.dispatcher.common.entity.system.PfidInfo;
import com.ku6.cdn.dispatcher.common.entity.system.ServerInfo;
import com.ku6.cdn.dispatcher.common.util.Mappings;
import com.ku6.cdn.dispatcher.common.util.builder.impl.DispatchTaskBuilder;
import com.ku6.cdn.dispatcher.common.util.builder.impl.SynTaskBuilder;
import com.ku6.cdn.dispatcher.common.util.builder.impl.TaskStatusBuilder;
import com.ku6.cdn.dispatcher.common.util.builder.impl.TaskTimeoutBuilder;

public class DispatchTaskProducerThread implements Runnable {
	
	private final Manager manager;
	
	public DispatchTaskProducerThread(Manager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		long startId = 0;
		while ((startId = loadPfidInfoData(manager.getCdnSystemSessionFactory().openSession(), startId)) == -1) {
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	private Long loadPfidInfoData(Session session, long startId) {
		List<PfidInfo> list = session.createQuery("from PfidInfo "
														+ "where pfid >= " + startId + " "
														+ "and fileStatus = 1")
									 .setMaxResults(100000)
									 .list();
		if (list.size() == 0) {
			return -1L;
		}
		Iterator<PfidInfo> iterator = list.iterator();
		while (iterator.hasNext()) {
			PfidInfo pfidInfo = iterator.next();
			if (startId < pfidInfo.getPfid()) {
				startId = pfidInfo.getPfid();
			}
			if (pfidInfo.getMd5() == null) {
				manager.getCompleteQueue().add(new TaskStatusBuilder().pfid(pfidInfo.getPfid())
																	  .status(TASK_DSP_SRC_MD5_INVALID)
																	  .build());
				continue;
			}
			int type = castToStoreType(pfidInfo.getStoreType());
			if (type == COMMON_STORE_UNKNOWN) {
				manager.getCompleteQueue().add(new TaskStatusBuilder().pfid(pfidInfo.getPfid())
																	  .status(TASK_DSP_UNKNOWN)
																	  .build());
				continue;
			}
			int opt = pfidInfo.getDeleteState() == 0 ? OPT_SYN : OPT_DEL;
			Pair<Long, Long> pair = new ConcretePair<Long, Long>(0L, 0L);
			if (pfidInfo.getSource() != null) {
				pair = getSourceInfo(session, pfidInfo.getSource());
			}
			Mappings.PFID_SRC_SRV_MAP.put(pfidInfo.getPfid(), pair.first());
			Mappings.PFID_SRC_DISK_MAP.put(pfidInfo.getPfid(), pair.second());
			if (pair.first() != 0 && pair.second() != 0 
					&& (!manager.getCompleteMap().containsKey(pfidInfo.getPfid())
							|| !manager.getCompleteMap().get(pfidInfo.getPfid()).contains(pair.second()))) {
				FidSynTaskMap fidSynTaskMap;
				if (manager.getCompleteMap().containsKey(pfidInfo.getPfid())) {
					fidSynTaskMap = manager.getCompleteMap().get(pfidInfo);
				} else {
					fidSynTaskMap = new FidSynTaskMap();
					fidSynTaskMap.put(pair.second(), new SynTaskBuilder().pfid(pfidInfo.getPfid())
																			  .opt(opt)
																			  .destSvrId(pair.first())
																			  .destDiskId(pair.second())
																			  .priority(pfidInfo.getPriority())
																			  .status(TASK_COMPLETE)
																			  .childNum(0)
																			  .weight(-1)
																			  .altWeight(-1)
																			  .ispType(0)
																			  .svrType(SVR_TYPE_INIT_SRC)
																			  .build());
				}
				manager.getCompleteMap().put(pfidInfo.getPfid(), fidSynTaskMap);
				DispatchTask dispatchTask = new DispatchTaskBuilder()
														.pfid(pfidInfo.getPfid())
														.md5(pfidInfo.getMd5())
														.priority(pfidInfo.getPriority())
														.num(pfidInfo.getFileNum())
														.opt(opt)
														.pfname(pfidInfo.getPfname())
														.uniqIsp(true)
														.timeout(System.currentTimeMillis())
														.mod(2)
														.taskNum(0)
														.build();
				manager.getDispatchTaskQueue().add(dispatchTask);
				manager.getDispatchTaskMap().put(pfidInfo.getPfid(), dispatchTask);
				manager.getTaskTimeoutPriorityQueue().add(new TaskTimeoutBuilder()
																		.pfid(pfidInfo.getPfid())
																		.diskId(0)
																		.timeout(System.currentTimeMillis())
																		.count(0)
																		.build());
			}
		}
		
		return startId;
	}
	
	@SuppressWarnings("unchecked")
	private Pair<Long, Long> getSourceInfo(Session session, String ip) {
		Pair<Long, Long> result = new ConcretePair<Long, Long>(0L, 0L);
		Iterator<ServerInfo> iterator = session.createQuery("from ServerInfo "
											  + "where (ip = '" + ip + "' "
											  + "or ip2 = '" + ip + "') "
											  + "and inUse = " + COMMON_SVR_IN_USE + " "
//											  + "and nginxStatus = " + COMMON_SVR_OK + " "
											  + "and dispStatus = " + COMMON_SVR_OK)
								   .list()
								   .iterator();
		if (iterator.hasNext()) {
			ServerInfo serverInfo = iterator.next();
			result.setFirst(serverInfo.getSvrId());
			if (serverInfo.getSvrId() != 0) {
				Iterator<DiskInfo> iterator2 = session.createQuery("from DiskInfo "
																 + "where inUse = " + COMMON_DISK_IN_USE + " "
																 + "and diskStatus = " + COMMON_DISK_OK + " "
																 + "and svrId = " + serverInfo.getSvrId())
													  .list()
													  .iterator();
				if (iterator2.hasNext()) {
					result.setSecond(iterator2.next().getDiskId());
				}
			}
		}
		return result;
	}

}
