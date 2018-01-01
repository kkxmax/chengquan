package com.beijing.chengxin.network.model;

import java.io.Serializable;

/**
 * Created by star on 11/2/2017.
 */

public class BaseModel implements Serializable {
    int retCode;
    String msg;

    public BaseModel() {
        retCode = 0;
        msg = "";
    }

    public boolean isValid() {
        if (retCode == 0)
            return  false;
        return true;
    }

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
