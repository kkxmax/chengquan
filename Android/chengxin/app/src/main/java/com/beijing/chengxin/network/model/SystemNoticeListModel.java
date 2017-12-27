package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class SystemNoticeListModel extends BaseModel {

    @SerializedName("data")
    List<SystemNoticeModel> list;

    public List<SystemNoticeModel> getList() {
        return list;
    }

    public void setList(List<SystemNoticeModel> list) {
        this.list = list;
    }
}
