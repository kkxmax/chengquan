package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;

public class Notice extends BaseModel {
	
    private int id;
    private int type;
    private int accountId;
    private int kind;
    private int subKind;
    private String msgTitle;
    private String msgContent;
    private int inviteeId;
    private int estimateId;
    private int errorId;
    private int status = NoticeDAO.NOTICE_ST_NEW;
    private Date writeTime = new Date();
    
    private String writeTimeString;
    private String kindName;
    private String statusName;
    private int testStatus;
    private int inviteeAkind;
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
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
	public String getKindName() {
		return kindName;
	}
	public void setKindName(String kindName) {
		this.kindName = kindName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getWriteTimeString() {
		return writeTimeString;
	}
	public void setWriteTimeString(String writeTimeString) {
		this.writeTimeString = writeTimeString;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getSubKind() {
		return subKind;
	}
	public void setSubKind(int subKind) {
		this.subKind = subKind;
	}
	public int getInviteeId() {
		return inviteeId;
	}
	public void setInviteeId(int inviteeId) {
		this.inviteeId = inviteeId;
	}
	public int getEstimateId() {
		return estimateId;
	}
	public void setEstimateId(int estimateId) {
		this.estimateId = estimateId;
	}
	public int getErrorId() {
		return errorId;
	}
	public void setErrorId(int errorId) {
		this.errorId = errorId;
	}
	public int getTestStatus() {
		return testStatus;
	}
	public void setTestStatus(int testStatus) {
		this.testStatus = testStatus;
	}
	public int getInviteeAkind() {
		return inviteeAkind;
	}
	public void setInviteeAkind(int inviteeAkind) {
		this.inviteeAkind = inviteeAkind;
	}

}