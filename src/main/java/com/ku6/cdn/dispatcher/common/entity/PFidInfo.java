package com.ku6.cdn.dispatcher.common.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name = "pfid_info", uniqueConstraints = {
		@UniqueConstraint(columnNames = "pfname")
})
public class PFidInfo {
	
	private Long pfid;
	private String pfname;
	private Long cfid;
	private Long size;
	private String md5;
	private Long vid;
	private Integer rate;
	private Integer priority;
	private Integer fileStatus;
	private Integer fileNum;
	private String storeType;
	private String customer;
	private Date addTime;
	private Integer deleteState;
	private Integer enableUpdate;
	private String source;
	
	public PFidInfo() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pfid", length = 20, nullable = false, unique = true)
	public Long getPfid() {
		return pfid;
	}

	public void setPfid(Long pfid) {
		this.pfid = pfid;
	}

	@Column(name = "pfname", length = 255, nullable = false, unique = true)
	public String getPfname() {
		return pfname;
	}

	public void setPfname(String pfname) {
		this.pfname = pfname;
	}

	@Column(name = "cfid", length = 20, nullable = false)
	public Long getCfid() {
		return cfid;
	}

	public void setCfid(Long cfid) {
		this.cfid = cfid;
	}

	@Column(name = "size", length = 20, nullable = false)
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Column(name = "md5", length = 32, nullable = true)
	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	@Column(name = "vid", length = 20, nullable = false)
	public Long getVid() {
		return vid;
	}

	public void setVid(Long vid) {
		this.vid = vid;
	}

	@Column(name = "rate", length = 11, nullable = false)
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	@Column(name = "priority", length = 11, nullable = false)
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Column(name = "file_status", length = 11, nullable = false)
	public Integer getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(Integer fileStatus) {
		this.fileStatus = fileStatus;
	}

	@Column(name = "file_num", length = 11, nullable = false)
	public Integer getFileNum() {
		return fileNum;
	}

	public void setFileNum(Integer fileNum) {
		this.fileNum = fileNum;
	}

	@Column(name = "store_type", length = 20, nullable = false)
	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	@Column(name = "customer", length = 20, nullable = true)
	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	@Column(name = "add_time", nullable = true)
	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	@Column(name = "delete_state", length = 4, nullable = false)
	public Integer getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(Integer deleteState) {
		this.deleteState = deleteState;
	}

	@Column(name = "enable_update", length = 4, nullable = false)
	public Integer getEnableUpdate() {
		return enableUpdate;
	}

	public void setEnableUpdate(Integer enableUpdate) {
		this.enableUpdate = enableUpdate;
	}

	@Column(name = "source", length = 255, nullable = true)
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
