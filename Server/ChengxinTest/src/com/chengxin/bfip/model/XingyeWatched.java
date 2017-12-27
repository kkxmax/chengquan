package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;

public class XingyeWatched extends BaseModel {
	
    private int id;
    private int accountId;
    private int xyleixingId;
    private Date writeTime = new Date();
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getXyleixingId() {
		return xyleixingId;
	}
	public void setXyleixingId(int xyleixingId) {
		this.xyleixingId = xyleixingId;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
	
}