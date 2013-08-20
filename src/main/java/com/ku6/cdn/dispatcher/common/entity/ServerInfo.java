package com.ku6.cdn.dispatcher.common.entity;

import static com.ku6.cdn.dispatcher.common.Constrants.*;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tb_server_info", uniqueConstraints = {
		@UniqueConstraint(columnNames = "ip"),
		@UniqueConstraint(columnNames = "ip2")
})
public class ServerInfo {
	
	private Integer svrId;
	private Integer svrType;
	private Integer nodeId;
	private Integer groupId;
	private String ip;
	private String ip2;
	private Integer inUse;
	private Date addTime;
	private Date modifyTime;
	private Integer doScanNginx;
	private String nginxVersion;
	private Integer nginxStatus;
	private Integer liveStatus;
	private Date nginxUpdateTime;
	private String dispClientVersion;
	private Integer dispStatus;
	private Date dispUpdateTime;
	private String remark;
	private BigDecimal loadAvg;
	private BigDecimal rootUsg;
	private Integer isCache;
	
	public ServerInfo() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = SERVER_SVR_ID, length = 11, nullable = false, unique = true)
	public Integer getSvrId() {
		return svrId;
	}

	public void setSvrId(Integer svrId) {
		this.svrId = svrId;
	}

	@Column(name = SERVER_SVR_TYPE, length = 10, nullable = true, unique = false)
	public Integer getSvrType() {
		return svrType;
	}

	public void setSvrType(Integer svrType) {
		this.svrType = svrType;
	}

	@Column(name = SERVER_NODE_ID, length = 11, nullable = true, unique = false)
	public Integer getNodeId() {
		return nodeId;
	}

	
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = SERVER_GROUP_ID, length = 11, nullable = true, unique = false)
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Column(name = SERVER_IP, length = 32, nullable = true, unique = true)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = SERVER_IP2, length = 32, nullable = true, unique = true)
	public String getIp2() {
		return ip2;
	}

	public void setIp2(String ip2) {
		this.ip2 = ip2;
	}

	@Column(name = SERVER_IN_USE, length = 4, nullable = true, unique = false)
	public Integer getInUse() {
		return inUse;
	}

	public void setInUse(Integer inUse) {
		this.inUse = inUse;
	}

	@Column(name = SERVER_ADD_TIME, nullable = true, unique = false)
	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	@Column(name = SERVER_MODIFY_TIME, nullable = true, unique = false)
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = SERVER_DO_SCAN_NGINX, length = 4, nullable = true, unique = false)
	public Integer getDoScanNginx() {
		return doScanNginx;
	}

	public void setDoScanNginx(Integer doScanNginx) {
		this.doScanNginx = doScanNginx;
	}

	@Column(name = SERVER_NGINX_VERSION, length = 64, nullable = true, unique = false)
	public String getNginxVersion() {
		return nginxVersion;
	}

	public void setNginxVersion(String nginxVersion) {
		this.nginxVersion = nginxVersion;
	}

	@Column(name = SERVER_NGINX_STATUS, length = 4, nullable = true, unique = false)
	public Integer getNginxStatus() {
		return nginxStatus;
	}

	public void setNginxStatus(Integer nginxStatus) {
		this.nginxStatus = nginxStatus;
	}

	@Column(name = SERVER_LIVE_STATUS, length = 4, nullable = true, unique = false)
	public Integer getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(Integer liveStatus) {
		this.liveStatus = liveStatus;
	}

	@Column(name = SERVER_NGINX_UPDATE_TIME, nullable = true, unique = false)
	public Date getNginxUpdateTime() {
		return nginxUpdateTime;
	}

	public void setNginxUpdateTime(Date nginxUpdateTime) {
		this.nginxUpdateTime = nginxUpdateTime;
	}

	@Column(name = SERVER_DISP_CLINET_VERSION, length = 20, nullable = true, unique = false)
	public String getDispClientVersion() {
		return dispClientVersion;
	}

	public void setDispClientVersion(String dispClientVersion) {
		this.dispClientVersion = dispClientVersion;
	}

	@Column(name = SERVER_DISP_STATUS, length = 4, nullable = true, unique = false)
	public Integer getDispStatus() {
		return dispStatus;
	}

	public void setDispStatus(Integer dispStatus) {
		this.dispStatus = dispStatus;
	}

	@Column(name = SERVER_DISP_UPDATE_TIME, nullable = true, unique = false)
	public Date getDispUpdateTime() {
		return dispUpdateTime;
	}

	public void setDispUpdateTime(Date dispUpdateTime) {
		this.dispUpdateTime = dispUpdateTime;
	}

	@Column(name = SERVER_REMARK, nullable = true, unique = false)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = SERVER_LOAD_AVG, precision = 10, scale = 5, nullable = true, unique = false)
	public BigDecimal getLoadAvg() {
		return loadAvg;
	}

	public void setLoadAvg(BigDecimal loadAvg) {
		this.loadAvg = loadAvg;
	}

	@Column(name = SERVER_ROOT_USG, precision = 10, scale = 5, nullable = true, unique = false)
	public BigDecimal getRootUsg() {
		return rootUsg;
	}

	public void setRootUsg(BigDecimal rootUsg) {
		this.rootUsg = rootUsg;
	}

	@Column(name = SERVER_IS_CACHE, length = 4, nullable = true, unique = false)
	public Integer getIsCache() {
		return isCache;
	}

	public void setIsCache(Integer isCache) {
		this.isCache = isCache;
	}

}
