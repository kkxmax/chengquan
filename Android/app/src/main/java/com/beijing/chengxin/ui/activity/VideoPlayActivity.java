package com.beijing.chengxin.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;

public class VideoPlayActivity extends BaseFragmentActivity {

    public final String TAG = VideoPlayActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.video_play));
        findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    onBackActivity();
                    break;
            }
        }
    };
}
