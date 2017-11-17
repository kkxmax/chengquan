package com.beijing.chengxin.ui.activity;

import android.os.Bundle;
import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.ChengXinLogFragment;

public class ChengxinLogActivity extends BaseFragmentActivity {

    public final String TAG = ChengxinLogActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showFragment(new ChengXinLogFragment(), false, false);
    }
}
