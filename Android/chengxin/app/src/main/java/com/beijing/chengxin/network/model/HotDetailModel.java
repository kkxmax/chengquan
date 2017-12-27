package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class HotDetailModel extends BaseModel {

    HotModel hot;

    public HotModel getHot() {
        return hot;
    }

    public void setHot(HotModel hot) {
        this.hot = hot;
    }
}
