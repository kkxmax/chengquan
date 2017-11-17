package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 11/2/2017.
 */

public class EvalListModel extends BaseModel {
    @SerializedName("data")
    ArrayList<EvalModel> list;

    public ArrayList<EvalModel> getList() {
        return list;
    }

    public void setList(ArrayList<EvalModel> list) {
        this.list = list;
    }
}
