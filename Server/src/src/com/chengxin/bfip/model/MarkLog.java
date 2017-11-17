package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;

public class MarkLog extends BaseModel {
	
    private int id;
    private int accountId;
    private int kind;
    private int estimateKind;
    private int estimateMethod;
    private int errorKind;
    private int pmark;
    private int nmark;
    private int senderId;
    private Date writeTime = new Date();
    
    private String writeTimeString;
    private String msg;
    private String estimateKindName;
    private String estimateMethodName;
    private String errorKindName;
    private String accountRealname;
    private String accountEnterName;
    private int accountAkind;
    private int senderAkind;
    private String senderRealname;
    private String senderEnterName;
    
    
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
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public int getPmark() {
		return pmark;
	}
	public void setPmark(int pmark) {
		this.pmark = pmark;
	}
	public int getNmark() {
		return nmark;
	}
	public void setNmark(int nmark) {
		this.nmark = nmark;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
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
	public int getEstimateKind() {
		return estimateKind;
	}
	public void setEstimateKind(int estimateKind) {
		this.estimateKind = estimateKind;
	}
	public int getEstimateMethod() {
		return estimateMethod;
	}
	public void setEstimateMethod(int estimateMethod) {
		this.estimateMethod = estimateMethod;
	}
	public int getErrorKind() {
		return errorKind;
	}
	public void setErrorKind(int errorKind) {
		this.errorKind = errorKind;
	}
	public String getEstimateKindName() {
		return estimateKindName;
	}
	public void setEstimateKindName(String estimateKindName) {
		this.estimateKindName = estimateKindName;
	}
	public String getEstimateMethodName() {
		return estimateMethodName;
	}
	public void setEstimateMethodName(String estimateMethodName) {
		this.estimateMethodName = estimateMethodName;
	}
	public String getErrorKindName() {
		return errorKindName;
	}
	public void setErrorKindName(String errorKindName) {
		this.errorKindName = errorKindName;
	}
	public String getAccountRealname() {
		return accountRealname;
	}
	public void setAccountRealname(String accountRealname) {
		this.accountRealname = accountRealname;
	}
	public String getAccountEnterName() {
		return accountEnterName;
	}
	public void setAccountEnterName(String accountEnterName) {
		this.accountEnterName = accountEnterName;
	}
	public int getAccountAkind() {
		return accountAkind;
	}
	public void setAccountAkind(int accountAkind) {
		this.accountAkind = accountAkind;
	}
	public int getSenderAkind() {
		return senderAkind;
	}
	public void setSenderAkind(int senderAkind) {
		this.senderAkind = senderAkind;
	}
	public String getSenderRealname() {
		return senderRealname;
	}
	public void setSenderRealname(String senderRealname) {
		this.senderRealname = senderRealname;
	}
	public String getSenderEnterName() {
		return senderEnterName;
	}
	public void setSenderEnterName(String senderEnterName) {
		this.senderEnterName = senderEnterName;
	}

}