package com.ku6.cdn.dispatcher.common.thread;

import static com.ku6.cdn.dispatcher.common.Constrants.*;
import static com.ku6.cdn.dispatcher.common.util.TypeUtil.*;

import java.util.Iterator;

import org.hibernate.Session;

import com.ku6.cdn.dispatcher.Manager;
import com.ku6.cdn.dispatcher.common.TaskReference.Reference;
import com.ku6.cdn.dispatcher.common.TimeTask;
import com.ku6.cdn.dispatcher.common.util.Mappings;
import com.ku6.cdn.dispatcher.common.util.TaskBuilder;
import com.ku6.cdn.dispatcher.common.util.TaskReferenceBuilder;
import com.ku6.cdn.dispatcher.common.util.TimeTaskBuilder;

public class TaskProducerRunnable implements Runnable {
	
	private final Manager manager;
	
	public TaskProducerRunnable(Manager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		createTasks(manager.getUtccSessionFactory().openSession());
	}
	
	@SuppressWarnings("rawtypes")
	private void createTasks(Session session) {
		Integer ri = STATS_OK;
		String rv = "dispatch_ok";
		Iterator iterator = session.createQuery("select task.sid, task.vid, task.taskId, "
											  + "task.videoDomain, task.videoPath, "
											  + "task.dispatchSrcIp, task.dispatchStatus, task.priority "
											  + "from Task task "
											  + "where task.dispatchStatus in (1, 2)")
								   .list()
								   .iterator();
		while (iterator.hasNext()) {
			Object[] tuple = (Object[]) iterator.next();
			String path = (String) tuple[4];
//			String domain = (String) tuple[3];
			String srcIp = (String) tuple[5];
			Long taskId = (Long) tuple[2];
			Integer priority = (Integer) tuple[7];
			Long vid = (Long) tuple[1];
			Long sid = (Long) tuple[0];
			Integer dispatchStatus = (Integer) tuple[6];
			
			if (path == null || srcIp == null) {
				Mappings.TASK_REFS.put(taskId, new TaskReferenceBuilder().build());
				manager.getTimeTaskQueue().add(new TimeTaskBuilder()
														.taskId(taskId)
														.type(TASK_ID_TYPE)
														.sid(sid)
														.vid(vid)
														.priority(BASE_PRIORITY + priority)
														.ri(STATS_DOMAIN_PATH_EQ_NULL)
														.rv("ip_or_path_eq_null")
														.runTs(System.currentTimeMillis())
														.startTime(0)
														.endTime(0)
														.status(STATUS_FAIL)
														.build());
				continue;
			}
			if (!Mappings.SERVER_STATUS.containsKey(srcIp) 
					|| !Mappings.SERVER_STATUS.get(srcIp)) {
				// TODO: do some log about server status
				manager.getTimeTaskQueue().add(new TimeTaskBuilder()
														.taskId(taskId)
														.type(TASK_ID_TYPE)
														.sid(sid)
														.vid(vid)
														.priority(BASE_PRIORITY + priority)
														.ri(STATS_DOMAIN_PATH_EQ_NULL)
														.rv("src_ip_error")
														.runTs(System.currentTimeMillis())
														.startTime(0)
														.endTime(0)
														.status(STATUS_FAIL)
														.build());
				Mappings.TASK_REFS.put(taskId, new TaskReferenceBuilder()
														.okRef(0)
														.completeRef(0)
														.type(REPORT_ERROR)
														.build());
				continue;
			}
			
			if (Mappings.TASK_REFS.containsKey(taskId)) {
				continue;
			}
			
			String[] vecPath = path.split(",");
			Mappings.TASK_REFS.put(taskId, new TaskReferenceBuilder()
													.okRef(0)
													.completeRef(0)
													.type(dispatchStatus == 1 ? REPORT_RUNING_AUDIT : REPORT_RUNING_COMPLETE)
													.build());
			boolean berr = false;
			for (String eachPath : vecPath) {
				int pos = eachPath.lastIndexOf("/");
				String filename = "";
				if (pos != -1) {
					filename = eachPath.substring(pos + 1);
				}
				
				String[] vecKey = filename.split("-");
				if (vecKey.length < 10) {
					berr = true;
					ri = STATS_FILE_INVALID;
					rv = "file_invalid";
					break;
				}
				
				if (!Mappings.IP2DISK.containsKey(srcIp)) {
					berr = true;
					ri = STATS_NOT_EXIST_DISKID;
					rv = "not_exist_diskid";
					break;
				}
				
				if (vecKey[7].length() == 0 || vecKey[9].length() <= 10) {
					berr = true;
					ri = STATS_NOT_EXIST_DISKID;
					rv = "not_exist_diskid";
					break;
				}
				
				if (dispatchStatus == 1) {
					Mappings.TASK_REFS.get(taskId).incref(Reference.OK_REF);
				}
				Mappings.TASK_REFS.get(taskId).incref(Reference.COMPLETE_REF);
				
				manager.getTaskQueue().add(new TaskBuilder()
													.path(eachPath)
													.host(srcIp)
													.vid(vid)
													.taskId(taskId)
													.priority(BASE_PRIORITY + priority)
													.size(Long.valueOf(vecKey[7]))
													.md5(vecKey[9])
													.build());
			}
			if (berr) {
				Mappings.TASK_REFS.put(taskId, new TaskReferenceBuilder()
														.okRef(0)
														.completeRef(0)
														.build());
				manager.getTimeTaskQueue().add(new TimeTaskBuilder()
														.taskId(taskId)
														.type(TASK_ID_TYPE)
														.sid(sid)
														.vid(vid)
														.priority(BASE_PRIORITY + priority)
														.ri(ri)
														.rv(rv)
														.build());
			} else {
				TimeTask timeTask = new TimeTaskBuilder()
												.taskId(taskId)
												.type(TASK_ID_TYPE)
												.sid(sid)
												.vid(vid)
												.priority(BASE_PRIORITY + priority)
												.ri(ri)
												.rv(rv)
												.runTs(System.currentTimeMillis() + TASK_ID_TIME)
												.startTime(System.currentTimeMillis())
												.endTime(0)
												.status(STATUS_UNKNOWN)
												.build();
				manager.getTimeTaskQueue().add(timeTask);
				manager.getTimeTaskPriorityQueue().add(timeTask);
			}
			
		}
	}

	public Manager getManager() {
		return manager;
	}

}
