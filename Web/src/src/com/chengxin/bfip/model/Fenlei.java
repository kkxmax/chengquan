package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;
import com.chengxin.common.DateTimeUtil;

public class Fenlei extends BaseModel{
	
	private int id;
    private String title = "";
    private int leixing = 0;
    private String leixingName;
    private Date writeTime = new Date();
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getLeixing() {
		return leixing;
	}
	public void setLeixing(int leixing) {
		this.leixing = leixing;
	}
	public String getLeixingName() {
		return leixingName;
	}
	public void setLeixingName(String leixingName) {
		this.leixingName = leixingName;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
    
}

