package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.model.InviterModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.activity.ChengxinReportActivity;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

public class ChengxinReportEnterpriseView extends BaseView {

    private Context mContext;

    private UserModel mUser, mInviter;

    // Enterprise UI Controller
    ImageView imgLogoEnter, imgCertPhotoEnter;
    TextView txtNameEnter, txtHangyeEnter, txtCodeEnter, txtCreditEnter, txtElectCntEnter, txtFeedbackCntEnter;
    TextView txtRecommendEnter, txtCommentEnter, txtWeburlEnter, txtMainjobEnter, txtWeixinEnter;
    TextView txtBossNameEnter, txtBossJobEnter, txtBossMobileEnter, txtBossWeixinEnter, txtCityEnter;
    // Enterprise's Recommender UI Controller
    View viewRecommendPart;
    ImageView imgRecommendLogoEnter;
    TextView txtRecommendLogoTypeEnter, txtRecommendNameEnter, txtRecommendHangyeEnter, txtRecommendCodeEnter;
    TextView txtRecommendCredit1Enter, txtRecommendCredit2Enter, txtRecommendElectCntEnter, txtRecommendFeedbackCntEnter;

    public ChengxinReportEnterpriseView(Context context) {
        super(context);
        mContext = context;
        initialize();
    }

    public ChengxinReportEnterpriseView(Context context, UserModel userInfo, InviterModel inviterInfo) {
        super(context);
        mContext = context;
        mUser = userInfo;
        mInviter = inviterInfo.getInviterInfo();
        initialize();
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.view_chengxin_report_enter);

        // Enterprise UI Controller
        imgLogoEnter = (ImageView) findViewById(R.id.img_logo_enter);
        imgCertPhotoEnter = (ImageView) findViewById(R.id.img_cert_photo_enter);
        txtNameEnter = (TextView) findViewById(R.id.txt_name_enter);
        txtHangyeEnter = (TextView) findViewById(R.id.txt_hangye_enter);
        txtCodeEnter = (TextView) findViewById(R.id.txt_code_enter);
        txtCreditEnter = (TextView) findViewById(R.id.txt_credit_enter);
        txtElectCntEnter = (TextView) findViewById(R.id.txt_elect_cnt_enter);
        txtFeedbackCntEnter = (TextView) findViewById(R.id.txt_feedback_cnt_enter);
        txtRecommendEnter = (TextView) findViewById(R.id.txt_recommend_enter);
        txtCommentEnter = (TextView) findViewById(R.id.txt_comment_enter);
        txtWeburlEnter = (TextView) findViewById(R.id.txt_weburl_enter);
        txtMainjobEnter = (TextView) findViewById(R.id.txt_mainjob_enter);
        txtWeixinEnter = (TextView) findViewById(R.id.txt_weixin_enter);
        txtBossNameEnter = (TextView) findViewById(R.id.txt_boss_name_enter);
        txtBossJobEnter = (TextView) findViewById(R.id.txt_boss_job_enter);
        txtBossMobileEnter = (TextView) findViewById(R.id.txt_boss_mobile_enter);
        txtBossWeixinEnter = (TextView) findViewById(R.id.txt_boss_weixin_enter);
        txtCityEnter = (TextView) findViewById(R.id.txt_city_enter);
        // Enterprise's Recommender UI Controller
        viewRecommendPart = (View) findViewById(R.id.view_recommed_part);
        imgRecommendLogoEnter = (ImageView) findViewById(R.id.img_recommend_logo_enter);
        txtRecommendLogoTypeEnter = (TextView) findViewById(R.id.txt_recommend_logo_type_enter);
        txtRecommendNameEnter = (TextView) findViewById(R.id.txt_recommend_name_enter);
        txtRecommendCodeEnter = (TextView) findViewById(R.id.txt_recommend_code_enter);
        txtRecommendCredit1Enter = (TextView) findViewById(R.id.txt_recommend_credit1_enter);
        txtRecommendCredit2Enter = (TextView) findViewById(R.id.txt_recommend_credit2_enter);
        txtRecommendElectCntEnter = (TextView) findViewById(R.id.txt_recommend_elect_cnt_enter);
        txtRecommendFeedbackCntEnter = (TextView) findViewById(R.id.txt_recommend_feedback_cnt_enter);
        txtRecommendHangyeEnter = (TextView) findViewById(R.id.txt_recommend_hangye_enter);
    }

    @Override
    protected void initData() {
        super.initData();

        // Enterprise UI Controller
        Picasso.with(getContext()).load(Constants.FILE_ADDR + mUser.getLogo()).placeholder(R.drawable.no_image).into(imgLogoEnter);
        Picasso.with(getContext()).load(Constants.FILE_ADDR + mUser.getEnterCertImage()).placeholder(R.drawable.no_image).into(imgCertPhotoEnter);
        String name = "";
        if (mUser.getTestStatus() == Constants.TEST_STATUS_PASSED) {
            name = mUser.getAkind() == 1 ? mUser.getRealname() : mUser.getEnterName();
        } else {
            name = mUser.getMobile();
        }
        txtNameEnter.setText(name);
        if (mUser.getXyName() == null || mUser.getXyName().length() == 0)
            txtHangyeEnter.setVisibility(View.GONE);
        else
            txtHangyeEnter.setVisibility(View.VISIBLE);
        txtHangyeEnter.setText(mUser.getXyName());
        txtCodeEnter.setText(mUser.getCode());
        txtCreditEnter.setText("" + mUser.getCredit());
        txtCreditEnter.setText(mUser.getCredit() == 0 ? getString(R.string.rate_zero) : "" + mUser.getCredit() + "%");
        txtElectCntEnter.setText("" + mUser.getElectCnt());
        txtFeedbackCntEnter.setText("" + mUser.getFeedbackCnt());
        txtRecommendEnter.setText(mUser.getRecommend().length() == 0 ? "没有" : mUser.getRecommend());
        txtCommentEnter.setText(mUser.getComment());
        txtWeburlEnter.setText(mUser.getWeburl());
        txtMainjobEnter.setText(mUser.getMainJob());
        txtWeixinEnter.setText(mUser.getWeixin());
        txtBossNameEnter.setText(mUser.getBossName());
        txtBossJobEnter.setText(mUser.getBossJob());
        txtBossMobileEnter.setText(mUser.getBossMobile());
        txtBossWeixinEnter.setText(mUser.getBossWeixin());
        txtCityEnter.setText(mUser.getCityName());

        // Enterprise's Recommender UI Controller
        if (mInviter != null) {
            Picasso.with(getContext()).load(Constants.FILE_ADDR + mInviter.getLogo()).placeholder(R.drawable.no_image).into(imgRecommendLogoEnter);
            txtRecommendLogoTypeEnter.setText(mInviter.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? R.string.str_person : R.string.str_enterprise);

            String inviterName = mInviter.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? mInviter.getRealname() : mInviter.getEnterName();
            if (inviterName.length() == 0)
                inviterName = mInviter.getMobile();
            txtRecommendNameEnter.setText(inviterName);
            txtRecommendHangyeEnter.setText(mInviter.getXyName());
            txtRecommendCodeEnter.setText(mInviter.getCode());
            txtRecommendCredit1Enter.setText(mInviter.getCredit() == 0 ? getString(R.string.rate_zero) : "" + mInviter.getCredit() + "%");
            txtRecommendCredit2Enter.setText(mInviter.getCredit() == 0 ? getString(R.string.rate_zero) : "" + mInviter.getCredit() + "%");
            txtRecommendElectCntEnter.setText("" + mInviter.getElectCnt());
            txtRecommendFeedbackCntEnter.setText("" + mInviter.getFeedbackCnt());

            txtWeburlEnter.setOnClickListener(mClickListener);
            viewRecommendPart.setOnClickListener(mClickListener);
        } else {
            viewRecommendPart.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.txt_weburl_enter:
                    if (mUser.getWeburl() != null && mUser.getWeburl().length() > 0) {
                        try {
                            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CommonUtils.getCompleteWebUrl(mUser.getWeburl()))));
                        } catch (Exception e) {
                        }
                    }
                    break;
                case R.id.view_recommed_part:
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
