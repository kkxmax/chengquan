package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class MyEvalNoticeModel extends BaseModel {

    List<EvalModel> nEstimateList;
    int nMoreCnt;
    List<EvalModel> pEstimateList;
    int pMoreCnt;

    public List<EvalModel> getnEstimateList() {
        return nEstimateList;
    }

    public void setnEstimateList(List<EvalModel> nEstimateList) {
        this.nEstimateList = nEstimateList;
    }

    public int getnMoreCnt() {
        return nMoreCnt;
    }

    public void setnMoreCnt(int nMoreCnt) {
        this.nMoreCnt = nMoreCnt;
    }

    public List<EvalModel> getpEstimateList() {
        return pEstimateList;
    }

    public void setpEstimateList(List<EvalModel> pEstimateList) {
        this.pEstimateList = pEstimateList;
    }

    public int getpMoreCnt() {
        return pMoreCnt;
    }

    public void setpMoreCnt(int pMoreCnt) {
        this.pMoreCnt = pMoreCnt;
    }
}
