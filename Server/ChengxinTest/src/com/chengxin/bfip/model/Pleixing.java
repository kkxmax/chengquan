package com.chengxin.bfip.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chengxin.common.BaseModel;

public class Pleixing extends BaseModel {
	
	private int id;
    private String title = "";
    private int upperId = 0;
    private Date writeTime = new Date();
    private List<Pleixing> children = new ArrayList<Pleixing>();
    
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
	public int getUpperId() {
		return upperId;
	}
	public void setUpperId(int upperId) {
		this.upperId = upperId;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
	public List<Pleixing> getChildren() {
		return children;
	}
	public void setChildren(List<Pleixing> children) {
		this.children = children;
	}
}

