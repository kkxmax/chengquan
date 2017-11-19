package com.beijing.chengxin.network.model;

import java.util.List;

/**
 * Created by start on 2017.11.07.
 */

public class ProvinceModel {
    int id;
    String name;
    String code;
    List <CityModel> cities;

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

    public List<CityModel> getCities() {
        return cities;
    }

    public void setCities(List<CityModel> cities) {
        this.cities = cities;
    }
}
