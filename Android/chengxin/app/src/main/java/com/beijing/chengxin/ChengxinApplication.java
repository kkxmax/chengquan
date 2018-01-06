package com.beijing.chengxin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Environment;
import android.widget.Toast;

import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.NetworkEngine;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.ui.activity.LoginActivity;
import com.beijing.chengxin.ui.widget.CustomToast;
import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;

import io.fabric.sdk.android.Fabric;

public class ChengxinApplication extends Application {

    private static Context mContext;
    public static boolean mIsVisibleFlag;
    public static boolean mIsEmulator;
    public static boolean mIsInvalidExitFlag;
    public static ChengxinApplication instance;

    public static String LOGIN_MOBILE;
    public static String LOGIN_PASSWORD;

    @Override
    public void onCreate() {
        mContext = getApplicationContext();

        //keep session alive on server side.
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        mIsVisibleFlag = true;
        mIsInvalidExitFlag = false;
        NetworkEngine.mContentResolver = getContentResolver();

        instance = this;
        super.onCreate();

        Fabric.with(this, new Crashlytics());
    }
    @Override
    public void onTerminate() {
        ((AudioManager)getSystemService(Context.AUDIO_SERVICE)).setMode(AudioManager.MODE_NORMAL);
        Toast.makeText(mContext, "Application Terminate", Toast.LENGTH_LONG).show();
    }
    public static Context getContext() {
        return mContext;
    }

    public static int getScreenWidth() {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return mContext.getResources().getDisplayMetrics().heightPixels;
    }

    public static String getTempFilePath(String tmpName) {
        boolean state = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        String filePath = "";
        if (state) {
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            if(android.os.Build.DEVICE.contains("Samsung") || android.os.Build.MANUFACTURER.contains("Samsung")){
                filePath = filePath + "/external_sd";
            }
        } else {
            filePath = instance.getCacheDir().getAbsolutePath();
        }
        filePath = filePath + "/chengxin/";
        File file = new File(filePath);
        boolean isExist = file.exists();
        if (!isExist){
            isExist = file.mkdirs();
        }
        filePath += tmpName;
        return filePath;
    }

    public static void finishActivityFromDuplicate(Activity activity) {
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            activity.setResult(Constants.RESULT_CODE_LOGIN_DUPLICATE);
            activity.finish();
        }
    }

    public static void finishAndLoginActivityFromDuplicate(Activity activity) {
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            AppConfig.setLoginPwd("");
            SessionInstance.clearInstance();

            CustomToast.makeText(activity, R.string.msg_login_duplicate, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity, LoginActivity.class);
//            intent.putExtra(Constants.IS_AUTO_LOGIN, false);
            activity.startActivity(intent);

            activity.finish();
        }
    }

}
