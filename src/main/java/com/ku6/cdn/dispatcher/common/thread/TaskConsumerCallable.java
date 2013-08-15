package com.ku6.cdn.dispatcher.common.thread;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import com.google.common.collect.Lists;
import com.ku6.cdn.dispatcher.Manager;
import com.ku6.cdn.dispatcher.common.DispatchTask;
import com.ku6.cdn.dispatcher.common.SynTask;
import com.ku6.cdn.dispatcher.common.util.Mappings;

public class TaskConsumerCallable implements Callable<Boolean> {
	
	private final Manager manager;
	private final DispatchTask dispatchTask;
	
	public TaskConsumerCallable(Manager manager, DispatchTask dispatchTask) {
		this.manager = manager;
		this.dispatchTask = dispatchTask;
	}

	@Override
	public Boolean call() throws Exception {
		if (manager.containsKeyInDispatchMap(dispatchTask.getPfid())) {
			DispatchTask tempTask = manager.dispatchMapGet(dispatchTask.getPfid());
			tempTask.setTaskNum(tempTask.getTaskNum() + 1);
			manager.dispatchMapInsert(dispatchTask.getPfid(), tempTask);
		}

		List<SynTask> destTasks = Lists.newArrayList();
		if (dispatchTask.getOpt() == OPT_SYN) {
			return manager.createSynTask(dispatchTask, destTasks);
		} else if (dispatchTask.getOpt() == OPT_DEL) {
			manager.deleteSynTask(dispatchTask);
			manager.checkMap(dispatchTask.getPfid(), 0);
			return true;
		}
		
		if (!manager.containsKeyInCompleteMap(dispatchTask.getPfid())) {
			manager.checkMap(dispatchTask.getPfid(), 0);
			return true;
		}
		
		for (SynTask synTask : destTasks) {
			manager.waitSrcMapGet(synTask.getPfid()).put(synTask.getDestDiskId(), synTask);
			if (Mappings.DISKS.containsKey(synTask.getDestDiskId())
					&& Mappings.DISKS.get(synTask.getDestDiskId()).getStoreType() == COMMON_NORMAL_NODE_TYPE) {
				Set<Long> set = manager.storeDiskMapGet(synTask.getPfid());
				set.add(synTask.getDestDiskId());
				manager.storeDiskMapInsert(synTask.getPfid(), set);
			}
		}
		
		manager.doTask(dispatchTask.getPfid(), COMMON_NORMAL_DO_TASK);
		manager.checkMap(dispatchTask.getPfid(), 0);
		
		return true;
	}

}
