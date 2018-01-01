package com.hy.chengxin.http.Api;

import com.beijing.chengxin.network.SessionInstance;
import com.hy.chengxin.http.model.OnShareResponseResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HoKw on 2017-12-20.
 */

public class OnShareAPI {
    private OnShareResponseListener mListener;
    private static OnShareAPI mInstance;

    public void setResponseListener(OnShareResponseListener listener) {
        mListener = listener;
    }

    public void onShare(int kind, int id) {
        String token = SessionInstance.getInstance().getLoginData().getToken();
        Call<OnShareResponseResult> call = HttpApi.getApiService().onShare("onShare", kind, id, token);
        call.enqueue(new Callback<OnShareResponseResult>() {
            @Override
            public void onResponse(Call<OnShareResponseResult> call, Response<OnShareResponseResult> response) {
                int statusCode = response.code();
                OnShareResponseResult result = response.body();
                if (mListener != null) {
                    if (statusCode == 200 && result != null && result.getRetCode() == 200)
                        mListener.onShareSuccess(result);
                    else
                        mListener.onShareFailure(result);
                }
            }

            @Override
            public void onFailure(Call<OnShareResponseResult> call, Throwable t) {
                if (mListener != null)
                    mListener.onShareFailure(null);
            }
        });
    }

    public interface OnShareResponseListener {
        public void onShareSuccess(OnShareResponseResult result);
        public void onShareFailure(OnShareResponseResult result);
    }

    public static OnShareAPI getInstance() {
        if (mInstance == null)
            mInstance = new OnShareAPI();
        return mInstance;
    }
}
