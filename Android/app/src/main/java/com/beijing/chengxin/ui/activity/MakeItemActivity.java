package com.beijing.chengxin.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beijing.chengxin.R;

public class MakeItemActivity extends ParentFragmentActivity {

    public final String TAG = MakeItemActivity.class.getName();
    private  static int MAX_LETTER_COUNT = 300;

    private ImageButton  btnBack;
    private EditText mEditText;
    private  TextView txtLetterCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_item);
        // set title

        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.make_item));

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mClickListener);

        txtLetterCount = (TextView)findViewById(R.id.txt_letter_count);
        txtLetterCount.setText(String.format("0/%d", MAX_LETTER_COUNT));

        mEditText = (EditText)findViewById(R.id.edit_item_comment);
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String content = mEditText.getText().toString();
                int length = content.length();
                txtLetterCount.setText(String.format("%d/%d", length, MAX_LETTER_COUNT));

                if (length >= MAX_LETTER_COUNT) {
                    mEditText.setText(content.substring(0, MAX_LETTER_COUNT));
                    return true;
                }
                return false;
            }
        });
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
