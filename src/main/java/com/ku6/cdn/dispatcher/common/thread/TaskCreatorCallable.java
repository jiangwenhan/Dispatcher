package com.ku6.cdn.dispatcher.common.thread;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.Iterator;
import java.util.concurrent.Callable;

import org.hibernate.Session;

import com.ku6.cdn.dispatcher.common.Task;
import com.ku6.cdn.dispatcher.common.TaskReference;
import com.ku6.cdn.dispatcher.common.TimeTask;
import com.ku6.cdn.dispatcher.common.collection.TaskQueue;
import com.ku6.cdn.dispatcher.common.collection.TimeTaskPriorityQueue;
import com.ku6.cdn.dispatcher.common.collection.TimeTaskQueue;
import com.ku6.cdn.dispatcher.common.resource.TranscodeServerResource;
import com.ku6.cdn.dispatcher.common.util.Mappings;

public class TaskCreatorCallable implements Callable<Void> {
	
	private Session session;
	
	public TaskCreatorCallable() {
	}

	@Override
	public Void call() throws Exception {
		createTasks();
		return null;
	}
	/**
	 * In Object list:
	 * [0] : sid
	 * [1] : vid
	 * [2] : taskId
	 * [3] : videoDomain
	 * [4] : videoPath
	 * [5] : dispatchSrcIp
	 * [6] : dispatchStatus
	 * [7] : priority
	 */
	private void createTasks() {
		String hql = "select sid, vid, taskId, videoDomain, videoPath, "
				+ "dispatchSrcIp, dispatchStatus, priority "
				+ "from Task "
				+ "where dispatchStatus (1, 2)";
		Integer ri = STATS_OK;
		String rv = "dispatch_ok";
		Iterator iterator = session.createQuery(hql).list().iterator();
		while (iterator.hasNext()) {
			Object[] tuple = (Object[]) iterator.next();
			String path = (String) tuple[4];
			String domain = (String) tuple[3];
			String srcIp = (String) tuple[5];
			Long taskId = (Long) tuple[2];
			Integer priority = (Integer) tuple[7];
			Integer vid = (Integer) tuple[1];
			Integer sid = (Integer) tuple[0];
			Integer dispatchStatus = (Integer) tuple[6];
			
			if (path == null || srcIp == null) {
				TimeTask timeTask = new TimeTask();
				timeTask.setType(TASK_ID_TYPE);
				timeTask.setTaskId(taskId);
				timeTask.setSid(sid);
				timeTask.setVid(vid);
				timeTask.setPriority(BASE_PRIORITY + priority);
				timeTask.setRi(STATS_DOMAIN_PATH_EQ_NULL);
				timeTask.setRv("ip_or_path_eq_null");
				timeTask.setRunTs(System.currentTimeMillis());
				timeTask.setStartTime(0);
				timeTask.setEndTime(0);
				timeTask.setStatus(STATUS_FAIL);
				
				Mappings.TASK_REFS.put(taskId, new TaskReference()
													.setOkRef(0)
													.setCompleteRef(0)
													.setType(REPORT_ERROR));
				TimeTaskQueue.push(timeTask);
				continue;
			}
			if (!TranscodeServerResource.containsKey(srcIp) 
					|| !TranscodeServerResource.get(srcIp)) {
				TimeTask timeTask = new TimeTask();
				timeTask.setType(TASK_ID_TYPE);
				timeTask.setTaskId(taskId);
				timeTask.setSid(sid);
				timeTask.setVid(vid);
				timeTask.setPriority(BASE_PRIORITY + priority);
				timeTask.setRi(STATS_DOMAIN_PATH_EQ_NULL);
				timeTask.setRv("src_ip_error");
				timeTask.setRunTs(System.currentTimeMillis());
				timeTask.setStartTime(0);
				timeTask.setEndTime(0);
				timeTask.setStatus(STATUS_FAIL);
				
				TimeTaskQueue.push(timeTask);
				continue;
			}
			if (Mappings.TASK_REFS.containsKey(taskId)) {
				continue;
			}
			String[] vecPath = path.split(",");
			Mappings.TASK_REFS.put(taskId, new TaskReference()
											.setOkRef(0)
											.setCompleteRef(0)
											.setType(dispatchStatus == 1 ? REPORT_RUNING_AUDIT : REPORT_RUNING_COMPLETE));
			boolean berr = false;
			Integer status = ERR_STATUS;
			for (String eachPath : vecPath) {
				Task task = new Task();
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
					status = 3;
					ri = STATS_NOT_EXIST_DISKID;
					rv = "not_exist_diskid";
					break;
				}
				task.setPath(eachPath);
				task.setHost(srcIp);
				task.setVid(vid);
				task.setTaskId(taskId);
				task.setPriority(BASE_PRIORITY + priority);
				
				if (vecKey[7].length() == 0 || vecKey[9].length() <= 10) {
					berr = true;
					status = 3;
					ri = STATS_NOT_EXIST_DISKID;
					rv = "not_exist_diskid";
					break;
				}
				task.setSize(Integer.parseInt(vecKey[7]));
				task.setMd5(vecKey[9]);
				if (dispatchStatus == 1) {
					Mappings.increaseTaskOkRef(taskId);
				}
				Mappings.increaseTaskCompleteRef(taskId);
				TaskQueue.push(task);
			}
			TimeTask timeTask = new TimeTask();
			timeTask.setType(TASK_ID_TYPE);
			timeTask.setTaskId(taskId);
			timeTask.setSid(sid);
			timeTask.setVid(vid);
			timeTask.setPriority(BASE_PRIORITY + priority);
			timeTask.setRi(ri);
			timeTask.setRv(rv);
			if (berr) {
				timeTask.setRunTs(System.currentTimeMillis());
				timeTask.setStartTime(0);
				timeTask.setEndTime(0);
				timeTask.setStatus(STATUS_FAIL);
				Mappings.TASK_REFS.put(taskId, new TaskReference()
													.setOkRef(0)
													.setCompleteRef(0));
				TimeTaskQueue.push(timeTask);
			} else {
				timeTask.setRunTs(System.currentTimeMillis() + TASK_ID_TIME);
				timeTask.setStartTime(System.currentTimeMillis());
				timeTask.setEndTime(0);
				timeTask.setStatus(STATUS_UNKNOWN);
				
				TimeTaskPriorityQueue.add(timeTask);
			}
			
		}
	}

}
