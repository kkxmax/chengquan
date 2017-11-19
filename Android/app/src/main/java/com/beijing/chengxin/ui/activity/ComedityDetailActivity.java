package com.beijing.chengxin.ui.activity;

import android.os.Bundle;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.ComedityDetailFragment;

public class ComedityDetailActivity extends BaseFragmentActivity {
    public final String TAG = ComedityDetailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int productId = getIntent().getIntExtra("productId", 0);

        ComedityDetailFragment fragment = new ComedityDetailFragment();
        fragment.setId(productId);

        showFragment(fragment, false, false);
    }
}
