package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by start on 2017.11.07.
 */

public class XyleixingListModel extends BaseModel {
    @SerializedName("data")
    List<XyleixingModel> list;

    public List<XyleixingModel> getList() {
        return list;
    }

    public void setList(List<XyleixingModel> list) {
        this.list = list;
    }
}
