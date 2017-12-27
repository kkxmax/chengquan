package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;

public class Carousel extends BaseModel {
	
	private int id;
	private String imgName;
	private double imgSize;
	private String imgUrl;
	private int kind;
	private int ord;
	private int status;
	private int videoId;
	private Date writeTime = new Date();

	private String writeTimeString;
	private String videoName;
	private double videoSize;
	private String videoUrl;
	private String videoTitle;
	private String videoComment;

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public String getVideoComment() {
		return videoComment;
	}

	public void setVideoComment(String videoComment) {
		this.videoComment = videoComment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public double getImgSize() {
		return imgSize;
	}

	public void setImgSize(double imgSize) {
		this.imgSize = imgSize;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public double getVideoSize() {
		return videoSize;
	}

	public void setVideoSize(double videoSize) {
		this.videoSize = videoSize;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
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

}