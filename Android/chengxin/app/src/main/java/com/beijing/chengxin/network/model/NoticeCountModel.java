package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class NoticeCountModel extends BaseModel {
    int totalCnt;
    int myEstimateCnt;
    int estimateToMeCnt;

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getMyEstimateCnt() {
        return myEstimateCnt;
    }

    public void setMyEstimateCnt(int myEstimateCnt) {
        this.myEstimateCnt = myEstimateCnt;
    }

    public int getEstimateToMeCnt() {
        return estimateToMeCnt;
    }

    public void setEstimateToMeCnt(int estimateToMeCnt) {
        this.estimateToMeCnt = estimateToMeCnt;
    }
}
