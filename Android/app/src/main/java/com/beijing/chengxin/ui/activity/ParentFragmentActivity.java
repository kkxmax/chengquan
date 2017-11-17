package com.beijing.chengxin.ui.activity;

import android.support.v4.app.FragmentActivity;

import com.beijing.chengxin.R;

/**
 * Created by star on 10/25/2017.
 */

public class ParentFragmentActivity extends FragmentActivity {
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

