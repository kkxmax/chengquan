package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by start on 2017.11.10.
 */

public class FenleiListModel extends BaseModel {
    @SerializedName("data")
    List<FenleiModel> list;

    public List<FenleiModel> getList() {
        return list;
    }

    public void setList(List<FenleiModel> list) {
        this.list = list;
    }
}
