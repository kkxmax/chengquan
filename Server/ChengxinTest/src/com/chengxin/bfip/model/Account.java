package com.chengxin.bfip.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chengxin.common.BaseModel;

public class Account extends BaseModel {
	
    private int id;
    private String mobile = "";
    private String password = "";
    private int reqCodeId = 0;
    private String token = "";
    private int akind = 0;
    private String code = "";
    private int viewCnt = 0;
    private String logo = "";
    private String realname = "";
    private int enterKind = 0;
    private String enterName = "";
    private String enterNamePinyin = "";
    private int enterId = 0;
    private int xyleixingId = 0;
    private int cityId = 0;
    private String addr = "";
    private String job = "";
    private String weburl = "";
    private String weixin = "";
    private String mainJob = "";
    private String certNum = "";
    private String certImage = "";
    private String enterCertNum = "";
    private String enterCertImage = "";
    private String experience = "";
    private String history = "";
    private String comment = "";
    private String recommend = "";
    private String bossName = "";
    private String bossJob = "";
    private String bossMobile = "";
    private String bossWeixin = "";
    private int testStatus = 0;
    private int banStatus = 0;
    private Date writeTime = new Date();
    
    private String writeTimeString;
    private int credit = 0;
    private int electCnt = 0;
    private int inviteCnt = 0;
    private int feedbackCnt = 0;
    private int positiveFeedbackCnt = 0;
    private int negativeFeedbackCnt = 0;
    private int reqCodeSenderId;
    private String inviterFriendLevel;
    private String reqCodeSenderMobile;
    private int reqCodeSenderAkind;
    private String reqCodeSenderRealname;
    private String reqCodeSenderEnterName;
    private String reqCodeSenderName;
    private String testStatusName;
    private String banStatusName;
    private String enterKindName;    
    private String friend1;
    private String friend1Name;
    private String friend2;
    private String friend3;
    private String cityName;
    private int provinceId;
    private String provinceName;
    private String provCity;
    private String xyName;
    private String xyWatchName;
    private String xyWatchedName;
    private int interested = 0;
    private int isStarInterested = 0;
    private List<Estimate> estimates = new ArrayList<Estimate>();
    private List<Product> products = new ArrayList<Product>();
    private List<Item> items = new ArrayList<Item>();
    private List<Service> services = new ArrayList<Service>();
    
    
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getReqCodeId() {
		return reqCodeId;
	}
	public void setReqCodeId(int reqCodeId) {
		this.reqCodeId = reqCodeId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getBossName() {
		return bossName;
	}
	public void setBossName(String bossName) {
		this.bossName = bossName;
	}
	public int getXyleixingId() {
		return xyleixingId;
	}
	public void setXyleixingId(int xyleixingId) {
		this.xyleixingId = xyleixingId;
	}
	public String getMainJob() {
		return mainJob;
	}
	public void setMainJob(String mainJob) {
		this.mainJob = mainJob;
	}
	public String getCertImage() {
		return certImage;
	}
	public void setCertImage(String certImage) {
		this.certImage = certImage ;
	}
	public String getBossJob() {
		return bossJob;
	}
	public void setBossJob(String bossJob) {
		this.bossJob = bossJob;
	}
	public String getBossMobile() {
		return bossMobile;
	}
	public void setBossMobile(String bossMobile) {
		this.bossMobile = bossMobile;
	}
	public String getBossWeixin() {
		return bossWeixin;
	}
	public void setBossWeixin(String bossWeixin) {
		this.bossWeixin = bossWeixin;
	}
	public int getTestStatus() {
		return testStatus;
	}
	public void setTestStatus(int testStatus) {
		this.testStatus = testStatus;
	}
	public int getBanStatus() {
		return banStatus;
	}
	public void setBanStatus(int banStatus) {
		this.banStatus = banStatus;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
	public String getTestStatusName() {
		return testStatusName;
	}
	public void setTestStatusName(String testStatusName) {
		this.testStatusName = testStatusName;
	}
	public String getBanStatusName() {
		return banStatusName;
	}
	public void setBanStatusName(String banStatusName) {
		this.banStatusName = banStatusName;
	}
	public String getEnterKindName() {
		return enterKindName;
	}
	public void setEnterKindName(String enterKindName) {
		this.enterKindName = enterKindName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAkind() {
		return akind;
	}
	public void setAkind(int akind) {
		this.akind = akind;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getViewCnt() {
		return viewCnt;
	}
	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}
	public int getElectCnt() {
		return electCnt;
	}
	public void setElectCnt(int electCnt) {
		this.electCnt = electCnt;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public int getEnterKind() {
		return enterKind;
	}
	public void setEnterKind(int enterKind) {
		this.enterKind = enterKind;
	}
	public String getEnterName() {
		return enterName;
	}
	public void setEnterName(String enterName) {
		this.enterName = enterName;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getWeburl() {
		return weburl;
	}
	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public String getFriend1() {
		return friend1;
	}
	public void setFriend1(String friend1) {
		this.friend1 = friend1;
	}
	public String getFriend2() {
		return friend2;
	}
	public void setFriend2(String friend2) {
		this.friend2 = friend2;
	}
	public String getFriend3() {
		return friend3;
	}
	public void setFriend3(String friend3) {
		this.friend3 = friend3;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getCertNum() {
		return certNum;
	}
	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}
	public String getEnterCertNum() {
		return enterCertNum;
	}
	public void setEnterCertNum(String enterCertNum) {
		this.enterCertNum = enterCertNum;
	}
	public String getEnterCertImage() {
		return enterCertImage;
	}
	public void setEnterCertImage(String enterCertImage) {
		this.enterCertImage = enterCertImage;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getXyName() {
		return xyName;
	}
	public void setXyName(String xyName) {
		this.xyName = xyName;
	}
	public int getReqCodeSenderId() {
		return reqCodeSenderId;
	}
	public void setReqCodeSenderId(int reqCodeSenderId) {
		this.reqCodeSenderId = reqCodeSenderId;
	}
	public String getReqCodeSenderRealname() {
		return reqCodeSenderRealname;
	}
	public void setReqCodeSenderRealname(String reqCodeSenderRealname) {
		this.reqCodeSenderRealname = reqCodeSenderRealname;
	}
	public String getReqCodeSenderEnterName() {
		return reqCodeSenderEnterName;
	}
	public void setReqCodeSenderEnterName(String reqCodeSenderEnterName) {
		this.reqCodeSenderEnterName = reqCodeSenderEnterName;
	}
	public int getInterested() {
		return interested;
	}
	public void setInterested(int interested) {
		this.interested = interested;
	}
	public int getFeedbackCnt() {
		return feedbackCnt;
	}
	public void setFeedbackCnt(int feedbackCnt) {
		this.feedbackCnt = feedbackCnt;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public List<Service> getServices() {
		return services;
	}
	public void setServices(List<Service> services) {
		this.services = services;
	}
	public int getIsStarInterested() {
		return isStarInterested;
	}
	public void setIsStarInterested(int isStarInterested) {
		this.isStarInterested = isStarInterested;
	}
	public int getPositiveFeedbackCnt() {
		return positiveFeedbackCnt;
	}
	public void setPositiveFeedbackCnt(int positiveFeedbackCnt) {
		this.positiveFeedbackCnt = positiveFeedbackCnt;
	}
	public int getNegativeFeedbackCnt() {
		return negativeFeedbackCnt;
	}
	public void setNegativeFeedbackCnt(int negativeFeedbackCnt) {
		this.negativeFeedbackCnt = negativeFeedbackCnt;
	}
	public List<Estimate> getEstimates() {
		return estimates;
	}
	public void setEstimates(List<Estimate> estimates) {
		this.estimates = estimates;
	}
	public String getWriteTimeString() {
		return writeTimeString;
	}
	public void setWriteTimeString(String writeTimeString) {
		this.writeTimeString = writeTimeString;
	}
	public int getReqCodeSenderAkind() {
		return reqCodeSenderAkind;
	}
	public void setReqCodeSenderAkind(int reqCodeSenderAkind) {
		this.reqCodeSenderAkind = reqCodeSenderAkind;
	}
	public String getInviterFriendLevel() {
		return inviterFriendLevel;
	}
	public void setInviterFriendLevel(String inviterFriendLevel) {
		this.inviterFriendLevel = inviterFriendLevel;
	}
	public String getProvCity() {
		return provCity;
	}
	public void setProvCity(String provCity) {
		this.provCity = provCity;
	}
	public int getEnterId() {
		return enterId;
	}
	public void setEnterId(int enterId) {
		this.enterId = enterId;
	}
	public String getFriend1Name() {
		return friend1Name;
	}
	public void setFriend1Name(String friend1Name) {
		this.friend1Name = friend1Name;
	}
	public String getXyWatchName() {
		return xyWatchName;
	}
	public void setXyWatchName(String xyWatchName) {
		this.xyWatchName = xyWatchName;
	}
	public String getXyWatchedName() {
		return xyWatchedName;
	}
	public void setXyWatchedName(String xyWatchedName) {
		this.xyWatchedName = xyWatchedName;
	}
	public String getEnterNamePinyin() {
		return enterNamePinyin;
	}
	public void setEnterNamePinyin(String enterNamePinyin) {
		this.enterNamePinyin = enterNamePinyin;
	}
	public String getReqCodeSenderMobile() {
		return reqCodeSenderMobile;
	}
	public void setReqCodeSenderMobile(String reqCodeSenderMobile) {
		this.reqCodeSenderMobile = reqCodeSenderMobile;
	}
	public String getReqCodeSenderName() {
		return reqCodeSenderName;
	}
	public void setReqCodeSenderName(String reqCodeSenderName) {
		this.reqCodeSenderName = reqCodeSenderName;
	}
	public int getInviteCnt() {
		return inviteCnt;
	}
	public void setInviteCnt(int inviteCnt) {
		this.inviteCnt = inviteCnt;
	}

}