package com.beijing.chengxin.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;

/**
 * Created by star on 10/25/2017.
 */

public class ParentFragmentActivity extends FragmentActivity {

    public AppConfig appConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appConfig = new AppConfig(this);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

