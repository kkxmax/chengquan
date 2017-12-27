package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by start on 2017.11.07.
 */

public class XyleixingModel {
    int id;
    String title;
    int upperId;
    int isMyWatch;
    int isMyWatched;
    @SerializedName("children")
    List<XyleixingModel> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUpperId() {
        return upperId;
    }

    public void setUpperId(int upperId) {
        this.upperId = upperId;
    }

    public int getIsMyWatch() {
        return isMyWatch;
    }

    public void setIsMyWatch(int isMyWatch) {
        this.isMyWatch = isMyWatch;
    }

    public int getIsMyWatched() {
        return isMyWatched;
    }

    public void setIsMyWatched(int isMyWatched) {
        this.isMyWatched = isMyWatched;
    }

    public List<XyleixingModel> getList() {
        return list;
    }

    public void setList(List<XyleixingModel> list) {
        this.list = list;
    }
}
