package com.hy.chengxin.http.Api;

import com.hy.chengxin.http.model.OnShareResponseResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by HoKw on 2017-12-20.
 */

public interface ApiService  {
    @POST("api.html")
    @FormUrlEncoded
    Call<OnShareResponseResult> onShare(
            @Field("pAct") String pAct,
            @Field("kind") int kind,
            @Field("id") int id,
            @Field("share") int share
    );
}
