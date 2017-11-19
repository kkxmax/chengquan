package com.beijing.chengxin;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.beijing.chengxin.network.NetworkEngine;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;

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
        //String filePath = mContext.getFilesDir().getAbsolutePath() + "/cache/";
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/chengxin/";
        File file = new File(filePath);
        if (!file.exists()){
            file.mkdirs();
        }
        filePath += tmpName;
        return filePath;
    }

}
