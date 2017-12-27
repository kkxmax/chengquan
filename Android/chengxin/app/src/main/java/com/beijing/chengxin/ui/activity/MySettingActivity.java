package com.beijing.chengxin.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
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

    public void logout() {
        AppConfig.setLoginMobile("");
        AppConfig.setLoginPwd("");

        finish();
        MainActivity.mainActivity.finish();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.IS_AUTO_LOGIN, false);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
