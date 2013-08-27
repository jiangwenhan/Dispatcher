package com.ku6.cdn.dispatcher.common.thread;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import com.ku6.cdn.dispatcher.Manager;
import com.ku6.cdn.dispatcher.common.DispatchTask;
import com.ku6.cdn.dispatcher.common.Task;
import com.ku6.cdn.dispatcher.common.collection.FidSynTaskMap;
import com.ku6.cdn.dispatcher.common.collection.Pair;
import com.ku6.cdn.dispatcher.common.collection.TaskQueue;
import com.ku6.cdn.dispatcher.common.entity.system.PfidInfo;
import com.ku6.cdn.dispatcher.common.entity.utcc.SourceVideoInfo;
import com.ku6.cdn.dispatcher.common.util.Mappings;
import com.ku6.cdn.dispatcher.common.util.builder.impl.DispatchTaskBuilder;
import com.ku6.cdn.dispatcher.common.util.builder.impl.PfidInfoBuilder;
import com.ku6.cdn.dispatcher.common.util.builder.impl.SynTaskBuilder;
import com.ku6.cdn.dispatcher.common.util.builder.impl.TaskTimeoutBuilder;

public class TaskConsumerThread implements Callable<Void> {
	
	private final Manager manager;
	
	public TaskConsumerThread(Manager manager) {
		this.manager = manager;
	}

	@Override
	public Void call() throws Exception {
		consumeTask(manager.getUtccSessionFactory().openSession(),
				manager.getCdnSystemSessionFactory().openSession());
		return null;
	}
	
	private void consumeTask(Session utccSession, Session cdnSystemSession) {
		while (!manager.getTaskQueue().isEmpty()) {
			Task task = manager.getTaskQueue().poll();
			if (task == null)
				return;
			String customer = "ku6";
			String storeType = "group_cold";
			String uploadServer = "ku6";
			Integer enableUpdate = 1;
			Integer fileNum = 1;
			List<SourceVideoInfo> infos = findSourceVideoInfoByTaskId(utccSession, task.getVid());
			if (infos == null || infos.size() == 0) {
				TaskQueue.push(task);
				continue;
			}
			SourceVideoInfo info = infos.get(0);
			customer = info.getCfrom();
			uploadServer = info.getUploadServer();
			if (HOT_CFROM.contains(customer.toLowerCase())) {
				enableUpdate = 0;
				storeType = "node_hot";
				fileNum = 2;
			}
			
			/**
			 * connType is 1
			 */
			int opt = 1;
			String pfname = task.getPath();
			String md5 = task.getMd5();
			long size = task.getSize();
			String srcIp = task.getHost();
			long cfid = task.getTaskId();
			long vid = task.getVid();
			int rate = 0;
			int priority = task.getPriority();
			
			// TODO: if success, then create time task and put it into priority queue
			int deleteState = opt == OPT_DEL ? 1 : 0;
			PfidInfo pfidInfo = findPfidInfoByPfname(cdnSystemSession, pfname);
			if (pfidInfo != null) {
				pfidInfo.setFileStatus(1);
				pfidInfo.setDeleteState(deleteState);
				if (opt == OPT_SYN) {
					pfidInfo.setFileNum(fileNum);
					pfidInfo.setStoreType(storeType);
					pfidInfo.setPriority(priority);
					pfidInfo.setCustomer(customer);
					pfidInfo.setMd5(md5);
					pfidInfo.setEnableUpdate(enableUpdate);
					pfidInfo.setSize(size);
				}
			} else {
				pfidInfo = new PfidInfoBuilder().pfname(pfname)
												.cfid(cfid)
												.size(size)
												.md5(md5)
												.vid(vid)
												.rate(rate)
												.fileNum(fileNum)
												.priority(priority)
												.fileStatus(1)
												.storeType(storeType)
												.customer(customer)
												.addTime(new Date(System.currentTimeMillis()))
												.deleteState(deleteState)
												.source(srcIp)
												.enableUpdate(enableUpdate)
												.build();
			}
			saveOrUpdatePFidInfo(cdnSystemSession, pfidInfo);
			if (!Mappings.IP_SVR_DISK.containsKey(srcIp) || 
					Mappings.IP_SVR_DISK.get(srcIp).size() == 0) {
				// TODO: do some log
				continue;
			}
			Collection<Pair<Long, Long>> pairs = Mappings.IP_SVR_DISK.get(srcIp);
			Pair<Long, Long> pair = pairs.iterator().next();
			addTask(new DispatchTaskBuilder().build(), pair.first(), pair.second());
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<SourceVideoInfo> findSourceVideoInfoByTaskId(Session session, Long vid) {
		String hql = "from SourceVideoInfo "
				+ "where vid = " + vid;
		List<SourceVideoInfo> infos = session.createQuery(hql)
											 .list();
		return infos;
	}
	
	@SuppressWarnings("unchecked")
	private PfidInfo findPfidInfoByPfname(Session session, String pfname) {
		Iterator<PfidInfo> iterator = session.createQuery("from PfidInfo pfidInfo "
											 		    + "where pfidInfo.pfname = '"
													    + pfname + "'")
											 .list()
											 .iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}
	
	private void saveOrUpdatePFidInfo(Session session, PfidInfo pfidInfo) {
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(pfidInfo);
		transaction.commit();
	}
	
	private boolean addTask(DispatchTask task, long srcSvrId, long srcDiskId) {
		checkNotNull(task, "DispatchTask");
		if (task.getMd5() == null) {
			return false;
		}
		
		if (srcSvrId != 0 && srcDiskId != 0 &&
				(!manager.getCompleteMap().containsKey(task.getPfid()) 
						|| !manager.getCompleteMap().get(task.getPfid()).contains(srcDiskId))) {
			FidSynTaskMap fidSynTaskMap = null;
			if (manager.getCompleteMap().containsKey(task.getPfid())) {
				fidSynTaskMap = manager.getCompleteMap().get(task.getPfid());
			} else {
				fidSynTaskMap = new FidSynTaskMap();
			}
			fidSynTaskMap.put(srcDiskId, new SynTaskBuilder().destDiskId(srcDiskId)
															 .destSvrId(srcSvrId)
															 .opt(task.getOpt())
															 .priority(task.getPriority())
															 .pfid(task.getPfid())
															 .status(TASK_COMPLETE)
															 .childNum(0)
															 .weight(-1)
															 .altWeight(-1)
															 .ispType(0)
															 .svrType(SVR_TYPE_INIT_SRC)
															 .build());
			manager.getCompleteMap().put(task.getPfid(), fidSynTaskMap);
		}
		
		manager.getPfidSrcSrvMap().put(task.getPfid(), srcSvrId);
		manager.getPfidSrcDiskMap().put(task.getPfid(), srcDiskId);
		
		manager.getDispatchTaskQueue().add(task);
		manager.getDispatchTaskMap().put(task.getPfid(), task);
		manager.getTaskTimeoutPriorityQueue().add(new TaskTimeoutBuilder().pfid(task.getPfid())
															 .diskId(0)
															 .timeout(System.currentTimeMillis())
															 .count(0)
															 .build());
		
		return true;
	}
	
}
