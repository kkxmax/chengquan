package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class ServeListModel extends BaseModel {

    @SerializedName("data")
    List<ServeModel> list;

    public List<ServeModel> getList() {
        return list;
    }

    public void setList(List<ServeModel> list) {
        this.list = list;
    }
}
