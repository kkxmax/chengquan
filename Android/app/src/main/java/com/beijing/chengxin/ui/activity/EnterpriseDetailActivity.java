package com.beijing.chengxin.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;

public class EnterpriseDetailActivity extends ParentFragmentActivity {

    public final String TAG = EnterpriseDetailActivity.class.getName();

    private ImageButton btnShare , btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        // set title

        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.enterprise_detail));
        btnShare = (ImageButton)findViewById(R.id.btn_share);
        btnShare.setVisibility(View.VISIBLE);

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
