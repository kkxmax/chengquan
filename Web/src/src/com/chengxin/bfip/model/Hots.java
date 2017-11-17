package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;
import com.chengxin.common.DateTimeUtil;

public class Hots extends BaseModel{

	private int id;
    private String title = "";
    private int kind = 0;
    private int visitCnt = 0;
	private int commentCnt = 0;
	private int remarkCnt = 0;
	private int shareCnt = 0;
	private Date upTime = null;
    private Date downTime = null;
    private String content = "";
    private int st = 0;
    private int accountId = 0;
    private Date writeTime = new Date();
    private String kindName;
    private String stName;
    private int xyleixingLevel1Id;
    private String xyleixingLevel1Name;
    
    
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
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public int getVisitCnt() {
		return visitCnt;
	}
	public void setVisitCnt(int visitCnt) {
		this.visitCnt = visitCnt;
	}
	public int getCommentCnt() {
		return commentCnt;
	}
	public void setCommentCnt(int commentCnt) {
		this.commentCnt = commentCnt;
	}
	public int getRemarkCnt() {
		return remarkCnt;
	}
	public void setRemarkCnt(int remarkCnt) {
		this.remarkCnt = remarkCnt;
	}
	public int getShareCnt() {
		return shareCnt;
	}
	public void setShareCnt(int shareCnt) {
		this.shareCnt = shareCnt;
	}
	public Date getUpTime() {
		return upTime;
	}
	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}
	public Date getDownTime() {
		return downTime;
	}
	public void setDownTime(Date downTime) {
		this.downTime = downTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getSt() {
		return st;
	}
	public void setSt(int st) {
		this.st = st;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
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
	public String getStName() {
		return stName;
	}
	public void setStName(String stName) {
		this.stName = stName;
	}
	public int getXyleixingLevel1Id() {
		return xyleixingLevel1Id;
	}
	public void setXyleixingLevel1Id(int xyleixingLevel1Id) {
		this.xyleixingLevel1Id = xyleixingLevel1Id;
	}
	public String getXyleixingLevel1Name() {
		return xyleixingLevel1Name;
	}
	public void setXyleixingLevel1Name(String xyleixingLevel1Name) {
		this.xyleixingLevel1Name = xyleixingLevel1Name;
	}
    
}
