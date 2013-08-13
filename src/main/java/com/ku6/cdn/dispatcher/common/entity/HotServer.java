package com.ku6.cdn.dispatcher.common.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "tb_hot_server")
public class HotServer {

	private Long svrId;
	private Long nodeId;
	private String diskIds;
	private Integer maxratio;
	private Date insertTime;
	
	public HotServer() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "svr_id", length = 11, nullable = false, unique = true)
	public Long getSvrId() {
		return svrId;
	}

	public void setSvrId(Long svrId) {
		this.svrId = svrId;
	}

	@Column(name = "node_id", length = 11, nullable = true, unique = false)
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "disk_ids", length = 500, nullable = true, unique = false)
	public String getDiskIds() {
		return diskIds;
	}

	public void setDiskIds(String diskIds) {
		this.diskIds = diskIds;
	}

	@Column(name = "maxratio", length = 4, nullable = true, unique = false)
	public Integer getMaxratio() {
		return maxratio;
	}

	public void setMaxratio(Integer maxratio) {
		this.maxratio = maxratio;
	}

	@Column(name = "inserttime", nullable = true, unique = false)
	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	
}
