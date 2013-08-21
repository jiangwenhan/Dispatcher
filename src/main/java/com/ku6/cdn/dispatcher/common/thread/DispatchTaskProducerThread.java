package com.ku6.cdn.dispatcher.common.thread;

import java.util.Iterator;
import java.util.concurrent.Callable;

import org.hibernate.Session;

import com.ku6.cdn.dispatcher.Manager;
import com.ku6.cdn.dispatcher.common.entity.system.PfidInfo;
import com.ku6.cdn.dispatcher.common.util.builder.impl.TaskStatusBuilder;

public class DispatchTaskProducerThread implements Callable<Long> {
	
	private final Manager manager;
	private final long startId;
	
	public DispatchTaskProducerThread(Manager manager, long startId) {
		this.manager = manager;
		this.startId = startId;
	}

	@Override
	public Long call() throws Exception {
		return loadPfidInfoData(manager.getCdnSystemSessionFactory().openSession(), startId);
	}
	
	@SuppressWarnings("unchecked")
	private Long loadPfidInfoData(Session session, long startId) {
		Iterator<PfidInfo> iterator = session.createQuery("from PfidInfo pfidInfo "
														+ "where pfidInfo.pfid = " + startId + " "
														+ "and pfidInfo.fileStatus = 1 "
														+ "limit 100000")
											 .list()
											 .iterator();
		while (iterator.hasNext()) {
			PfidInfo pfidInfo = iterator.next();
			if (pfidInfo.getMd5() == null) {
				manager.getCompleteQueue().add(new TaskStatusBuilder().pfid(pfidInfo.getPfid())
																	  .status(TASK_DSP_SRC_MD5_INVALID)
																	  .build());
				continue;
			}
		}
		
		return 0L;
	}

}
