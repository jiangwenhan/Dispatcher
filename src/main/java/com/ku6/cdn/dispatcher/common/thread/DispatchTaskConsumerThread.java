package com.ku6.cdn.dispatcher.common.thread;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import com.google.common.collect.Lists;
import com.ku6.cdn.dispatcher.Manager;
import com.ku6.cdn.dispatcher.common.DispatchTask;
import com.ku6.cdn.dispatcher.common.SynTask;
import com.ku6.cdn.dispatcher.common.collection.FidSynTaskMap;
import com.ku6.cdn.dispatcher.common.util.Mappings;
import com.ku6.cdn.dispatcher.common.util.builder.impl.TaskStatusBuilder;

public class DispatchTaskConsumerThread implements Callable<Boolean> {
	
	private final Manager manager;
	private final DispatchTask dispatchTask;
	
	public DispatchTaskConsumerThread(Manager manager, DispatchTask dispatchTask) {
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
			checkMap(dispatchTask.getPfid(), 0);
			return true;
		}
		
		if (!manager.containsKeyInCompleteMap(dispatchTask.getPfid())) {
			checkMap(dispatchTask.getPfid(), 0);
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
		
		doTask(dispatchTask.getPfid(), COMMON_NORMAL_DO_TASK);
		checkMap(dispatchTask.getPfid(), 0);
		
		return true;
	}
	
	private void doTask(long pfid, int type) {
		if (type == COMMON_NORMAL_DO_TASK) {
			FidSynTaskMap srcTaskMap = manager.getCompleteMap().get(pfid);
			if (!manager.getWaitSrcMap().containsKey(pfid) || manager.getWaitSrcMap().get(pfid).size() == 0) {
				// TODO: do some log 
				return;
			}
			FidSynTaskMap destTaskMap = manager.getWaitSrcMap().get(pfid);
			List<SynTask> destTask = Lists.newLinkedList();
			List<SynTask> srcTask = Lists.newLinkedList();
			List<SynTask> srcInit = Lists.newLinkedList();
			for (Entry<Long, SynTask> entry : srcTaskMap.entrySet()) {
				if (entry.getValue().getSvrType() == SVR_TYPE_INIT_SRC
						&& entry.getValue().getChildNum() < manager.getSrcSelector().getChildNum()) {
					srcInit.add(entry.getValue());
				} else if (entry.getValue().getChildNum() < manager.getSrcSelector().getChildNum()
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
			if (manager.getDispatchTaskMap().containsKey(pfid)) {
				if (srcTask.isEmpty()) {
					manager.getSrcSelector().FindSrc(destTask, srcInit, manager.getDispatchTaskMap().get(pfid).isUniqIsp());
				} else {
					manager.getSrcSelector().FindSrc(destTask, srcTask, manager.getDispatchTaskMap().get(pfid).isUniqIsp());
				}
			} else {
				if (srcTask.isEmpty()) {
					manager.getSrcSelector().FindSrc(destTask, srcInit);
				} else {
					manager.getSrcSelector().FindSrc(destTask, srcTask);
				}
			}
			
			if (!srcTask.isEmpty()) {
				for (SynTask synTask : srcTask) {
					if (manager.getCompleteMap().get(pfid).contains(synTask.getDestDiskId())) {
						manager.getCompleteMap().get(pfid).put(synTask.getDestDiskId(), synTask);
					}
				}
			} else {
				for (SynTask synTask : srcInit) {
					if (manager.getCompleteMap().get(pfid).contains(synTask.getDestDiskId())) {
						manager.getCompleteMap().get(pfid).put(synTask.getDestDiskId(), synTask);
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
					manager.getWaitSrcMap().get(pfid).remove(synTask.getDestDiskId());
					manager.getWaitDispatchMap().get(pfid).put(synTask.getDestDiskId(), synTask);
					manager.getSynTaskQueue().add(synTask);
				}
			}
			
		} else if (type == COMMON_FULL_DO_TASK) {
			
		}
	}
	
	private boolean checkMap(long pfid, int state) {
		if (state != 0 || 
				(!manager.getWaitDispatchMap().containsKey(pfid) || manager.getWaitDispatchMap().get(pfid).size() == 0)
				&& (!manager.getWaitSrcMap().containsKey(pfid) || manager.getWaitSrcMap().get(pfid).size() == 0)) {
			if (manager.getDispatchTaskMap().containsKey(pfid)) {
				if (manager.getDispatchTaskMap().get(pfid).getTaskNum() == 0) {
					// TODO: do some log
					return true;
				}
				manager.getCompleteQueue().add(new TaskStatusBuilder()
											.pfid(pfid)
											.status(state)
											.build());
				manager.getDispatchTaskMap().remove(pfid);
			} else {
				// TODO: do some log
			}
			manager.getWaitDispatchMap().remove(pfid);
			manager.getWaitSrcMap().remove(pfid);
			manager.getCompleteMap().remove(pfid);
			return true;
		}
		
		int count = 0;
		if (manager.getCompleteMap().containsKey(pfid)) {
			count = manager.getCompleteMap().get(pfid).size();
		}
		if (count >= manager.getConfFileNum()) {
			long totalNum = manager.getWaitSrcMap().get(pfid).size() + manager.getWaitDispatchMap().get(pfid).size() + count;
			// TODO: do some log
			if (count * 100 / totalNum >= manager.getConfFileRate()
					&& (!manager.constainsKeyInStoreDiskMap(pfid) || manager.storeDiskMapGet(pfid).size() == 0)) {
				if (manager.getDispatchTaskMap().containsKey(pfid)) {
					if (manager.getDispatchTaskMap().get(pfid).getTaskNum() == 0) {
						// TODO: do some log
						return true;
					}
					manager.getCompleteQueue().add(new TaskStatusBuilder()
												.pfid(pfid)
												.status(state)
												.build());
					// TODO: do some log
					manager.getDispatchTaskMap().remove(pfid);
				} else {
					// TODO: do some log
				}
				manager.getWaitDispatchMap().remove(pfid);
				manager.getWaitSrcMap().remove(pfid);
				manager.getCompleteMap().remove(pfid);
				return true;
			}
			if (manager.getDispatchTaskMap().containsKey(pfid)) {
				if (manager.getDispatchTaskMap().get(pfid).getPriority() != 3) {
					manager.getDispatchTaskMap().get(pfid).setPriority(3);
					manager.getDispatchTaskQueue().add(manager.getDispatchTaskMap().get(pfid));
				}
				manager.getWaitSrcMap().remove(pfid);
			}
		}
		return true;
	}

}
