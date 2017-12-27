package com.chengxin.bfip.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chengxin.common.BaseModel;

public class Errors extends BaseModel{
	
	private int id;
	private int estimateId;
	private int ownerId;
	private int kind;
	private String reason;
	private int status;
	private String whyis;
	private String imgPath1;
	private String imgPath2;
	private String imgPath3;
	private String imgPath4;
	private String imgPath5;
	private String imgPath6;
	private Date writeTime = new Date();

	private String writeTimeString;
    List<String> imgPaths = new ArrayList<String>();
	private String statusName;
	private String kindName;
	private int ownerAkind;
	private String ownerMobile;
	private String ownerRealname;
	private String ownerEnterName;
	private String ownerName;
	private int estimateeId;
	private int estimateeAkind;
	private String estimateeRealname;
	private String estimateeEnterName;
	private String estimateeName;
	private int estimaterId;
	private String estimaterLogo;
	private int estimaterAkind;
	private String estimaterRealname;
	private String estimaterEnterName;
	private String estimaterName;
	private String estimateContent;
	private String estimateImgPath1;
	private String estimateImgPath2;
	private String estimateImgPath3;
	private String estimateImgPath4;
	private String estimateImgPath5;
	List<String> estimateImgPaths = new ArrayList<String>();
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEstimateId() {
		return estimateId;
	}
	public void setEstimateId(int estimateId) {
		this.estimateId = estimateId;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getWhyis() {
		return whyis;
	}
	public void setWhyis(String whyis) {
		this.whyis = whyis;
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
	public String getKindName() {
		return kindName;
	}
	public void setKindName(String kindName) {
		this.kindName = kindName;
	}
	public int getOwnerAkind() {
		return ownerAkind;
	}
	public void setOwnerAkind(int ownerAkind) {
		this.ownerAkind = ownerAkind;
	}
	public String getOwnerMobile() {
		return ownerMobile;
	}
	public void setOwnerMobile(String ownerMobile) {
		this.ownerMobile = ownerMobile;
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
	public int getEstimateeId() {
		return estimateeId;
	}
	public void setEstimateeId(int estimateeId) {
		this.estimateeId = estimateeId;
	}
	public int getEstimateeAkind() {
		return estimateeAkind;
	}
	public void setEstimateeAkind(int estimateeAkind) {
		this.estimateeAkind = estimateeAkind;
	}
	public String getEstimateeRealname() {
		return estimateeRealname;
	}
	public void setEstimateeRealname(String estimateeRealname) {
		this.estimateeRealname = estimateeRealname;
	}
	public String getEstimateeEnterName() {
		return estimateeEnterName;
	}
	public void setEstimateeEnterName(String estimateeEnterName) {
		this.estimateeEnterName = estimateeEnterName;
	}
	public int getEstimaterId() {
		return estimaterId;
	}
	public void setEstimaterId(int estimaterId) {
		this.estimaterId = estimaterId;
	}
	public int getEstimaterAkind() {
		return estimaterAkind;
	}
	public void setEstimaterAkind(int estimaterAkind) {
		this.estimaterAkind = estimaterAkind;
	}
	public String getEstimaterRealname() {
		return estimaterRealname;
	}
	public void setEstimaterRealname(String estimaterRealname) {
		this.estimaterRealname = estimaterRealname;
	}
	public String getEstimaterEnterName() {
		return estimaterEnterName;
	}
	public void setEstimaterEnterName(String estimaterEnterName) {
		this.estimaterEnterName = estimaterEnterName;
	}
	public String getEstimateContent() {
		return estimateContent;
	}
	public void setEstimateContent(String estimateContent) {
		this.estimateContent = estimateContent;
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
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getEstimateeName() {
		return estimateeName;
	}
	public void setEstimateeName(String estimateeName) {
		this.estimateeName = estimateeName;
	}
	public String getEstimaterName() {
		return estimaterName;
	}
	public void setEstimaterName(String estimaterName) {
		this.estimaterName = estimaterName;
	}
	public String getEstimaterLogo() {
		return estimaterLogo;
	}
	public void setEstimaterLogo(String estimaterLogo) {
		this.estimaterLogo = estimaterLogo;
	}
	public String getEstimateImgPath1() {
		return estimateImgPath1;
	}
	public void setEstimateImgPath1(String estimateImgPath1) {
		this.estimateImgPath1 = estimateImgPath1;
	}
	public String getEstimateImgPath2() {
		return estimateImgPath2;
	}
	public void setEstimateImgPath2(String estimateImgPath2) {
		this.estimateImgPath2 = estimateImgPath2;
	}
	public String getEstimateImgPath3() {
		return estimateImgPath3;
	}
	public void setEstimateImgPath3(String estimateImgPath3) {
		this.estimateImgPath3 = estimateImgPath3;
	}
	public String getEstimateImgPath4() {
		return estimateImgPath4;
	}
	public void setEstimateImgPath4(String estimateImgPath4) {
		this.estimateImgPath4 = estimateImgPath4;
	}
	public String getEstimateImgPath5() {
		return estimateImgPath5;
	}
	public void setEstimateImgPath5(String estimateImgPath5) {
		this.estimateImgPath5 = estimateImgPath5;
	}
	public List<String> getEstimateImgPaths() {
		return estimateImgPaths;
	}
	public void setEstimateImgPaths(List<String> estimateImgPaths) {
		this.estimateImgPaths = estimateImgPaths;
	}

}
