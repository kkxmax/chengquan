package com.beijing.chengxin.ui.bean;

public class CityInfo {

    public int cityId;
    public String cityTitle;
    public String cityAlias;

    public CityInfo() {
    }

    public CityInfo(CityInfo item) {
        this.cityId = item.cityId;
        this.cityTitle = item.cityTitle;
        this.cityAlias = item.cityTitle;
    }

}
