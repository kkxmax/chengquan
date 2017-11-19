package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by start on 2017.11.07.
 */

public class ProvinceListModel extends BaseModel {
    @SerializedName("data")
    List<ProvinceModel> list;

    public List<ProvinceModel> getList() {
        return list;
    }

    public void setList(List<ProvinceModel> list) {
        this.list = list;
    }
}
