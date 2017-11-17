package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class EnterpriseModel extends UserModel {

    List<EvalModel> estimates;
    List<ComedityModel> products;
    List<ItemModel> items;
    List<ServeModel> services;

    public List<EvalModel> getEstimates() {
        return estimates;
    }

    public void setEstimates(List<EvalModel> estimates) {
        this.estimates = estimates;
    }

    @Override
    public List<ComedityModel> getProducts() {
        return products;
    }

    @Override
    public void setProducts(List<ComedityModel> products) {
        this.products = products;
    }

    @Override
    public List<ItemModel> getItems() {
        return items;
    }

    @Override
    public void setItems(List<ItemModel> items) {
        this.items = items;
    }

    @Override
    public List<ServeModel> getServices() {
        return services;
    }

    @Override
    public void setServices(List<ServeModel> services) {
        this.services = services;
    }
}
