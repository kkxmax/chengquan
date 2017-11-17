package com.beijing.chengxin.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;

public class ListViewNoScroll extends android.widget.GridView {

    public ListViewNoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewNoScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ListViewNoScroll(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ListViewNoScroll(Context context) {
        super(context);
    }

    @Override
	protected void onMeasure(int widthSpec, int heightSpec) {
		super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(0x1FFFFFFF, MeasureSpec.AT_MOST));
	}
}