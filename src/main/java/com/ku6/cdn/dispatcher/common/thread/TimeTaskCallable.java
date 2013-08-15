package com.ku6.cdn.dispatcher.common.thread;

import static com.ku6.cdn.dispatcher.common.Constrants.*;
import static com.ku6.cdn.dispatcher.common.util.Mappings.*;

import java.util.concurrent.Callable;

import com.ku6.cdn.dispatcher.common.TimeTask;
import com.ku6.cdn.dispatcher.common.collection.TimeTaskPriorityQueue;
import com.ku6.cdn.dispatcher.common.collection.TimeTaskQueue;

public class TimeTaskCallable implements Callable<Void> {

	@Override
	public Void call() throws Exception {
		while (!TimeTaskQueue.isEmpty()) {
			TimeTask timeTask = TimeTaskQueue.poll();
			long diff = System.currentTimeMillis() - timeTask.getStartTime();
			if (timeTask.getType() == TASK_ID_TYPE) {
				Integer type = TASK_REFS.get(timeTask.getTaskId()).getType();
				Integer status = timeTask.getStatus();
				if (status == STATUS_FAIL
						|| type == REPORT_AUDIT
						|| type == REPORT_COMPLETE
						|| type == REPORT_ERROR) {
					if (type == REPORT_AUDIT) {
						timeTask.setStatus(STATUS_AUDIT);
						TASK_REFS.put(timeTask.getTaskId(), 
								TASK_REFS.get(timeTask.getTaskId())
									.setType(REPORT_AUDIT_REPORT));
					} else if (type == REPORT_COMPLETE) {
						timeTask.setStatus(STATUS_COMPLETE);
					} else if (type == REPORT_ERROR) {
						timeTask.setStatus(STATUS_FAIL);
					}
					// TODO:
					// Access TCC
					
				}
			} else if (timeTask.getType() == PFID_TYPE) {
				Integer type = TASK_REFS.get(timeTask.getTaskId()).getType();
				boolean brepair = false;
				if (diff / DELAY_TIME == timeTask.getDelay()) {
					timeTask.delay++;
					brepair = true;
				}
				if (type == REPORT_RUNING_COMPLETE /*&& IsDispatchOK2*/) {
					if (TASK_REFS.get(timeTask.getTaskId()).getCompleteRef() <= 0) {
						TASK_REFS.get(timeTask.getTaskId()).setType(REPORT_COMPLETE);
					}
				} else {
					int state = 0;
					if (type == REPORT_PFID_ERROR || type == REPORT_ERROR) {
						state = -1;
						if (TASK_REFS.get(timeTask.getTaskId()).getOkRef() <= 0) {
							TASK_REFS.get(timeTask.getTaskId()).setType(REPORT_ERROR);
						}
					} else if (type == REPORT_RUNING_AUDIT /*&& (state = checkAuditOK2) != 0*/) {
						if (state == -1) {
							if (TASK_REFS.get(timeTask.getTaskId()).getOkRef() <= 0) {
								TASK_REFS.get(timeTask.getTaskId()).setType(REPORT_ERROR);
							} else {
								TASK_REFS.get(timeTask.getTaskId()).setType(REPORT_PFID_ERROR);
							}
						}
					} else if (type == REPORT_RUNING_AUDIT) {
						if (!timeTask.isbRunstat()) {
							if (TASK_REFS.get(timeTask.getTaskId()).getOkRef() <= 0) {
								if (TASK_REFS.get(timeTask.getTaskId()).getType() != REPORT_PFID_ERROR) {
									TASK_REFS.get(timeTask.getTaskId()).setType(REPORT_AUDIT);
								} else {
									TASK_REFS.get(timeTask.getTaskId()).setType(REPORT_ERROR);
								}
							}
							timeTask.setbRunstat(true);
						}
					}
					if (state != -1) {
						timeTask.setRunTs(System.currentTimeMillis() + PFID_TIME);
//						TimeTaskPriorityQueue.add(timeTask);
					}
				} 
			}
			
		}

		return null;
	}

}
