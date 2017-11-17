package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;

public class Products extends BaseModel{
	
	private int id;
	private String code;
	private int isMain;
	private String name;
	private Double price;
	private int pleixingId;
	private String comment;
	private String weburl;
	private String saleAddr;
	private int status;
	private Date upTime;
	private Date downTime;
	private String imgPath1;
	private String imgPath2;
	private String imgPath3;
	private String imgPath4;
	private String imgPath5;
	private int accountId;
	private Date writeTime;

	private String pleixingName;
	private String statusName;
	private int enterKind;
	private String enterKindName;
	private String enterName;
	private String accountMobile;	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEnterName() {
		return enterName;
	}
	public void setEnterName(String enterName) {
		this.enterName = enterName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public int getPleixingId() {
		return pleixingId;
	}
	public void setPleixingId(int pleixingId) {
		this.pleixingId = pleixingId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getWeburl() {
		return weburl;
	}
	public void setWeburl(String weburl) {
		this.weburl = weburl;
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
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getIsMain() {
		return isMain;
	}
	public void setIsMain(int isMain) {
		this.isMain = isMain;
	}
	public String getSaleAddr() {
		return saleAddr;
	}
	public void setSaleAddr(String saleAddr) {
		this.saleAddr = saleAddr;
	}
	public String getPleixingName() {
		return pleixingName;
	}
	public void setPleixingName(String pleixingName) {
		this.pleixingName = pleixingName;
	}
	public int getEnterKind() {
		return enterKind;
	}
	public void setEnterKind(int enterKind) {
		this.enterKind = enterKind;
	}
	public String getEnterKindName() {
		return enterKindName;
	}
	public void setEnterKindName(String enterKindName) {
		this.enterKindName = enterKindName;
	}
	public String getAccountMobile() {
		return accountMobile;
	}
	public void setAccountMobile(String accountMobile) {
		this.accountMobile = accountMobile;
	}
	

}
