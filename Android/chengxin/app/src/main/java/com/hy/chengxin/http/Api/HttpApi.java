package com.hy.chengxin.http.Api;

import com.beijing.chengxin.config.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HoKw on 2017-12-20.
 */

public class HttpApi {
    private static ApiService mApiService;

    public static void initialize() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiService = retrofit.create(ApiService.class);
    }

    public static ApiService getApiService() {
        return mApiService;
    }

    public static void onShare(int kind, int id) {
        OnShareAPI.getInstance().onShare(kind, id);
    }
}
