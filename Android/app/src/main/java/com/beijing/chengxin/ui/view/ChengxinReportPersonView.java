package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    ImageView imgRecommendLogo, imgRecommendDetail;
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
        imgRecommendLogo = (ImageView) findViewById(R.id.img_recommend_logo);
        imgRecommendDetail = (ImageView) findViewById(R.id.img_recommend_detail);
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
        txtName.setText(mUser.getRealname());
        txtHangye.setText(mUser.getXyName());
        txtCode.setText(mUser.getCode());
        txtCredit.setText("" + mUser.getCredit());
        txtElectCnt.setText("" + mUser.getElectCnt());
        txtFeedbackCnt.setText("" + mUser.getFeedbackCnt());
        txtEnterName.setText("" + mUser.getEnterName());
        txtCity.setText(mUser.getCityName());
        txtWeixin.setText(mUser.getWeixin());
        txtJob.setText(mUser.getJob());
        txtRecommender.setText(getString(R.string.str_1_du_friend) + " - " + (mInviter.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? mInviter.getRealname() : mInviter.getEnterName()));
        txtHistory.setText(mUser.getHistory());
        txtExperience.setText(mUser.getExperience());
        // Personal's Recommender UI Controller
        Picasso.with(getContext()).load(Constants.FILE_ADDR + mInviter.getLogo()).placeholder(R.drawable.no_image).into(imgRecommendLogo);
        txtRecommendLogoType.setText(mInviter.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? R.string.str_person : R.string.str_enterprise);
        txtRecommendName.setText(mInviter.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? mInviter.getRealname() : mInviter.getEnterName());
        txtRecommendHangye.setText(mInviter.getXyName());
        txtRecommendCode.setText(mInviter.getCode());
        txtRecommendCredit1.setText("" + mInviter.getCredit());
        txtRecommendCredit2.setText("" + mInviter.getCredit());
        txtRecommendElectCnt.setText("" + mInviter.getElectCnt());
        txtRecommendFeedbackCnt.setText("" + mInviter.getFeedbackCnt());

        imgRecommendDetail.setOnClickListener(mClickListener);
        txtRecommender.setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.img_recommend_detail:
                case R.id.txt_recommender:
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra("id", mInviter.getId());
                    intent.putExtra("type", Constants.INDEX_ENTERPRISE);
                    intent.putExtra("akind", mInviter.getAkind());
                    getContext().startActivity(intent);
                    ((ChengxinReportActivity)getContext()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
            }
        }
    };

}
