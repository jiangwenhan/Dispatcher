package com.ku6.cdn.dispatcher.common.entity;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "tb_node_info")
public class Node {

	private Long id;
	private Long nodeId;
	private String nodeName;
	private Integer nodeType;
	private Long groupId;
	private Long dupNodeId;
	private Integer inUse;
	private Long pid;
	private Long aid;
	private Long ispId;
	private String vpid;
	private BigDecimal maxBand;
	private BigDecimal realBand;
	private BigDecimal bandUsage;
	private Integer needDispatch;
	private Integer isDispatchSource;
	private Integer isGslbSource;
	private String serviceArea;
	private Integer useP2p;
	private Integer usePlay;
	private Integer useLive;
	private Integer useDload;
	private Long ahtime;
	private Integer doScanNginx;
	private String nodeSwitch;
	private String oidlist;
	private String nodeSwitch2;
	private String oidlist2;
	private Date addTime;
	private Date modifyTime;
	private String remark;

	public Node() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", length = 11, nullable = false, unique = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "node_id", length = 11, nullable = false)
	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "node_name", length = 128)
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	@Column(name = "node_type", length = 6)
	public Integer getNodeType() {
		return nodeType;
	}

	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}

	@Column(name = "group_id", length = 11)
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "dup_node_id", length = 11)
	public Long getDupNodeId() {
		return dupNodeId;
	}

	public void setDupNodeId(Long dupNodeId) {
		this.dupNodeId = dupNodeId;
	}

	@Column(name = "in_use", length = 4)
	public Integer getInUse() {
		return inUse;
	}

	public void setInUse(Integer inUse) {
		this.inUse = inUse;
	}

	@Column(name = "pid", length = 11)
	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	@Column(name = "aid", length = 11)
	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}

	@Column(name = "isp_id", length = 11)
	public Long getIspId() {
		return ispId;
	}

	public void setIspId(Long ispId) {
		this.ispId = ispId;
	}

	@Column(name = "vpid", length = 32)
	public String getVpid() {
		return vpid;
	}

	public void setVpid(String vpid) {
		this.vpid = vpid;
	}

	@Column(name = "max_band")
	public BigDecimal getMaxBand() {
		return maxBand;
	}

	public void setMaxBand(BigDecimal maxBand) {
		this.maxBand = maxBand;
	}

	@Column(name = "real_band")
	public BigDecimal getRealBand() {
		return realBand;
	}

	public void setRealBand(BigDecimal realBand) {
		this.realBand = realBand;
	}

	@Column(name = "band_usage")
	public BigDecimal getBandUsage() {
		return bandUsage;
	}

	public void setBandUsage(BigDecimal bandUsage) {
		this.bandUsage = bandUsage;
	}

	@Column(name = "need_dispatch", length = 4)
	public Integer getNeedDispatch() {
		return needDispatch;
	}

	public void setNeedDispatch(Integer needDispatch) {
		this.needDispatch = needDispatch;
	}

	@Column(name = "is_dispatch_source", length = 4)
	public Integer getIsDispatchSource() {
		return isDispatchSource;
	}

	public void setIsDispatchSource(Integer isDispatchSource) {
		this.isDispatchSource = isDispatchSource;
	}

	@Column(name = "is_gslb_source", length = 4)
	public Integer getIsGslbSource() {
		return isGslbSource;
	}

	public void setIsGslbSource(Integer isGslbSource) {
		this.isGslbSource = isGslbSource;
	}

	@Column(name = "service_area")
	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	@Column(name = "use_p2p", length = 4)
	public Integer getUseP2p() {
		return useP2p;
	}

	public void setUseP2p(Integer useP2p) {
		this.useP2p = useP2p;
	}

	@Column(name = "use_play", length = 4)
	public Integer getUsePlay() {
		return usePlay;
	}

	public void setUsePlay(Integer usePlay) {
		this.usePlay = usePlay;
	}

	@Column(name = "use_live", length = 4)
	public Integer getUseLive() {
		return useLive;
	}

	public void setUseLive(Integer useLive) {
		this.useLive = useLive;
	}

	@Column(name = "use_dload", length = 4)
	public Integer getUseDload() {
		return useDload;
	}

	public void setUseDload(Integer useDload) {
		this.useDload = useDload;
	}

	@Column(name = "ahtime", length = 11)
	public Long getAhtime() {
		return ahtime;
	}

	public void setAhtime(Long ahtime) {
		this.ahtime = ahtime;
	}

	@Column(name = "do_scan_nginx", length = 4)
	public Integer getDoScanNginx() {
		return doScanNginx;
	}

	public void setDoScanNginx(Integer doScanNginx) {
		this.doScanNginx = doScanNginx;
	}

	@Column(name = "switch", length = 32)
	public String getNodeSwitch() {
		return nodeSwitch;
	}

	public void setNodeSwitch(String nodeSwitch) {
		this.nodeSwitch = nodeSwitch;
	}

	@Column(name = "oidlist")
	public String getOidlist() {
		return oidlist;
	}

	public void setOidlist(String oidlist) {
		this.oidlist = oidlist;
	}

	@Column(name = "switch2", length = 32)
	public String getNodeSwitch2() {
		return nodeSwitch2;
	}

	public void setNodeSwitch2(String nodeSwitch2) {
		this.nodeSwitch2 = nodeSwitch2;
	}

	@Column(name = "oidlist2")
	public String getOidlist2() {
		return oidlist2;
	}

	public void setOidlist2(String oidlist2) {
		this.oidlist2 = oidlist2;
	}

	@Column(name = "add_time")
	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	@Column(name = "modify_time")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
