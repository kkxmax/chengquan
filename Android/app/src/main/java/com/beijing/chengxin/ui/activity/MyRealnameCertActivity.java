package com.beijing.chengxin.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;

public class MyRealnameCertActivity extends ParentFragmentActivity {

    public final String TAG = MyRealnameCertActivity.class.getName();

    private ImageButton  btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_realname_cert);
        // set title
        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.realname_cert));

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_back:
                        onBackPressed();
                        break;
                }
        }
    };

}
