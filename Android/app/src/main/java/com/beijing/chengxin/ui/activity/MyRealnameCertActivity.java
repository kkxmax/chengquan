package com.beijing.chengxin.ui.activity;

import android.os.Bundle;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.RealnameCertFragment;

public class MyRealnameCertActivity extends BaseFragmentActivity {

    public final String TAG = MyRealnameCertActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showFragment(new RealnameCertFragment(), false, false);
    }
}
