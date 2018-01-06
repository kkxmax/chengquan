package com.beijing.chengxin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.InviterModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.view.ChengxinReportEnterpriseView;
import com.beijing.chengxin.ui.view.ChengxinReportPersonView;
import com.beijing.chengxin.ui.widget.Utils;
import com.hy.chengxin.http.Api.HttpApi;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import static com.beijing.chengxin.config.Constants.ERROR_DUPLICATE;
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
        btnShare.setOnClickListener(mClickListener);

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mClickListener);

        viewBody = (LinearLayout) findViewById(R.id.view_body);

        getDataTask();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULT_CODE_LOGIN_DUPLICATE)
            ChengxinApplication.finishActivityFromDuplicate(this);
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
                    } else if (result.getRetCode() == ERROR_DUPLICATE) {
                        ChengxinApplication.finishActivityFromDuplicate(ChengxinReportActivity.this);
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

    // add dd -- 2017.12.07
    public void showShare(Context context, String platformToShare, boolean showContentEdit) {
        cn.sharesdk.onekeyshare.OnekeyShare oks = new cn.sharesdk.onekeyshare.OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }

        final UserModel user = SessionInstance.getInstance().getLoginData().getUser();

        String titleStr = "", urlStr = "", descStr = "";;

        if (user.getAkind() == Constants.ACCOUNT_TYPE_PERSON) {
            titleStr = "【诚信报告】您的好友给您分享了一份个人诚信，立即查看！";
            urlStr = String.format(Constants.PERSONAL_REPORT_SHARE_URL, user.getId(), user.getId());
            descStr += user.getRealname();
            descStr += ", 诚信度" + user.getCredit() + "%";
            descStr += ", " + user.getEnterName();
            descStr += ", " + user.getPositiveFeedbackCnt() + "个正面评价";
            descStr += ", " + user.getNegativeFeedbackCnt() + "个负面评价";
            descStr += ", 查看完整诚信报告！";

        } else {
            titleStr = "【诚信报告】您的好友给您分享了一份企业诚信，立即查看！";
            urlStr = String.format(Constants.ENTERPRISE_REPORT_SHARE_URL, user.getId(), user.getId());
            descStr += user.getEnterName();
            descStr += ", 诚信度" + user.getCredit() + "%";
            descStr += ", " + user.getPositiveFeedbackCnt() + "个正面评价";
            descStr += ", " + user.getNegativeFeedbackCnt() + "个负面评价";
            descStr += ", 查看完整诚信报告！";
        }

        final String title = titleStr;

        final String url = urlStr;

        oks.setTitle(title);
        oks.setTitleUrl(url);

        final String desc = descStr;

        oks.setText(desc);
        oks.setImageUrl(Constants.LOGO_URL);
        oks.setUrl(url);

        oks.setShareContentCustomizeCallback(new
                                                     ShareContentCustomizeCallback() {
                                                         @Override
                                                         public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                                                             if ("WechatMoments".equals(platform.getName())) {
                                                                 paramsToShare.setTitle(title);
                                                                 paramsToShare.setText(desc);
                                                                 paramsToShare.setImageUrl(Constants.LOGO_URL);
                                                                 paramsToShare.setUrl(url);
                                                             }
                                                         }
                                                     });

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d(TAG, "onComplete ---->  分享成功");
                platform.isClientValid();

                HttpApi.onShare(Constants.SHARE_KIND.REPORT, user.getId());
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d(TAG, "onError ---->  分享失败" + throwable.getStackTrace().toString());
                Log.d(TAG, "onError ---->  分享失败" + throwable.getMessage());
                throwable.getMessage();
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

        oks.show(context);
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
                case R.id.btn_share:
                    showShare(ChengxinReportActivity.this, null, true);
                    break;
            }
        }
    };

}
