package com.beijing.chengxin.network.model;

import java.util.List;

/**
 * Created by start on 2017.11.07.
 */

public class CityModel {
    int id;
    String name;
    String cityAlias;
    int provinceId;
    String provinceCode;
    String provinceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityAlias() {
        return cityAlias;
    }

    public void setCityAlias(String cityAlias) {
        this.cityAlias = cityAlias;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
