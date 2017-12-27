package com.chengxin.bfip.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chengxin.common.BaseModel;

public class Estimate extends BaseModel{

	private int id;
	private int type;
	private int upperId;
	private int accountId;
	private int hotId;
	private int owner;
	private String content;
	private int kind;
	private int method;
	private String reason;
	private String imgPath1;
	private String imgPath2;
	private String imgPath3;
	private String imgPath4;
	private String imgPath5;
	private Date updateTime;
	private Date writeTime = new Date();
	
	private String writeTimeString;
    List<String> imgPaths = new ArrayList<String>();
	private String kindName;
	private String methodName;
	private String ownerLogo;
	private String ownerMobile;
	private int ownerAkind;
	private String ownerRealname;
	private String ownerEnterName;
	private String ownerName;
	private int electCnt;
	private int targetAccountAkind;
	private String targetAccountMobile;
	private String targetAccountLogo;
	private String targetAccountRealname;
	private String targetAccountEnterName;
	private String targetAccountName;
	private int targetAccountElectCnt;
	private int targetAccountFeedbackCnt;
	private int ownerFeedbackCnt;
	private int isFalse;
	private int isElectedByMe;
	private List<Estimate> replys = new ArrayList<Estimate>();
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUpperId() {
		return upperId;
	}
	public void setUpperId(int upperId) {
		this.upperId = upperId;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getHotId() {
		return hotId;
	}
	public void setHotId(int hotId) {
		this.hotId = hotId;
	}
	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public int getMethod() {
		return method;
	}
	public void setMethod(int method) {
		this.method = method;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getTargetAccountMobile() {
		return targetAccountMobile;
	}
	public void setTargetAccountMobile(String targetAccountMobile) {
		this.targetAccountMobile = targetAccountMobile;
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
	public int getElectCnt() {
		return electCnt;
	}
	public void setElectCnt(int electCnt) {
		this.electCnt = electCnt;
	}
	public List<Estimate> getReplys() {
		return replys;
	}
	public void setReplys(List<Estimate> replys) {
		this.replys = replys;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getOwnerAkind() {
		return ownerAkind;
	}
	public void setOwnerAkind(int ownerAkind) {
		this.ownerAkind = ownerAkind;
	}
	public String getOwnerRealname() {
		return ownerRealname;
	}
	public void setOwnerRealname(String ownerRealname) {
		this.ownerRealname = ownerRealname;
	}
	public String getOwnerEnterName() {
		return ownerEnterName;
	}
	public void setOwnerEnterName(String ownerEnterName) {
		this.ownerEnterName = ownerEnterName;
	}
	public int getTargetAccountAkind() {
		return targetAccountAkind;
	}
	public void setTargetAccountAkind(int targetAccountAkind) {
		this.targetAccountAkind = targetAccountAkind;
	}
	public String getTargetAccountRealname() {
		return targetAccountRealname;
	}
	public void setTargetAccountRealname(String targetAccountRealname) {
		this.targetAccountRealname = targetAccountRealname;
	}
	public String getTargetAccountEnterName() {
		return targetAccountEnterName;
	}
	public void setTargetAccountEnterName(String targetAccountEnterName) {
		this.targetAccountEnterName = targetAccountEnterName;
	}
	public int getTargetAccountElectCnt() {
		return targetAccountElectCnt;
	}
	public void setTargetAccountElectCnt(int targetAccountElectCnt) {
		this.targetAccountElectCnt = targetAccountElectCnt;
	}
	public int getTargetAccountFeedbackCnt() {
		return targetAccountFeedbackCnt;
	}
	public void setTargetAccountFeedbackCnt(int targetAccountFeedbackCnt) {
		this.targetAccountFeedbackCnt = targetAccountFeedbackCnt;
	}
	public String getWriteTimeString() {
		return writeTimeString;
	}
	public void setWriteTimeString(String writeTimeString) {
		this.writeTimeString = writeTimeString;
	}
	public List<String> getImgPaths() {
		return imgPaths;
	}
	public void setImgPaths(List<String> imgPaths) {
		this.imgPaths = imgPaths;
	}
	public String getTargetAccountLogo() {
		return targetAccountLogo;
	}
	public void setTargetAccountLogo(String targetAccountLogo) {
		this.targetAccountLogo = targetAccountLogo;
	}
	public String getOwnerLogo() {
		return ownerLogo;
	}
	public void setOwnerLogo(String ownerLogo) {
		this.ownerLogo = ownerLogo;
	}
	public int getIsFalse() {
		return isFalse;
	}
	public void setIsFalse(int isFalse) {
		this.isFalse = isFalse;
	}
	public int getIsElectedByMe() {
		return isElectedByMe;
	}
	public void setIsElectedByMe(int isElectedByMe) {
		this.isElectedByMe = isElectedByMe;
	}
	public String getOwnerMobile() {
		return ownerMobile;
	}
	public void setOwnerMobile(String ownerMobile) {
		this.ownerMobile = ownerMobile;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getTargetAccountName() {
		return targetAccountName;
	}
	public void setTargetAccountName(String targetAccountName) {
		this.targetAccountName = targetAccountName;
	}
	public int getOwnerFeedbackCnt() {
		return ownerFeedbackCnt;
	}
	public void setOwnerFeedbackCnt(int ownerFeedbackCnt) {
		this.ownerFeedbackCnt = ownerFeedbackCnt;
	}

}
