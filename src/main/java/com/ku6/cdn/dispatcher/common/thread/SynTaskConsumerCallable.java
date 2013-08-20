package com.ku6.cdn.dispatcher.common.thread;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;

import com.ku6.cdn.dispatcher.Manager;
import com.ku6.cdn.dispatcher.common.SynTask;
import com.ku6.cdn.dispatcher.common.entity.system.PFidInfo;
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
		
		String reportPath = StringUtils.stripStart(pFidInfo.getPfname(), "/");
		String path = "/" + reportPath;
		String destPath = Mappings.DISKS.get(synTask.getDestDiskId()).getDisk().getMount() + path;
		StringBuilder sqlBuilder = new StringBuilder();
		
		if (synTask.getOpt() == OPT_SYN) {
			if (!Mappings.DISKS.containsKey(synTask.getSrcDiskId())) {
				// TODO: do some log
				return false;
			}
			if (synTask.getAltSrcDiskId() != 0 && !Mappings.DISKS.containsKey(synTask.getAltSrcDiskId())) {
				// TODO: do some log
				return false;
			}
			String srcPath = Mappings.DISKS.get(synTask.getSrcDiskId()).getDisk().getMount() + path;
			String altSrcPath = Mappings.DISKS.containsKey(synTask.getAltSrcDiskId()) ? 
					Mappings.DISKS.get(synTask.getAltSrcDiskId()).getDisk().getMount() + path : null;
			
			sqlBuilder.append("INSERT INTO t_tasks_2_")
					  .append(synTask.getDestSvrId() % 256)
					  .append("(pfid, md5, report_path, dest_disk_id, dest_svr_id, dest_path, ")
					  .append("src_svr_id, src_path, alt_svr_id, alt_path, ")
					  .append("weight, exp_speed, alt_exp_speed,priority,operation,level,")
					  .append("task_type,task_status, create_time) ")
					  .append("VALUES(")
					  .append(synTask.getPfid() + ", ")
					  .append("\"" + pFidInfo.getMd5() + "\", ")
					  .append("\"" + reportPath + "\", ")
					  .append(synTask.getDestDiskId() + ", ")
					  .append(synTask.getDestSvrId() + ", ")
					  .append("\"" + destPath + "\", ")
					  .append(synTask.getSrcSvrId() + ", ")
					  .append("\"" + srcPath + "\", ")
					  .append(synTask.getAltSrcSvrId() + ", ")
					  .append("\"" + altSrcPath + ", ")
					  .append(synTask.getWeight() + ", ")
					  .append(synTask.getSpeed() + ", ")
					  .append(synTask.getAltSpeed() + ", ")
					  .append(synTask.getPriority() + ", ")
					  .append(synTask.getOpt() + ", ")
					  .append(synTask.getLevel() + ", ")
					  .append(0 + ", ")
					  .append(1 + ", ")
					  .append("NOW())");
		} else if (synTask.getOpt() == OPT_DEL) {
			sqlBuilder.append("INSERT INTO t_tasks_2_")
					  .append(synTask.getDestSvrId() % 256)
					  .append("(pfid,report_path, dest_disk_id, dest_svr_id, dest_path,")
					  .append("priority,operation,task_type,task_status,create_time)")
					  .append("VALUES(")
					  .append(synTask.getPfid() + ", ")
					  .append("\"" + reportPath + "\", ")
					  .append(synTask.getDestDiskId() + ", ")
					  .append(synTask.getDestSvrId() + ", ")
					  .append("\"" + destPath + "\", ")
					  .append(synTask.getPriority() + ", ")
					  .append(synTask.getOpt() + ", ")
					  .append(0 + ", ")
					  .append(1 + ", ")
					  .append("NOW())");
		}
		
		manager.getCdnSystemSessionFactory().getCurrentSession().createSQLQuery(sqlBuilder.toString());
		
		return true;
	}

	public Manager getManager() {
		return manager;
	}
	
}
