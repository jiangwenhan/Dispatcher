package com.ku6.cdn.dispatcher.common.entity.system;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ku6.cdn.dispatcher.common.entity.PersistenceObject;



@Entity
@Table(name = "tb_disk_info")
public class DiskInfo extends PersistenceObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5023960811802755203L;
	private Long id;
	private Long diskId;
	private Long gslbDiskId;
	private Long svrId;
	private Long nodeId;
	private Integer inUse;
	private Integer diskStatus;
	private Long diskErrno;
	private Integer port;
	private String rootDir;  
	private String mount;
	private Long diskSize;
	private Long usedSize;
	private Long inodeTotal;
	private Long inodeUsed;
	private Date updateTime;
	private Integer isCache;
	
	public DiskInfo() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", length = 11, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "disk_id", length = 11, nullable = false, unique = true)
	public Long getDiskId() {
		return diskId;
	}

	public void setDiskId(Long diskId) {
		this.diskId = diskId;
	}

	@Column(name = "gslb_disk_id", length = 11, nullable = true, unique = false)
	public Long getGslbDiskId() {
		return gslbDiskId;
	}

	public void setGslbDiskId(Long gslbDiskId) {
		this.gslbDiskId = gslbDiskId;
	}

	@Column(name = "svr_id", length = 11, nullable = true, unique = false)
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

	@Column(name = "in_use", length = 4, nullable = true, unique = false)
	public Integer getInUse() {
		return inUse;
	}

	public void setInUse(Integer inUse) {
		this.inUse = inUse;
	}

	@Column(name = "disk_status", length = 4, nullable = true, unique = false)
	public Integer getDiskStatus() {
		return diskStatus;
	}

	public void setDiskStatus(Integer diskStatus) {
		this.diskStatus = diskStatus;
	}

	@Column(name = "disk_errno", length = 11, nullable = true, unique = false)
	public Long getDiskErrno() {
		return diskErrno;
	}

	public void setDiskErrno(Long diskErrno) {
		this.diskErrno = diskErrno;
	}

	@Column(name = "port", length = 6, nullable = true, unique = false)
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@Column(name = "root_dir", length = 10, nullable = true, unique = false)
	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	@Column(name = "mount", length = 32, nullable = true, unique = false)
	public String getMount() {
		return mount;
	}

	public void setMount(String mount) {
		this.mount = mount;
	}

	@Column(name = "disk_size", length = 11, nullable = true, unique = false)
	public Long getDiskSize() {
		return diskSize;
	}

	public void setDiskSize(Long diskSize) {
		this.diskSize = diskSize;
	}

	@Column(name = "used_size", length = 11, nullable = true, unique = false)
	public Long getUsedSize() {
		return usedSize;
	}

	public void setUsedSize(Long usedSize) {
		this.usedSize = usedSize;
	}

	@Column(name = "inode_total", length = 11, nullable = true, unique = false)
	public Long getInodeTotal() {
		return inodeTotal;
	}

	public void setInodeTotal(Long inodeTotal) {
		this.inodeTotal = inodeTotal;
	}

	@Column(name = "inode_used", length = 11, nullable = true, unique = false)
	public Long getInodeUsed() {
		return inodeUsed;
	}

	public void setInodeUsed(Long inodeUsed) {
		this.inodeUsed = inodeUsed;
	}

	@Column(name = "update_time", nullable = true, unique = false)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "is_cache", length = 4, nullable = true, unique = false)
	public Integer getIsCache() {
		return isCache;
	}

	public void setIsCache(Integer isCache) {
		this.isCache = isCache;
	}

}
