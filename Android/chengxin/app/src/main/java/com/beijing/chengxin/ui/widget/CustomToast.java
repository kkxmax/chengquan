package com.beijing.chengxin.ui.widget;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.widget.Toast;

public class CustomToast extends Toast {

    public CustomToast(Context context) {
        super(context);
        this.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public static Toast makeText(Context context, CharSequence text, @Snackbar.Duration int duration) {
        Toast result = Toast.makeText(context, text, duration);
        result.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);

        return result;
    }

}
