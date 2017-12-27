package com.beijing.chengxin.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
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

    // add dd -- 2017.12.07
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULT_CODE_LOGIN_DUPLICATE) {
            ChengxinApplication.finishActivityFromDuplicate(this);
            return;
        }
        if (RealnameCertFragment.instance != null)
            RealnameCertFragment.instance.onActivityResult(requestCode, resultCode, data);
    }
}
