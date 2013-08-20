package com.ku6.cdn.dispatcher.common.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "tb_group_info")
public class GroupInfo {
	
	private Long id;
	private Long groupId;
	private String groupName;
	private Integer groupType;
	private Integer inUse;
	private Long ispId;
	private Date addTime;
	private Date modifyTime;
	private String remark;
	
	public GroupInfo() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", length = 11, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "group_id", length = 11, nullable = false, unique = false)
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "group_name", length = 128, nullable = true, unique = false)
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name = "group_type", length = 4, nullable = true, unique = false)
	public Integer getGroupType() {
		return groupType;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	@Column(name = "in_use", length = 4, nullable = true, unique = false)
	public Integer getInUse() {
		return inUse;
	}

	public void setInUse(Integer inUse) {
		this.inUse = inUse;
	}

	@Column(name = "isp_id", length = 11, nullable = true, unique = false)
	public Long getIspId() {
		return ispId;
	}

	public void setIspId(Long ispId) {
		this.ispId = ispId;
	}

	@Column(name = "add_time", nullable = true, unique = false)
	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	@Column(name = "modify_time", nullable = true, unique = false)
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "remark", nullable = true, unique = false)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
