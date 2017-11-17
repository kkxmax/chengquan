package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by star on 11/2/2017.
 */

public class InviterModel extends BaseModel {

    UserModel inviterInfo;

    public UserModel getInviterInfo() {
        return inviterInfo;
    }

    public void setInviterInfo(UserModel inviterInfo) {
        this.inviterInfo = inviterInfo;
    }
}
