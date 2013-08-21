package com.ku6.cdn.dispatcher.common.util.builder.impl;

import java.sql.Date;

import com.ku6.cdn.dispatcher.common.entity.system.PfidInfo;
import com.ku6.cdn.dispatcher.common.util.builder.Builder;

public class PfidInfoBuilder implements Builder<PfidInfo> {
	
	private Long _pfid = null;
	private String _pfname;
	private Long _cfid = 0L;
	private Long _size = 0L;
	private String _md5;
	private Long _vid = 0L;
	private Integer _rate = 0;
	private Integer _priority = 0;
	private Integer _fileStatus = 0;
	private Integer _fileNum = 0;
	private String _storeType;
	private String _customer;
	private Date _addTime;
	private Integer _deleteState = 0;
	private Integer _enableUpdate = 0;
	private String _source;

	@Override
	public PfidInfo build() {
		PfidInfo pfidInfo = new PfidInfo();
		pfidInfo.setPfid(_pfid);
		pfidInfo.setPfname(_pfname);
		pfidInfo.setCfid(_cfid);
		pfidInfo.setSize(_size);
		pfidInfo.setMd5(_md5);
		pfidInfo.setVid(_vid);
		pfidInfo.setRate(_rate);
		pfidInfo.setPriority(_priority);
		pfidInfo.setFileStatus(_fileStatus);
		pfidInfo.setFileNum(_fileNum);
		pfidInfo.setStoreType(_storeType);
		pfidInfo.setCustomer(_customer);
		pfidInfo.setAddTime(_addTime);
		pfidInfo.setDeleteState(_deleteState);
		pfidInfo.setEnableUpdate(_enableUpdate);
		pfidInfo.setSource(_source);
		return pfidInfo;
	}
	
	public PfidInfoBuilder pfid(long pfid) {
		this._pfid = pfid;
		return this;
	}
	
	public PfidInfoBuilder pfname(String pfname) {
		this._pfname = pfname;
		return this;
	}
	
	public PfidInfoBuilder cfid(long cfid) {
		this._cfid = cfid;
		return this;
	}
	
	public PfidInfoBuilder size(long size) {
		this._size = size;
		return this;
	}
	
	public PfidInfoBuilder md5(String md5) {
		this._md5 = md5;
		return this;
	}
	
	public PfidInfoBuilder vid(long vid) {
		this._vid = vid;
		return this;
	}
	
	public PfidInfoBuilder rate(int rate) {
		this._rate = rate;
		return this;
	}
	
	public PfidInfoBuilder priority(int priority) {
		this._priority = priority;
		return this;
	}
	
	public PfidInfoBuilder fileStatus(int fileStatus) {
		this._fileStatus = fileStatus;
		return this;
	}
	
	public PfidInfoBuilder fileNum(int fileNum) {
		this._fileNum = fileNum;
		return this;
	}
	
	public PfidInfoBuilder storeType(String storeType) {
		this._storeType = storeType;
		return this;
	}
	
	public PfidInfoBuilder customer(String customer) {
		this._customer = customer;
		return this;
	}
	
	public PfidInfoBuilder addTime(Date addTime) {
		this._addTime = addTime;
		return this;
	}
	
	public PfidInfoBuilder deleteState(int deleteState) {
		this._deleteState = deleteState;
		return this;
	}
	
	public PfidInfoBuilder enableUpdate(int enableUpdate) {
		this._enableUpdate = enableUpdate;
		return this;
	}
	
	public PfidInfoBuilder source(String source) {
		this._source = source;
		return this;
	}

}
