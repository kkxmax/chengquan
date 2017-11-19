package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavouriteListModel extends BaseModel {
    @SerializedName("data")
    List<FavouriteModel> list;

    public List<FavouriteModel> getList() {
        return list;
    }

    public void setList(List<FavouriteModel> list) {
        this.list = list;
    }
}
