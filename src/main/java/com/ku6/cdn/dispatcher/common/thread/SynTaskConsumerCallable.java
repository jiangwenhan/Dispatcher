package com.ku6.cdn.dispatcher.common.thread;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.concurrent.Callable;

import com.ku6.cdn.dispatcher.Manager;
import com.ku6.cdn.dispatcher.common.SynTask;
import com.ku6.cdn.dispatcher.common.entity.PFidInfo;
import com.ku6.cdn.dispatcher.common.util.Mappings;

public class SynTaskConsumerCallable implements Callable<Boolean> {
	
	private final Manager manager;
	private final SynTask synTask;
	
	public SynTaskConsumerCallable(Manager manager, SynTask synTask) {
		this.manager = manager;
		this.synTask = synTask;
	}

	@Override
	public Boolean call() throws Exception {
		PFidInfo pFidInfo = Mappings.PFID_MAP.get(synTask.getPfid());
		if (pFidInfo == null) {
			// TODO: do some log
			return false;
		} else if (pFidInfo.getMd5() == null) {
			// TODO: do some log
			return false;
		}
		
		if (!Mappings.DISKS.containsKey(synTask.getDestDiskId())) {
			return false;
		}
		
		if (synTask.getOpt() == OPT_SYN) {
			if (!Mappings.DISKS.containsKey(synTask.getSrcDiskId())) {
				// TODO: do some log
				return false;
			}
			if (synTask.getAltSrcDiskId() != 0 && !Mappings.DISKS.containsKey(synTask.getAltSrcDiskId())) {
				// TODO: do some log
				return false;
			}
		}
		updateTask();
		
		return true;
	}
	
	private boolean updateTask() {
		return false;
	}

}
