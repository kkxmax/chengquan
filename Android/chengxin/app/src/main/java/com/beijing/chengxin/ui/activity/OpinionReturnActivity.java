package com.beijing.chengxin.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.ui.view.BaseView;
import com.beijing.chengxin.ui.widget.Utils;

import static com.beijing.chengxin.config.Constants.ERROR_DUPLICATE;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class OpinionReturnActivity extends ParentFragmentActivity {

    public final String TAG = OpinionReturnActivity.class.getName();
    private  static int MIN_LETTER_COUNT = 10;
    private  static int MAX_LETTER_COUNT = 100;

    EditText edtText;
    TextView txtLetterCount;
    RelativeLayout layoutSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_return);

        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.opinion_return));
        findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
        findViewById(R.id.btn_send).setOnClickListener(mButtonClickListener);

        txtLetterCount = (TextView)findViewById(R.id.txt_letter_count);
        txtLetterCount.setText(String.format("%d/%d", 0, MAX_LETTER_COUNT));

        edtText = (EditText)findViewById(R.id.edit_opinion);
        edtText.setHint(Html.fromHtml("<font  color='#888888'>快来写下你的建议吧。您也可以通过以下方式联系我们</font><font color='#333333'>，电话：010-67686510。</font>"));
        edtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = edtText.getText().toString();
                int length = content.length();
                txtLetterCount.setText(String.format("%d/%d", length, MAX_LETTER_COUNT));

                if (length > MAX_LETTER_COUNT) {
                    edtText.setText(content.substring(0, MAX_LETTER_COUNT));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        layoutSuccess = (RelativeLayout)findViewById(R.id.layout_success);
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send:
                    String msg = edtText.getText().toString();
                    if (msg.length() < MIN_LETTER_COUNT) {
                        Toast.makeText(OpinionReturnActivity.this, getString(R.string.err_opinion_min_letter_count), Toast.LENGTH_LONG).show();
                    } else {
                        sendDataTask(msg);
                    }
                    break;
                case R.id.btn_back:
                    onBackPressed();
                    break;
            }
        }
    };

    private void gotoBackScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
            }
        }, 2000);
    }

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
                        layoutSuccess.setVisibility(View.VISIBLE);
                        gotoBackScreen();
                    } else if (result.getRetCode() == ERROR_DUPLICATE) {
                        ChengxinApplication.finishActivityFromDuplicate(OpinionReturnActivity.this);
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
