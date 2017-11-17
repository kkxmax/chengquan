package com.beijing.chengxin.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.SelectSearchContentFragment;

public class SearchActivity extends BaseFragmentActivity {

    public final String TAG = SearchActivity.class.getName();
    public Fragment fragmentLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragmentLogin = new SelectSearchContentFragment();
        showFragment(fragmentLogin, false, false);
    }
}
