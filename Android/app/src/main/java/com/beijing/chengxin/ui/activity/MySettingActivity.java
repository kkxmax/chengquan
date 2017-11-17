package com.beijing.chengxin.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.SettingFragment;

public class MySettingActivity extends BaseFragmentActivity {

    public final String TAG = MySettingActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showFragment(new SettingFragment(), false, false);
    }
}
