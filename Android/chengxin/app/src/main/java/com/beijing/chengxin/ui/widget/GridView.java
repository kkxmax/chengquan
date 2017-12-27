package com.beijing.chengxin.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;

public class GridView extends android.widget.GridView {

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public GridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public GridView(Context context) {
        super(context);
    }

    @Override
	protected void onMeasure(int widthSpec, int heightSpec) {
		super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(0x1FFFFFFF, MeasureSpec.AT_MOST));
	}
}