package com.beijing.chengxin.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.InviterModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.view.ChengxinReportEnterpriseView;
import com.beijing.chengxin.ui.view.ChengxinReportPersonView;
import com.beijing.chengxin.ui.widget.Utils;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class ChengxinReportActivity extends ParentFragmentActivity {

    public final String TAG = ChengxinReportActivity.class.getName();

    private ImageButton btnShare , btnBack;

    LinearLayout viewBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chengxin_report);
        // set title
        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.chengxin_report));
        btnShare = (ImageButton)findViewById(R.id.btn_share);
        btnShare.setVisibility(View.VISIBLE);

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mClickListener);

        viewBody = (LinearLayout) findViewById(R.id.view_body);

        getDataTask();
    }

    private void getDataTask() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                Utils.displayProgressDialog(ChengxinReportActivity.this);
            }
            @Override
            protected Object doInBackground(Object... params) {
                return new SyncInfo(ChengxinReportActivity.this).syncInviter();
            }
            @Override
            protected void onPostExecute(Object o) {
                InviterModel result = (InviterModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        viewBody.removeAllViews();
                        UserModel user = SessionInstance.getInstance().getLoginData().getUser();
                        if (user.getAkind() == Constants.ACCOUNT_TYPE_PERSON) {
                            ChengxinReportPersonView personView = new ChengxinReportPersonView(ChengxinReportActivity.this, user, result);
                            viewBody.addView(personView);
                        } else if (user.getAkind() == Constants.ACCOUNT_TYPE_ENTERPRISE) {
                            ChengxinReportEnterpriseView enterpriseView = new ChengxinReportEnterpriseView(ChengxinReportActivity.this, user, result);
                            viewBody.addView(enterpriseView);
                        }
                    } else {
                        Toast.makeText(ChengxinReportActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChengxinReportActivity.this, getString(R.string.err_server), Toast.LENGTH_SHORT).show();
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
