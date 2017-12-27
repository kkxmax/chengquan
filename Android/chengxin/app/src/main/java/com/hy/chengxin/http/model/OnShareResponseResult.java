package com.hy.chengxin.http.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HoKw on 2017-12-20.
 */

public class OnShareResponseResult {

    @SerializedName("retCode")
    @Expose
    private int retCode;

    @SerializedName("msg")
    @Expose
    private String msg;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
