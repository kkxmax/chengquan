package com.beijing.chengxin.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class BaseView extends LinearLayout {

    protected Activity mActivity;
//    protected OnActionListener mActionListener;

    public BaseView(Context context) {
        super(context);
        init(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mActivity = (Activity) context;
        setClickable(true);
    }

    public void initialize() {
        initUI();
        initData();
    }

    protected void initUI() {
    }

    protected void initData() {
    }

    public void refreshUI() {
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void setContentView(int layoutId) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        inflater.inflate(layoutId, this);
    }

//    public void setOnActionListener(OnActionListener listener) {
//        mActionListener = listener;
//    }

    protected String getString(int stringId) {
        return mActivity.getString(stringId);
    }

    protected LayoutInflater getLayoutInflater() {
        return mActivity.getLayoutInflater();
    }

//    protected void sendAction(int actionId) {
//        if (mActionListener != null) {
//            mActionListener.onAction(actionId, null);
//        }
//    }
//
//    protected void sendAction(int actionId, Object actionParam) {
//        if (mActionListener != null) {
//            mActionListener.onAction(actionId, actionParam);
//        }
//    }
//
//    public static interface OnActionListener {
//        public void onAction(int actionId, Object actionParam);
//    }

}