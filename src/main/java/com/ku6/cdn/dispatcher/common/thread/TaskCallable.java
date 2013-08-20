package com.ku6.cdn.dispatcher.common.thread;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.List;
import java.util.concurrent.Callable;

import org.hibernate.Session;

import com.ku6.cdn.dispatcher.common.Task;
import com.ku6.cdn.dispatcher.common.collection.TaskQueue;
import com.ku6.cdn.dispatcher.common.entity.utcc.SourceVideoInfo;

public class TaskCallable implements Callable<Void> {
	
	private Session session;
	
	public TaskCallable() {
	}

	@Override
	public Void call() throws Exception {
		while (!TaskQueue.isEmpty()) {
			Task task = TaskQueue.poll();
			if (task == null)
				return null;
			String customer = "ku6";
			String storeType = "groupCold";
			String uploadServer = "ku6";
			Integer enableUpdate = 1;
			Integer fileNum = 1;
			List<SourceVideoInfo> infos = findSourceVideoInfoByTaskId(task.getVid());
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
			// TODO
			// combine query string
			// communicate with TCC
			// handle the response
		}
		
		return null;
	}
	
	private List<SourceVideoInfo> findSourceVideoInfoByTaskId(Long vid) {
		String hql = "from SourceVideoInfo "
				+ "where vid = " + vid;
		List<SourceVideoInfo> infos = session.createQuery(hql).list();
		return infos;
	}

}
