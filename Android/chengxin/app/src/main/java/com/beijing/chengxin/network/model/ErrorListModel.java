package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ErrorListModel extends BaseModel {
    @SerializedName("data")
    ArrayList<ErrorModel> list;

    public ArrayList<ErrorModel> getList() {
        return list;
    }

    public void setList(ArrayList<ErrorModel> list) {
        this.list = list;
    }
}
