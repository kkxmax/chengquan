package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;

public class ReqCode extends BaseModel {
	
    private int id;
    private String reqCode;
    private int sender;
    private int status;
    private Date writeTime = new Date();
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReqCode() {
		return reqCode;
	}
	public void setReqCode(String reqCode) {
		this.reqCode = reqCode;
	}
	public int getSender() {
		return sender;
	}
	public void setSender(int sender) {
		this.sender = sender;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
    
}