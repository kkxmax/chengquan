package com.beijing.chengxin.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.AccountModel;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.ReqCodeModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.activity.ChengxinLogActivity;
import com.beijing.chengxin.ui.activity.ChengxinReportActivity;
import com.beijing.chengxin.ui.activity.EvalMeActivity;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.activity.MyCollectActivity;
import com.beijing.chengxin.ui.activity.MyErrorCorrectActivity;
import com.beijing.chengxin.ui.activity.MyEvalActivity;
import com.beijing.chengxin.ui.activity.MyRealnameCertActivity;
import com.beijing.chengxin.ui.activity.MySettingActivity;
import com.beijing.chengxin.ui.activity.MyWriteActivity;
import com.beijing.chengxin.ui.activity.OpinionReturnActivity;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class MainMeFragment extends Fragment {

	public final String TAG = MainMeFragment.class.getName();
    private View rootView;
    private Context mContext;

    ImageView imgAvatar;
    TextView txtName, txtChengxinId, txtChengxinRate, txtLikeCount, txtEvalCount;
    Button btnRealnameCert, btnReport, btnLog;
    TextView txtMyEval, txtEvalMe, txtMyError, txtMyWrite, txtMyCollect, txtOpinion, txtMySetting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_main_me, container, false);
        mContext = getContext();

        imgAvatar = (ImageView) rootView.findViewById(R.id.img_avatar);
        txtName = (TextView) rootView.findViewById(R.id.txt_name);
        txtChengxinId = (TextView) rootView.findViewById(R.id.txt_chengxin_id);
        txtChengxinRate = (TextView) rootView.findViewById(R.id.txt_chengxin_rate);
        txtLikeCount = (TextView) rootView.findViewById(R.id.txt_like_count);
        txtEvalCount = (TextView) rootView.findViewById(R.id.txt_eval_count);

        btnRealnameCert = (Button)rootView.findViewById(R.id.btn_realname_cert);
        btnReport = (Button)rootView.findViewById(R.id.btn_chengxin_report);
        btnLog = (Button)rootView.findViewById(R.id.btn_chengxin_log);

        txtMyEval = (TextView) rootView.findViewById(R.id.txt_my_eval);
        txtEvalMe = (TextView) rootView.findViewById(R.id.txt_eval_me);
        txtMyError = (TextView) rootView.findViewById(R.id.txt_my_error_correct);
        txtMyWrite = (TextView) rootView.findViewById(R.id.txt_my_write);
        txtMyCollect = (TextView) rootView.findViewById(R.id.txt_my_collect);
        txtOpinion = (TextView) rootView.findViewById(R.id.txt_opinion_return);
        txtMySetting = (TextView) rootView.findViewById(R.id.txt_setting);

        rootView.findViewById(R.id.btn_request_friend).setOnClickListener(mButtonClickListener);
        imgAvatar.setOnClickListener(mButtonClickListener);
        btnRealnameCert.setOnClickListener(mButtonClickListener);
        btnReport.setOnClickListener(mButtonClickListener);
        btnLog.setOnClickListener(mButtonClickListener);

        txtMyEval.setOnClickListener(mButtonClickListener);
        txtEvalMe.setOnClickListener(mButtonClickListener);
        txtMyError.setOnClickListener(mButtonClickListener);
        txtMyWrite.setOnClickListener(mButtonClickListener);
        txtMyCollect.setOnClickListener(mButtonClickListener);
        txtOpinion.setOnClickListener(mButtonClickListener);
        txtMySetting.setOnClickListener(mButtonClickListener);

        getDataTask();

        return rootView;
    }

    private void initData() {
        UserModel data = SessionInstance.getInstance().getLoginData().getUser();
        Picasso.with(getContext())
                .load(Constants.FILE_ADDR + data.getLogo())
                .placeholder(R.drawable.add_touxiang)
                .into(imgAvatar);
        String name = "";
        if (data.getTestStatus() == Constants.TEST_STATUS_PASSED) {
            name = data.getAkind() == 1 ? data.getRealname() : data.getEnterName();
        } else {
            name = data.getMobile();
        }
        txtName.setText(name);
        txtChengxinId.setText(data.getTestStatus() == 0 ? getString(R.string.pls_cert) : data.getCode());
        txtChengxinRate.setText(data.getCredit() == 0 ? getString(R.string.rate_zero) : "" + data.getCredit() + "%");
        txtLikeCount.setText("" + data.getElectCnt());
        txtEvalCount.setText("" + data.getFeedbackCnt());
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Constants.NOTIFY_USERMODEL_CHANGED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_request_friend:
                    inviteFriend();
                    break;
                case R.id.img_avatar:
                case R.id.btn_realname_cert:
                    intent = new Intent(getActivity(), MyRealnameCertActivity.class);
                    break;
                case R.id.btn_chengxin_report:
                    intent = new Intent(getActivity(), ChengxinReportActivity.class);
                    break;
                case R.id.btn_chengxin_log:
                    intent = new Intent(getActivity(), ChengxinLogActivity.class);
                    break;
                case R.id.txt_my_eval:
                    intent = new Intent(getActivity(), MyEvalActivity.class);
                    break;
                case R.id.txt_eval_me:
                    intent = new Intent(getActivity(), EvalMeActivity.class);
                    break;
                case R.id.txt_my_error_correct:
                    intent = new Intent(getActivity(), MyErrorCorrectActivity.class);
                    break;
                case R.id.txt_my_write:
                    intent = new Intent(getActivity(), MyWriteActivity.class);
                    break;
                case R.id.txt_my_collect:
                    intent = new Intent(getActivity(), MyCollectActivity.class);
                    break;
                case R.id.txt_opinion_return:
                    intent = new Intent(getActivity(), OpinionReturnActivity.class);
                    break;
                case R.id.txt_setting:
                    intent = new Intent(getActivity(), MySettingActivity.class);
                    break;
            }
            if (intent != null) {
                startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constants.NOTIFY_USERMODEL_CHANGED) {
                initData();
            }
        }
    };

    private void getDataTask() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                Utils.displayProgressDialog(mContext);
            }
            @Override
            protected Object doInBackground(Object... params) {
                return new SyncInfo(mContext).syncAccountInfo(0);
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                AccountModel result = (AccountModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == Constants.ERROR_OK) {
                        SessionInstance.getInstance().getLoginData().getUser().setCredit(result.getAccount().getCredit());
                        initData();
                    } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                        ChengxinApplication.finishAndLoginActivityFromDuplicate(getActivity());
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, R.string.error_real_cert_failed, Toast.LENGTH_SHORT).show();
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
    public void showShare(Context context, String platformToShare, boolean showContentEdit, final String url) {
        cn.sharesdk.onekeyshare.OnekeyShare oks = new cn.sharesdk.onekeyshare.OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }

        oks.setTitle("沟通太难？产品不靠谱？用“诚乎”，找诚信人，拒绝忽悠！");
        oks.setTitleUrl(url);
        oks.setText("准备好了吗？诚信实时评价，你的话语权你做主！评价有回报，共建社会诚信生态系统，加入我们吧！");
        oks.setImageUrl(Constants.LOGO_URL);
        oks.setUrl(url);

        oks.setShareContentCustomizeCallback(new
                                                     ShareContentCustomizeCallback() {
                                                         @Override
                                                         public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                                                             if ("WechatMoments".equals(platform.getName())) {
                                                                 paramsToShare.setTitle("沟通太难？产品不靠谱？用“诚乎”，找诚信人，拒绝忽悠！");
                                                                 paramsToShare.setText("准备好了吗？诚信实时评价，你的话语权你做主！评价有回报，共建社会诚信生态系统，加入我们吧！");
                                                                 paramsToShare.setImageUrl(Constants.LOGO_URL);
                                                                 paramsToShare.setUrl(url);
                                                             }
                                                         }
                                                     });
        oks.show(context);
    }

    private void inviteFriend() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                Utils.displayProgressDialog(mContext);
            }
            @Override
            protected Object doInBackground(Object... params) {
                return new SyncInfo(mContext).syncInviteFriend();
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                ReqCodeModel result = (ReqCodeModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == Constants.ERROR_OK) {
//                        Toast.makeText(mContext, "Request Code is "+result.getReqCode(), Toast.LENGTH_LONG).show();
                        // add dd -- 2017.12.07
                        showShare(mContext, null, true, String.format(Constants.INVITE_URL, result.getReqCode(), SessionInstance.getInstance().getLoginData().getUser().getId()));

                    } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                        ChengxinApplication.finishAndLoginActivityFromDuplicate(getActivity());
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, R.string.error_real_cert_failed, Toast.LENGTH_SHORT).show();
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
