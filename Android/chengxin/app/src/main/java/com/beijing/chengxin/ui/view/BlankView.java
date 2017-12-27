package com.beijing.chengxin.ui.view;

import android.content.Context;

import com.beijing.chengxin.R;

public class BlankView extends BaseView {

    public BlankView(Context context) {
        super(context);
        initialize();
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.view_blank);
    }

    @Override
    protected void initData() {
        super.initData();
    }

}
