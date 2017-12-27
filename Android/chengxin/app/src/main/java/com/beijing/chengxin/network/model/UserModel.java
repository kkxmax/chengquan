package com.beijing.chengxin.network.model;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class UserModel {

    int id;
    String mobile;
    String reqCodeId;
    String realname;
    String code;
    int viewCnt;
    String logo;
    int enterKind;
    int enterId;
    String enterName;
    String xyName;
    int xyleixingId;
    int cityId;
    String cityName;
    int provinceId;
    String provinceName;
    String addr;
    String job;
    String weburl;
    String weixin;
    String mainJob;
    String certNum;
    String certImage;
    String enterCertNum;
    String enterCertImage;
    String experience;
    String history;
    String comment;
    String recommend;
    String bossName;
    String bossJob;
    String bossMobile;
    String bossWeixin;
    int testStatus;
    int banStatus;
    int akind;
    String token;

    int reqCodeSenderAkind;
    int reqCodeSenderId;
    String reqCodeSenderEnterName;
    String reqCodeSenderRealname;
    String reqCodeSenderMobile;
    int credit;
    int electCnt;
    int feedbackCnt;
    int positiveFeedbackCnt;
    int negativeFeedbackCnt;
    int interested;
    String inviterFriendLevel;

    List<EvalModel>estimates;
    List<ComedityModel> products;
    List<ServeModel> services;
    List<ItemModel> items;

    public String alias;

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

    public String getReqCodeId() {
        return reqCodeId;
    }

    public void setReqCodeId(String reqCodeId) {
        this.reqCodeId = reqCodeId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
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

    public int getEnterId() {
        return enterId;
    }

    public void setEnterId(int enterId) {
        this.enterId = enterId;
    }

    public String getEnterName() {
        return enterName;
    }

    public void setEnterName(String enterName) {
        this.enterName = enterName;
    }

    public String getXyName() {
        return xyName;
    }

    public void setXyName(String xyName) {
        this.xyName = xyName;
    }

    public int getXyleixingId() {
        return xyleixingId;
    }

    public void setXyleixingId(int xyleixingId) {
        this.xyleixingId = xyleixingId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public String getMainJob() {
        return mainJob;
    }

    public void setMainJob(String mainJob) {
        this.mainJob = mainJob;
    }

    public String getCertNum() {
        return certNum;
    }

    public void setCertNum(String certNum) {
        this.certNum = certNum;
    }

    public String getCertImage() {
        return certImage;
    }

    public void setCertImage(String certImage) {
        this.certImage = certImage;
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

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
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

    public int getAkind() {
        return akind;
    }

    public void setAkind(int akind) {
        this.akind = akind;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<EvalModel> getEstimates() {
        return estimates;
    }

    public void setEstimates(List<EvalModel> estimates) {
        this.estimates = estimates;
    }

    public List<ComedityModel> getProducts() {
        return products;
    }

    public void setProducts(List<ComedityModel> products) {
        this.products = products;
    }

    public List<ServeModel> getServices() {
        return services;
    }

    public void setServices(List<ServeModel> services) {
        this.services = services;
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }

    public int getReqCodeSenderAkind() {
        return reqCodeSenderAkind;
    }

    public void setReqCodeSenderAkind(int reqCodeSenderAkind) {
        this.reqCodeSenderAkind = reqCodeSenderAkind;
    }

    public int getReqCodeSenderId() {
        return reqCodeSenderId;
    }

    public void setReqCodeSenderId(int reqCodeSenderId) {
        this.reqCodeSenderId = reqCodeSenderId;
    }

    public String getReqCodeSenderEnterName() {
        return reqCodeSenderEnterName;
    }

    public void setReqCodeSenderEnterName(String reqCodeSenderEnterName) {
        this.reqCodeSenderEnterName = reqCodeSenderEnterName;
    }

    public String getReqCodeSenderRealname() {
        return reqCodeSenderRealname;
    }

    public void setReqCodeSenderRealname(String reqCodeSenderRealname) {
        this.reqCodeSenderRealname = reqCodeSenderRealname;
    }

    public String getReqCodeSenderMobile() {
        return reqCodeSenderMobile;
    }

    public void setReqCodeSenderMobile(String reqCodeSenderMobile) {
        this.reqCodeSenderMobile = reqCodeSenderMobile;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getElectCnt() {
        return electCnt;
    }

    public void setElectCnt(int electCnt) {
        this.electCnt = electCnt;
    }

    public int getFeedbackCnt() {
        return feedbackCnt;
    }

    public void setFeedbackCnt(int feedbackCnt) {
        this.feedbackCnt = feedbackCnt;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getInterested() {
        return interested;
    }

    public void setInterested(int interested) {
        this.interested = interested;
    }

    public String getInviterFriendLevel() {
        return inviterFriendLevel;
    }

    public void setInviterFriendLevel(String inviterFriendLevel) {
        this.inviterFriendLevel = inviterFriendLevel;
    }
}
