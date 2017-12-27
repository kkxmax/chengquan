package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class CarouselListModel extends BaseModel {
    @SerializedName("data")
    List<CarouselImageModel> imgList;

    public List<CarouselImageModel> getImgList() {
        return imgList;
    }

    public void setImgList(List<CarouselImageModel> imgList) {
        this.imgList = imgList;
    }
}
