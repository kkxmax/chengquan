package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.InviterModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.activity.ChengxinReportActivity;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.squareup.picasso.Picasso;

public class ChengxinReportPersonView extends BaseView {

    private Context mContext;

    private UserModel mUser, mInviter;

    // Personal UI Controller
    ImageView imgLogo;
    TextView txtName, txtHangye, txtCode, txtCredit, txtElectCnt, txtFeedbackCnt, txtEnterName;
    TextView txtCity, txtWeixin, txtJob, txtRecommender, txtHistory, txtExperience;
    // Personal's Recommender UI Controller
    View viewRecommendPart;
    ImageView imgRecommendLogo;
    TextView txtRecommendLogoType, txtRecommendName, txtRecommendHangye, txtRecommendCode, txtRecommendCredit1;
    TextView txtRecommendCredit2, txtRecommendElectCnt, txtRecommendFeedbackCnt;

    public ChengxinReportPersonView(Context context) {
        super(context);
        mContext = context;
        initialize();
    }

    public ChengxinReportPersonView(Context context, UserModel userInfo, InviterModel inviterInfo) {
        super(context);
        mContext = context;
        mUser = userInfo;
        mInviter = inviterInfo.getInviterInfo();
        initialize();
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.view_chengxin_report_person);

        // Personal UI Controller
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtHangye = (TextView) findViewById(R.id.txt_hangye);
        txtCode = (TextView) findViewById(R.id.txt_code);
        txtCredit = (TextView) findViewById(R.id.txt_credit);
        txtElectCnt = (TextView) findViewById(R.id.txt_elect_cnt);
        txtFeedbackCnt = (TextView) findViewById(R.id.txt_feedback_cnt);
        txtEnterName = (TextView) findViewById(R.id.txt_enter_name);
        txtCity = (TextView) findViewById(R.id.txt_city);
        txtWeixin = (TextView) findViewById(R.id.txt_weixin);
        txtJob = (TextView) findViewById(R.id.txt_job);
        txtRecommender = (TextView) findViewById(R.id.txt_recommender);
        txtHistory = (TextView) findViewById(R.id.txt_history);
        txtExperience = (TextView) findViewById(R.id.txt_experience);
        // Personal's Recommender UI Controller
        viewRecommendPart = (View) findViewById(R.id.view_recommed_part);
        imgRecommendLogo = (ImageView) findViewById(R.id.img_recommend_logo);
        txtRecommendLogoType = (TextView) findViewById(R.id.txt_recommend_logo_type);
        txtRecommendName = (TextView) findViewById(R.id.txt_recommend_name);
        txtRecommendHangye = (TextView) findViewById(R.id.txt_recommend_hangye);
        txtRecommendCode = (TextView) findViewById(R.id.txt_recommend_code);
        txtRecommendCredit1 = (TextView) findViewById(R.id.txt_recommend_credit_1);
        txtRecommendCredit2 = (TextView) findViewById(R.id.txt_recommend_credit_2);
        txtRecommendElectCnt = (TextView) findViewById(R.id.txt_recommend_elect_cnt);
        txtRecommendFeedbackCnt = (TextView) findViewById(R.id.txt_recommend_feedback_cnt);
    }

    @Override
    protected void initData() {
        super.initData();

        // Personal UI Controller
        Picasso.with(getContext()).load(Constants.FILE_ADDR + mUser.getLogo()).placeholder(R.drawable.no_image).into(imgLogo);
        String name = "";
        if (mUser.getTestStatus() == Constants.TEST_STATUS_PASSED) {
            name = mUser.getAkind() == 1 ? mUser.getRealname() : mUser.getEnterName();
        } else {
            name = mUser.getMobile();
        }
        txtName.setText(name);
        if (mUser.getXyName() == null || mUser.getXyName().length() == 0)
            txtHangye.setVisibility(View.GONE);
        else
            txtHangye.setVisibility(View.VISIBLE);
        txtHangye.setText(mUser.getXyName());
        txtCode.setText(mUser.getCode());
        txtCode.setText(mUser.getCode() == null || mUser.getCode().length() == 0 ? getString(R.string.pls_cert) : mUser.getCode());
        txtCredit.setText(mUser.getCredit() == 0 ? getString(R.string.rate_zero) : "" + mUser.getCredit() + "%");
        txtElectCnt.setText("" + mUser.getElectCnt());
        txtFeedbackCnt.setText("" + mUser.getFeedbackCnt());
        txtEnterName.setText(mUser.getEnterName());
        txtEnterName.setOnClickListener(mClickListener);
        txtCity.setText(mUser.getCityName());
        txtWeixin.setText(mUser.getWeixin());
        txtJob.setText(mUser.getJob());
        txtHistory.setText(mUser.getHistory());
        txtExperience.setText(mUser.getExperience());

        String recommenderName = "没有";
        if (mInviter != null) {
            if (mInviter.getAkind() == Constants.ACCOUNT_TYPE_PERSON) {
                recommenderName = (mInviter.getRealname().length() == 0 ? mInviter.getMobile() : mInviter.getRealname());
            } else {
                recommenderName = mInviter.getEnterName();
            }
            // Personal's Recommender UI Controller
            Picasso.with(getContext()).load(Constants.FILE_ADDR + mInviter.getLogo()).placeholder(R.drawable.no_image).into(imgRecommendLogo);
            txtRecommendLogoType.setText(mInviter.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? R.string.str_person : R.string.str_enterprise);

            String inviterName = mInviter.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? mInviter.getRealname() : mInviter.getEnterName();
            if (inviterName.length() == 0)
                inviterName = mInviter.getMobile();
            txtRecommendName.setText(inviterName);
            txtRecommendHangye.setText(mInviter.getXyName());
            txtRecommendCode.setText(mInviter.getCode());
            txtRecommendCredit1.setText(mInviter.getCredit() == 0 ? getString(R.string.rate_zero) : "" + mInviter.getCredit() + "%");
            txtRecommendCredit2.setText(mInviter.getCredit() == 0 ? getString(R.string.rate_zero) : "" + mInviter.getCredit() + "%");
            txtRecommendElectCnt.setText("" + mInviter.getElectCnt());
            txtRecommendFeedbackCnt.setText("" + mInviter.getFeedbackCnt());

            viewRecommendPart.setOnClickListener(mClickListener);
            txtRecommender.setOnClickListener(mClickListener);
        } else {
            // Personal's Recommender UI Controller
            viewRecommendPart.setVisibility(View.GONE);
        }
        txtRecommender.setText(recommenderName);
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.txt_enter_name:
                    if (mUser.getEnterName() != null && mUser.getEnterName().length() > 0) {
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra("id", mUser.getEnterId());
                        intent.putExtra("type", Constants.INDEX_ENTERPRISE);
                        intent.putExtra("akind", mUser.getEnterKind());

                        mActivity.startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                        ((ChengxinReportActivity) getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    break;
                case R.id.view_recommed_part:
                case R.id.txt_recommender:
                    if (mInviter.getTestStatus() == Constants.TEST_STATUS_PASSED) {
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra("id", mInviter.getId());
                        intent.putExtra("type", Constants.INDEX_ENTERPRISE);
                        intent.putExtra("akind", mInviter.getAkind());
                        mActivity.startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                        ((ChengxinReportActivity) getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        Toast.makeText(mActivity, getString(R.string.err_not_test_passed_person), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

}
