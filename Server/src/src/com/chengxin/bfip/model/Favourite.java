package com.chengxin.bfip.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chengxin.common.BaseModel;
import com.chengxin.common.DateTimeUtil;

public class Favourite extends BaseModel{
	
	private int id;
    private int kind;
    private int owner;
    private int productId;
    private int hotId;
    private Date writeTime = new Date();
    
    private String writeTimeString;
    private String productName;
    private String productImgPath1;
    private Double productPrice;
    private String hotTitle;
    private String hotContent;
    private int hotVisitCnt;
    private int hotCommentCnt;
    private Date hotWriteTime;
    private String hotWriteTimeString;
    private String hotImgPath1;
    private String hotImgPath2;
    private String hotImgPath3;
    private String hotImgPath4;
    private String hotImgPath5;
    private String hotImgPath6;
    private String hotImgPath7;
    private String hotImgPath8;
    private String hotImgPath9;
    private String hotImgPath10;
    private String hotImgPath11;
    private String hotImgPath12;
    private String hotImgPath13;
    private String hotImgPath14;
    private String hotImgPath15;
    List<String> hotImgPaths = new ArrayList<String>();
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getHotId() {
		return hotId;
	}
	public void setHotId(int hotId) {
		this.hotId = hotId;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductImgPath1() {
		return productImgPath1;
	}
	public void setProductImgPath1(String productImgPath1) {
		this.productImgPath1 = productImgPath1;
	}
	public Double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}
	public String getHotTitle() {
		return hotTitle;
	}
	public void setHotTitle(String hotTitle) {
		this.hotTitle = hotTitle;
	}
	public String getHotContent() {
		return hotContent;
	}
	public void setHotContent(String hotContent) {
		this.hotContent = hotContent;
	}
	public int getHotVisitCnt() {
		return hotVisitCnt;
	}
	public void setHotVisitCnt(int hotVisitCnt) {
		this.hotVisitCnt = hotVisitCnt;
	}
	public int getHotCommentCnt() {
		return hotCommentCnt;
	}
	public void setHotCommentCnt(int hotCommentCnt) {
		this.hotCommentCnt = hotCommentCnt;
	}
	public Date getHotWriteTime() {
		return hotWriteTime;
	}
	public void setHotWriteTime(Date hotWriteTime) {
		this.hotWriteTime = hotWriteTime;
	}
	public String getHotImgPath1() {
		return hotImgPath1;
	}
	public void setHotImgPath1(String hotImgPath1) {
		this.hotImgPath1 = hotImgPath1;
	}
	public String getHotImgPath2() {
		return hotImgPath2;
	}
	public void setHotImgPath2(String hotImgPath2) {
		this.hotImgPath2 = hotImgPath2;
	}
	public String getHotImgPath3() {
		return hotImgPath3;
	}
	public void setHotImgPath3(String hotImgPath3) {
		this.hotImgPath3 = hotImgPath3;
	}
	public String getHotImgPath4() {
		return hotImgPath4;
	}
	public void setHotImgPath4(String hotImgPath4) {
		this.hotImgPath4 = hotImgPath4;
	}
	public String getHotImgPath5() {
		return hotImgPath5;
	}
	public void setHotImgPath5(String hotImgPath5) {
		this.hotImgPath5 = hotImgPath5;
	}
	public String getHotImgPath6() {
		return hotImgPath6;
	}
	public void setHotImgPath6(String hotImgPath6) {
		this.hotImgPath6 = hotImgPath6;
	}
	public String getHotImgPath7() {
		return hotImgPath7;
	}
	public void setHotImgPath7(String hotImgPath7) {
		this.hotImgPath7 = hotImgPath7;
	}
	public String getHotImgPath8() {
		return hotImgPath8;
	}
	public void setHotImgPath8(String hotImgPath8) {
		this.hotImgPath8 = hotImgPath8;
	}
	public String getHotImgPath9() {
		return hotImgPath9;
	}
	public void setHotImgPath9(String hotImgPath9) {
		this.hotImgPath9 = hotImgPath9;
	}
	public String getHotImgPath10() {
		return hotImgPath10;
	}
	public void setHotImgPath10(String hotImgPath10) {
		this.hotImgPath10 = hotImgPath10;
	}
	public String getHotImgPath11() {
		return hotImgPath11;
	}
	public void setHotImgPath11(String hotImgPath11) {
		this.hotImgPath11 = hotImgPath11;
	}
	public String getHotImgPath12() {
		return hotImgPath12;
	}
	public void setHotImgPath12(String hotImgPath12) {
		this.hotImgPath12 = hotImgPath12;
	}
	public String getHotImgPath13() {
		return hotImgPath13;
	}
	public void setHotImgPath13(String hotImgPath13) {
		this.hotImgPath13 = hotImgPath13;
	}
	public String getHotImgPath14() {
		return hotImgPath14;
	}
	public void setHotImgPath14(String hotImgPath14) {
		this.hotImgPath14 = hotImgPath14;
	}
	public String getHotImgPath15() {
		return hotImgPath15;
	}
	public void setHotImgPath15(String hotImgPath15) {
		this.hotImgPath15 = hotImgPath15;
	}
	public String getWriteTimeString() {
		return writeTimeString;
	}
	public void setWriteTimeString(String writeTimeString) {
		this.writeTimeString = writeTimeString;
	}
	public String getHotWriteTimeString() {
		return hotWriteTimeString;
	}
	public void setHotWriteTimeString(String hotWriteTimeString) {
		this.hotWriteTimeString = hotWriteTimeString;
	}
	public List<String> getHotImgPaths() {
		return hotImgPaths;
	}
	public void setHotImgPaths(List<String> hotImgPaths) {
		this.hotImgPaths = hotImgPaths;
	}
    
}

