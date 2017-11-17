package com.beijing.chengxin.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beijing.chengxin.R;

public class OpinionReturnActivity extends ParentFragmentActivity {

    public final String TAG = OpinionReturnActivity.class.getName();
    private  static int MAX_LETTER_COUNT = 100;

    EditText mEditText;
    TextView txtLetterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_return);

        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.opinion_return));
        findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
        findViewById(R.id.btn_send).setOnClickListener(mButtonClickListener);

        txtLetterCount = (TextView)findViewById(R.id.txt_letter_count);
        txtLetterCount.setText(String.format("0/%d", MAX_LETTER_COUNT));

        mEditText = (EditText)findViewById(R.id.editText);
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

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send:
                case R.id.btn_back:
                    onBackPressed();
                    break;
            }
        }
    };
}
