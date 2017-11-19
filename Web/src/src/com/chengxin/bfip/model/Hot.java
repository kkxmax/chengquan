package com.chengxin.bfip.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chengxin.common.BaseModel;

public class Hot extends BaseModel{

	private int id;
    private String title = "";
    private int xyleixingId = 0;
    private int visitCnt = 0;
	private int shareCnt = 0;
	private Date upTime = null;
    private Date downTime = null;
    private String content = "";
    private String imgPath1 = "";
    private String imgPath2 = "";
    private String imgPath3 = "";
    private String imgPath4 = "";
    private String imgPath5 = "";
    private String imgPath6 = "";
    private String imgPath7 = "";
    private String imgPath8 = "";
    private String imgPath9 = "";
    private String imgPath10 = "";
    private String imgPath11 = "";
    private String imgPath12 = "";
    private String imgPath13 = "";
    private String imgPath14 = "";
    private String imgPath15 = "";
    private int status = 0;
    private Date writeTime = new Date();
    
    private String writeTimeString;
    List<String> imgPaths = new ArrayList<String>();
    private String xyleixingName;
    private String statusName;
    private int xyleixingLevel1Id;
    private String xyleixingLevel1Name;
    private int commentCnt = 0;
	private int electCnt = 0;
	private int isFavourite = 0;
	private int isElectedByMe = 0;

	
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
	public int getElectCnt() {
		return electCnt;
	}
	public void setElectCnt(int electCnt) {
		this.electCnt = electCnt;
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
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
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
	public int getXyleixingId() {
		return xyleixingId;
	}
	public void setXyleixingId(int xyleixingId) {
		this.xyleixingId = xyleixingId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImgPath1() {
		return imgPath1;
	}
	public void setImgPath1(String imgPath1) {
		this.imgPath1 = imgPath1;
	}
	public String getImgPath2() {
		return imgPath2;
	}
	public void setImgPath2(String imgPath2) {
		this.imgPath2 = imgPath2;
	}
	public String getImgPath3() {
		return imgPath3;
	}
	public void setImgPath3(String imgPath3) {
		this.imgPath3 = imgPath3;
	}
	public String getImgPath4() {
		return imgPath4;
	}
	public void setImgPath4(String imgPath4) {
		this.imgPath4 = imgPath4;
	}
	public String getImgPath5() {
		return imgPath5;
	}
	public void setImgPath5(String imgPath5) {
		this.imgPath5 = imgPath5;
	}
	public String getImgPath6() {
		return imgPath6;
	}
	public void setImgPath6(String imgPath6) {
		this.imgPath6 = imgPath6;
	}
	public String getImgPath7() {
		return imgPath7;
	}
	public void setImgPath7(String imgPath7) {
		this.imgPath7 = imgPath7;
	}
	public String getImgPath8() {
		return imgPath8;
	}
	public void setImgPath8(String imgPath8) {
		this.imgPath8 = imgPath8;
	}
	public String getImgPath9() {
		return imgPath9;
	}
	public void setImgPath9(String imgPath9) {
		this.imgPath9 = imgPath9;
	}
	public String getImgPath10() {
		return imgPath10;
	}
	public void setImgPath10(String imgPath10) {
		this.imgPath10 = imgPath10;
	}
	public String getImgPath11() {
		return imgPath11;
	}
	public void setImgPath11(String imgPath11) {
		this.imgPath11 = imgPath11;
	}
	public String getImgPath12() {
		return imgPath12;
	}
	public void setImgPath12(String imgPath12) {
		this.imgPath12 = imgPath12;
	}
	public String getImgPath13() {
		return imgPath13;
	}
	public void setImgPath13(String imgPath13) {
		this.imgPath13 = imgPath13;
	}
	public String getImgPath14() {
		return imgPath14;
	}
	public void setImgPath14(String imgPath14) {
		this.imgPath14 = imgPath14;
	}
	public String getImgPath15() {
		return imgPath15;
	}
	public void setImgPath15(String imgPath15) {
		this.imgPath15 = imgPath15;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getXyleixingName() {
		return xyleixingName;
	}
	public void setXyleixingName(String xyleixingName) {
		this.xyleixingName = xyleixingName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public int getIsFavourite() {
		return isFavourite;
	}
	public void setIsFavourite(int isFavourite) {
		this.isFavourite = isFavourite;
	}
	public List<String> getImgPaths() {
		return imgPaths;
	}
	public void setImgPaths(List<String> imgPaths) {
		this.imgPaths = imgPaths;
	}
	public String getWriteTimeString() {
		return writeTimeString;
	}
	public void setWriteTimeString(String writeTimeString) {
		this.writeTimeString = writeTimeString;
	}
	public int getIsElectedByMe() {
		return isElectedByMe;
	}
	public void setIsElectedByMe(int isElectedByMe) {
		this.isElectedByMe = isElectedByMe;
	}
    
}
