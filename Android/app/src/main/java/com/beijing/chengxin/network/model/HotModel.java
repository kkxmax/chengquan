package com.beijing.chengxin.network.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class HotModel  {
    int id;
    String title;
    int xyleixingId;
    String xyleixingName;
    int xyleixingLevel1Id;
    String xyleixingLevel1Name;
    int visitCnt;
    int shareCnt;
    int commentCnt;
    int electCnt;
    int isFavourite;
    int isElectedByMe;
    String up_time;
    String down_time;
    String content;
    int status;
    String statusName;
    List<String> imgPaths;
    String writeTimeString;

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

    public int getXyleixingId() {
        return xyleixingId;
    }

    public void setXyleixingId(int xyleixingId) {
        this.xyleixingId = xyleixingId;
    }

    public String getXyleixingName() {
        return xyleixingName;
    }

    public void setXyleixingName(String xyleixingName) {
        this.xyleixingName = xyleixingName;
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

    public int getVisitCnt() {
        return visitCnt;
    }

    public void setVisitCnt(int visitCnt) {
        this.visitCnt = visitCnt;
    }

    public int getShareCnt() {
        return shareCnt;
    }

    public void setShareCnt(int shareCnt) {
        this.shareCnt = shareCnt;
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

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public int getIsElectedByMe() {
        return isElectedByMe;
    }

    public void setIsElectedByMe(int isElectedByMe) {
        this.isElectedByMe = isElectedByMe;
    }

    public String getUp_time() {
        return up_time;
    }

    public void setUp_time(String up_time) {
        this.up_time = up_time;
    }

    public String getDown_time() {
        return down_time;
    }

    public void setDown_time(String down_time) {
        this.down_time = down_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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
}
