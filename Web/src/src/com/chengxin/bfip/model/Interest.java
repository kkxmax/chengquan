package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;

public class Interest extends BaseModel {
	
	private int id;
    private int target;
    private int owner;
    private Date writeTime = new Date();
    
    private String writeTimeString;
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
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
	public String getWriteTimeString() {
		return writeTimeString;
	}
	public void setWriteTimeString(String writeTimeString) {
		this.writeTimeString = writeTimeString;
	}
    
}