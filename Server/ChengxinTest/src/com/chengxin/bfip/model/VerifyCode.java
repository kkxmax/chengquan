package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;

public class VerifyCode extends BaseModel {
	
    private int id;
    private String mobile;
    private String verifyCode;
    private Date writeTime = new Date();
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}

}