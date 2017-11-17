package com.beijing.chengxin.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by star on 11/2/2017.
 */

public class LoginModel extends BaseModel {
    String token;
    @SerializedName("userInfo")
    UserModel user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
