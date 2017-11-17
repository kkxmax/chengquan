package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by star on 11/2/2017.
 */

public class VerifyCodeModel extends BaseModel {
    String verifyCode;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
