package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MarkLogListModel extends BaseModel {
    @SerializedName("data")
    ArrayList<MarkLogModel> list;

    public ArrayList<MarkLogModel> getList() {
        return list;
    }

    public void setList(ArrayList<MarkLogModel> list) {
        this.list = list;
    }
}
