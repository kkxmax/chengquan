package com.beijing.chengxin.ui.activity;

import android.os.Bundle;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.MyWriteFragment;

public class MyWriteActivity extends BaseFragmentActivity {

    public final String TAG = MyWriteActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showFragment(new MyWriteFragment(), false, false);
    }
}
