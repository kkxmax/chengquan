package com.beijing.chengxin.network.model;

import java.util.ArrayList;

public class FavouriteModel {
    int hotCommentCnt;
    String hotContent;
    String hotSummary;
    int hotId;
    ArrayList<String> hotImgPaths = new ArrayList<String>();
    String hotTitle;
    int hotVisitCnt;
    TimeModel hotWriteTime;
    String hotWriteTimeString;
    int id;
    int kind;
    int owner;
    int productId;
    String productImgPath1;
    String productName;
    double productPrice;
    int serial;
    String writeTimeString;
    int productStatus;
    int hotStatus;

    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(int productStatus) {
        this.productStatus = productStatus;
    }

    public int getHotStatus() {
        return hotStatus;
    }

    public void setHotStatus(int hotStatus) {
        this.hotStatus = hotStatus;
    }

    public int getHotCommentCnt() {
        return hotCommentCnt;
    }

    public void setHotCommentCnt(int hotCommentCnt) {
        this.hotCommentCnt = hotCommentCnt;
    }

    public String getHotContent() {
        return hotContent;
    }

    public void setHotContent(String hotContent) {
        this.hotContent = hotContent;
    }

    public String getHotSummary() {
        return hotSummary;
    }

    public void setHotSummary(String hotSummary) {
        this.hotSummary = hotSummary;
    }

    public int getHotId() {
        return hotId;
    }

    public void setHotId(int hotId) {
        this.hotId = hotId;
    }

    public ArrayList<String> getHotImgPaths() {
        return hotImgPaths;
    }

    public void setHotImgPaths(ArrayList<String> hotImgPaths) {
        this.hotImgPaths = hotImgPaths;
    }

    public String getHotTitle() {
        return hotTitle;
    }

    public void setHotTitle(String hotTitle) {
        this.hotTitle = hotTitle;
    }

    public int getHotVisitCnt() {
        return hotVisitCnt;
    }

    public void setHotVisitCnt(int hotVisitCnt) {
        this.hotVisitCnt = hotVisitCnt;
    }

    public TimeModel getHotWriteTime() {
        return hotWriteTime;
    }

    public void setHotWriteTime(TimeModel hotWriteTime) {
        this.hotWriteTime = hotWriteTime;
    }

    public String getHotWriteTimeString() {
        return hotWriteTimeString;
    }

    public void setHotWriteTimeString(String hotWriteTimeString) {
        this.hotWriteTimeString = hotWriteTimeString;
    }

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

    public String getProductImgPath1() {
        return productImgPath1;
    }

    public void setProductImgPath1(String productImgPath1) {
        this.productImgPath1 = productImgPath1;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getWriteTimeString() {
        return writeTimeString;
    }

    public void setWriteTimeString(String writeTimeString) {
        this.writeTimeString = writeTimeString;
    }
}
