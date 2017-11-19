package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class ComedityListModel extends BaseModel {

    @SerializedName("data")
    List<ComedityModel> list;

    public List<ComedityModel> getList() {
        return list;
    }

    public void setList(List<ComedityModel> list) {
        this.list = list;
    }
}
