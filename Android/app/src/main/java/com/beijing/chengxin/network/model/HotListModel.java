package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class HotListModel extends BaseModel {
    @SerializedName("data")
    List<HotModel> list;

    public List<HotModel> getList() {
        return list;
    }

    public void setList(List<HotModel> list) {
        this.list = list;
    }
}
