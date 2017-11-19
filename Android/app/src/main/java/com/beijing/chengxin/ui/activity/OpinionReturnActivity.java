package com.beijing.chengxin.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.ui.widget.Utils;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class OpinionReturnActivity extends ParentFragmentActivity {

    public final String TAG = OpinionReturnActivity.class.getName();
    private  static int MAX_LETTER_COUNT = 100;

    EditText edtText;
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

        edtText = (EditText)findViewById(R.id.edit_opinion);
        edtText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String content = edtText.getText().toString();
                int length = content.length();
                txtLetterCount.setText(String.format("%d/%d", length, MAX_LETTER_COUNT));

                if (length >= MAX_LETTER_COUNT) {
                    edtText.setText(content.substring(0, MAX_LETTER_COUNT));
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
                    sendDataTask(edtText.getText().toString().trim());
                    break;
                case R.id.btn_back:
                    onBackPressed();
                    break;
            }
        }
    };

    private void sendDataTask(final String content) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(OpinionReturnActivity.this);
            }
            @Override
            protected Object doInBackground(Object... params) {
                return new SyncInfo(OpinionReturnActivity.this).syncLeaveOpinion(content);
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                BaseModel result = (BaseModel) o;
                if (result.isValid()) {
                    if (result.getRetCode() == ERROR_OK) {
                        Toast.makeText(OpinionReturnActivity.this, R.string.msg_success_submit, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OpinionReturnActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OpinionReturnActivity.this, getString(R.string.err_server), Toast.LENGTH_SHORT).show();
                }
                Utils.disappearProgressDialog();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute();
    }
}
