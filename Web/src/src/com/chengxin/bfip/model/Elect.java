package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;

public class Elect extends BaseModel {
	
    private int id;
    private int kind;
    private int accountId;
    private int hotId;
    private int estimateId;
    private int owner;
    private Date writeTime = new Date();
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getHotId() {
		return hotId;
	}
	public void setHotId(int hotId) {
		this.hotId = hotId;
	}
	public int getEstimateId() {
		return estimateId;
	}
	public void setEstimateId(int estimateId) {
		this.estimateId = estimateId;
	}
	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
    
}