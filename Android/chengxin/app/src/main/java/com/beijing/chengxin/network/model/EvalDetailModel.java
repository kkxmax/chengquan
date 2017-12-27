package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by star on 11/2/2017.
 */

public class EvalDetailModel extends BaseModel {
    @SerializedName("estimateInfo")
    EvalModel eval;

    public EvalModel getEval() {
        return eval;
    }

    public void setEval(EvalModel eval) {
        this.eval = eval;
    }
}
