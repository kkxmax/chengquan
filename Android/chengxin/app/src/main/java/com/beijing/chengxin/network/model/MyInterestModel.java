package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyInterestModel extends BaseModel {
    @SerializedName("accountsRecommended")
    ArrayList<UserModel> list;

    int enterCnt;
    int friend1Cnt;
    int friend2Cnt;
    int friend3Cnt;
    int myAncestorCnt;
    int personalCnt;

    public ArrayList<UserModel> getList() {
        return list;
    }

    public void setList(ArrayList<UserModel> list) {
        this.list = list;
    }

    public int getEnterCnt() {
        return enterCnt;
    }

    public void setEnterCnt(int enterCnt) {
        this.enterCnt = enterCnt;
    }

    public int getFriend1Cnt() {
        return friend1Cnt;
    }

    public void setFriend1Cnt(int friend1Cnt) {
        this.friend1Cnt = friend1Cnt;
    }

    public int getFriend2Cnt() {
        return friend2Cnt;
    }

    public void setFriend2Cnt(int friend2Cnt) {
        this.friend2Cnt = friend2Cnt;
    }

    public int getFriend3Cnt() {
        return friend3Cnt;
    }

    public void setFriend3Cnt(int friend3Cnt) {
        this.friend3Cnt = friend3Cnt;
    }

    public int getMyAncestorCnt() {
        return myAncestorCnt;
    }

    public void setMyAncestorCnt(int myAncestorCnt) {
        this.myAncestorCnt = myAncestorCnt;
    }

    public int getPersonalCnt() {
        return personalCnt;
    }

    public void setPersonalCnt(int personalCnt) {
        this.personalCnt = personalCnt;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }
}
