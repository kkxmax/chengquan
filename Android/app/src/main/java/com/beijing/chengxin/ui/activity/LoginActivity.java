package com.beijing.chengxin.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.LoginFragment;

public class LoginActivity extends BaseFragmentActivity {

    public final String TAG = LoginActivity.class.getName();
    public Fragment fragmentLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragmentLogin = new LoginFragment();
        showFragment(fragmentLogin, false, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
