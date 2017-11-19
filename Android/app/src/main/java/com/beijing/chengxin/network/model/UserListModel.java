package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class UserListModel extends BaseModel {

    @SerializedName("data")
    List<UserModel> list;

    public List<UserModel> getList() {
        return list;
    }

    public void setList(List<UserModel> list) {
        this.list = list;
    }
}
