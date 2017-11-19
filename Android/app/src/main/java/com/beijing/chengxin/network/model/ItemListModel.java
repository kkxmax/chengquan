package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class ItemListModel extends BaseModel {

    @SerializedName("data")
    List<ItemModel> list;

    public List<ItemModel> getList() {
        return list;
    }

    public void setList(List<ItemModel> list) {
        this.list = list;
    }
}
