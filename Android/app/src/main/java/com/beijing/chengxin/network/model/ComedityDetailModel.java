package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class ComedityDetailModel extends BaseModel {

    ComedityModel product;

    public ComedityModel getProduct() {
        return product;
    }

    public void setProduct(ComedityModel product) {
        this.product = product;
    }
}
