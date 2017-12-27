package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;

public class ReadStatus extends BaseModel{
	
	private int id;
	private int ownerId;
	private int estimateId;
	private Date writeTime = new Date();
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public int getEstimateId() {
		return estimateId;
	}
	public void setEstimateId(int estimateId) {
		this.estimateId = estimateId;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
	
}
