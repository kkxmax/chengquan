package com.beijing.chengxin.ui.activity;

import android.os.Bundle;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.SystemNewsFragment;

public class SystemNewsActivity extends BaseFragmentActivity {

    public final String TAG = SystemNewsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showFragment(new SystemNewsFragment(), false, false);
    }
}
