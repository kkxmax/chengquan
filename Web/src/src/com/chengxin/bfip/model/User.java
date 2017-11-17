package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;

public class User extends BaseModel {
	
    private int id;
    private String username;
    private String realname;
    private String password;
    private int roleId;
    private Date writeTime = new Date();
    
    private String title;
    private int funcPersonal;
    private int funcEnter;
    private int funcFenlei;
    private int funcXingye;
    private int funcProduct;
    private int funcItem;
    private int funcCarousel;
    private int funcVideo;
    private int funcContent;
    private int funcComment;
    private int funcStatistic;
    private int funcPwd;
    private int funcHot;
    private int funcError;
    private int funcChanpin;
    private int funcOpinion;
    private int funcSystem;
    
    
	public int getFuncProduct() {
		return funcProduct;
	}
	public void setFuncProduct(int funcProduct) {
		this.funcProduct = funcProduct;
	}
	public int getFuncItem() {
		return funcItem;
	}
	public void setFuncItem(int funcItem) {
		this.funcItem = funcItem;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getFuncPersonal() {
		return funcPersonal;
	}
	public void setFuncPersonal(int funcPersonal) {
		this.funcPersonal = funcPersonal;
	}
	public int getFuncEnter() {
		return funcEnter;
	}
	public void setFuncEnter(int funcEnter) {
		this.funcEnter = funcEnter;
	}
	public int getFuncFenlei() {
		return funcFenlei;
	}
	public void setFuncFenlei(int funcFenlei) {
		this.funcFenlei = funcFenlei;
	}
	public int getFuncXingye() {
		return funcXingye;
	}
	public void setFuncXingye(int funcXingye) {
		this.funcXingye = funcXingye;
	}
	public int getFuncCarousel() {
		return funcCarousel;
	}
	public void setFuncCarousel(int funcCarousel) {
		this.funcCarousel = funcCarousel;
	}
	public int getFuncVideo() {
		return funcVideo;
	}
	public void setFuncVideo(int funcVideo) {
		this.funcVideo = funcVideo;
	}
	public int getFuncContent() {
		return funcContent;
	}
	public void setFuncContent(int funcContent) {
		this.funcContent = funcContent;
	}
	public int getFuncComment() {
		return funcComment;
	}
	public void setFuncComment(int funcComment) {
		this.funcComment = funcComment;
	}
	public int getFuncStatistic() {
		return funcStatistic;
	}
	public void setFuncStatistic(int funcStatistic) {
		this.funcStatistic = funcStatistic;
	}
	public int getFuncPwd() {
		return funcPwd;
	}
	public void setFuncPwd(int funcPwd) {
		this.funcPwd = funcPwd;
	}
	public int getFuncHot() {
		return funcHot;
	}
	public void setFuncHot(int funcHot) {
		this.funcHot = funcHot;
	}
	public int getFuncError() {
		return funcError;
	}
	public void setFuncError(int funcError) {
		this.funcError = funcError;
	}
	public int getFuncChanpin() {
		return funcChanpin;
	}
	public void setFuncChanpin(int funcChanpin) {
		this.funcChanpin = funcChanpin;
	}
	public int getFuncOpinion() {
		return funcOpinion;
	}
	public void setFuncOpinion(int funcOpinion) {
		this.funcOpinion = funcOpinion;
	}
	public int getFuncSystem() {
		return funcSystem;
	}
	public void setFuncSystem(int funcSystem) {
		this.funcSystem = funcSystem;
	}
    
    
    
}