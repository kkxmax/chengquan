package com.beijing.chengxin.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.UserDetailModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.activity.MakeEvaluationActivity;
import com.beijing.chengxin.ui.listener.OnViewSizeChangeListener;
import com.beijing.chengxin.ui.view.BaseView;
import com.beijing.chengxin.ui.view.TabComidityView;
import com.beijing.chengxin.ui.view.TabEvalView;
import com.beijing.chengxin.ui.view.TabItemView;
import com.beijing.chengxin.ui.view.TabServeView;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.beijing.chengxin.config.Constants.ACCOUNT_TYPE_PERSON;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class PersonDetailFragment extends Fragment implements OnViewSizeChangeListener {

	public final String TAG = PersonDetailFragment.class.getName();

    private View rootView;
    private ImageButton btnShare , btnBack, btnCall;
    private ToggleButton btnEval , btnComedity , btnItem , btnServe, btnFollow;
    private Button btnMakeEval;
    private ImageView img_avatar;
    private TextView txt_enter_type, txt_name, txt_leixing, txt_chengxin_id, txt_chengxin_rate, txt_like_count, txt_eval_count;
    private TextView txt_company_name, txt_weburl, txt_place, txt_weixin, txt_recommender, txt_position;

    MyAdapter mAdapter;
    ViewPager mPager;

    ArrayList<BaseView> mTabViews;
    TabEvalView mTabView1;
    TabComidityView mTabView2;
    TabItemView mTabView3;
    TabServeView mTabView4;

    SyncInfo info;
    int accountId;
    UserModel accountDetail;

    public void setId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_person_detail, container, false);

        // set title
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.person_detail));
        btnShare = (ImageButton)rootView.findViewById(R.id.btn_share);
        btnShare.setVisibility(View.VISIBLE);

        btnBack = (ImageButton)rootView.findViewById(R.id.btn_back);
        btnFollow = (ToggleButton)rootView.findViewById(R.id.btn_follow);
        btnEval = (ToggleButton)rootView.findViewById(R.id.btn_eval);
        btnComedity = (ToggleButton)rootView.findViewById(R.id.btn_comedity);
        btnItem = (ToggleButton)rootView.findViewById(R.id.btn_item);
        btnServe = (ToggleButton)rootView.findViewById(R.id.btn_serve);
        btnCall = (ImageButton)rootView.findViewById(R.id.btn_call);
        btnMakeEval = (Button) rootView.findViewById(R.id.btn_make_eval);

        btnBack.setOnClickListener(mClickListener);
        btnFollow.setOnClickListener(mClickListener);
        btnEval.setOnClickListener(mClickListener);
        btnComedity.setOnClickListener(mClickListener);
        btnItem.setOnClickListener(mClickListener);
        btnServe.setOnClickListener(mClickListener);
        btnCall.setOnClickListener(mClickListener);
        btnMakeEval.setOnClickListener(mClickListener);

        img_avatar = (ImageView)rootView.findViewById(R.id.img_avatar);

        txt_enter_type = (TextView)rootView.findViewById(R.id.txt_enter_type);
        txt_name = (TextView)rootView.findViewById(R.id.txt_name);
        txt_leixing = (TextView)rootView.findViewById(R.id.txt_leixing);
        txt_chengxin_id = (TextView)rootView.findViewById(R.id.txt_chengxin_id);
        txt_chengxin_rate = (TextView)rootView.findViewById(R.id.txt_chengxin_rate);
        txt_like_count = (TextView)rootView.findViewById(R.id.txt_like_count);
        txt_eval_count = (TextView)rootView.findViewById(R.id.txt_eval_count);

        txt_company_name = (TextView)rootView.findViewById(R.id.txt_company_name);
        txt_weburl = (TextView)rootView.findViewById(R.id.txt_weburl);
        txt_place = (TextView)rootView.findViewById(R.id.txt_place);
        txt_weixin = (TextView)rootView.findViewById(R.id.txt_weixin);
        txt_recommender = (TextView)rootView.findViewById(R.id.txt_recommender);
        txt_position = (TextView)rootView.findViewById(R.id.txt_position);

        txt_company_name.setOnClickListener(mClickListener);
        txt_weburl.setOnClickListener(mClickListener);
        txt_recommender.setOnClickListener(mClickListener);

        mTabView1 = new TabEvalView(getContext());
        mTabView2 = new TabComidityView(getContext());
        mTabView3 = new TabItemView(getContext());
        mTabView4 = new TabServeView(getContext());

        mTabView1.setOnViewSizeChangeListener(this);

        mTabViews = new ArrayList<BaseView>();
        mTabViews.add(mTabView1);
        mTabViews.add(mTabView2);
        mTabViews.add(mTabView3);
        mTabViews.add(mTabView4);

        mAdapter = new MyAdapter();
        mPager = (ViewPager)rootView.findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                selectTabButton(position);
                onChangedViewSize();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        onChangedViewSize();

        info = new SyncInfo(getActivity());
        if (accountId != 0)
            new AccountDetailAsync().execute();

        return rootView;
    }

    @Override
    public void onChangedViewSize() {
        BaseView itemView = mTabViews.get(mPager.getCurrentItem());
        itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height = itemView.getMeasuredHeight();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPager.getLayoutParams();
        //params.height = (int) (getResources().getDisplayMetrics().density * 100);
        params.height = height;
        mPager.setLayoutParams(params);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    ((BaseFragmentActivity)getActivity()).onBackPressed();
                    break;
                case R.id.txt_company_name:
                    EnterpriseDetailFragment efragment = new EnterpriseDetailFragment();
                    efragment.setId(accountDetail.getReqCodeSenderId());
                    ((BaseFragmentActivity)getActivity()).showFragment(efragment, true);
                    break;
                case R.id.txt_weburl:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(accountDetail.getWeburl())));
                    break;
                case R.id.txt_recommender:
                    Fragment fragment;
                    if (accountDetail.getReqCodeSenderAkind() == Constants.ACCOUNT_TYPE_ENTERPRISE) {
                        fragment = new EnterpriseDetailFragment();
                        ((EnterpriseDetailFragment)fragment).setId(accountDetail.getReqCodeSenderId());
                    }
                    else {
                        fragment = new PersonDetailFragment();
                        ((PersonDetailFragment)fragment).setId(accountDetail.getReqCodeSenderId());
                    }
                    ((BaseFragmentActivity)getActivity()).showFragment(fragment, true);
                    break;
                case R.id.btn_call:
                    new OnContactAsync().execute(String.valueOf(accountDetail.getId()));
                    break;
                case R.id.btn_eval:
                    selectTabButton(0);
                    mPager.setCurrentItem(0);
                    break;
                case R.id.btn_comedity:
                    selectTabButton(1);
                    mPager.setCurrentItem(1);
                    break;
                case R.id.btn_item:
                    selectTabButton(2);
                    mPager.setCurrentItem(2);
                    break;
                case R.id.btn_serve:
                    selectTabButton(3);
                    mPager.setCurrentItem(3);
                    break;
                case R.id.btn_follow:
                    String interested = (accountDetail.getInterested() == 1) ? "0" : "1";
                    new SetInterestAsync().execute(String.valueOf(accountDetail.getId()), interested);
                    break;
                case R.id.btn_make_eval:
                    Intent intent = new Intent(getActivity(), MakeEvaluationActivity.class);
                    intent.putExtra("accountId", accountDetail.getId());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
            }
        }
    };

    private void selectTabButton(int position){
        resetTabBtn();
        switch(position){
            case 0:
                btnEval.setChecked(true);
                break;
            case 1:
                btnComedity.setChecked(true);
                break;
            case 2:
                btnItem.setChecked(true);
                break;
            case 3:
                btnServe.setChecked(true);
                break;
        }
    }

    private void resetTabBtn(){
        btnEval.setChecked(false);
        btnComedity.setChecked(false);
        btnItem.setChecked(false);
        btnServe.setChecked(false);
    }

    public class MyAdapter extends PagerAdapter {
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mTabViews.get(arg1));
        }
        @Override
        public void finishUpdate(View arg0) {
        }
        @Override
        public int getCount() {
            return mTabViews.size();
        }
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mTabViews.get(arg1), 0);
            return mTabViews.get(arg1);
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }
        @Override
        public Parcelable saveState() {
            return null;
        }
        @Override
        public void startUpdate(View arg0) {
        }
    }

    class AccountDetailAsync extends AsyncTask<String, String, UserDetailModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected UserDetailModel doInBackground(String... strs) {
            return info.syncAccountDetail(String.valueOf(accountId));
        }
        @Override
        protected void onPostExecute(UserDetailModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    accountDetail = result.getAccount();
                    showUserDetail();
                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
            Utils.disappearProgressDialog();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Utils.disappearProgressDialog();
        }
    }

    class SetInterestAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncSetInterest(strs[0], strs[1]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    int interested = (accountDetail.getInterested() == 1) ? 0 : 1;
                    accountDetail.setInterested(interested);
                    btnFollow.setChecked(interested == 1);
                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
            Utils.disappearProgressDialog();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Utils.disappearProgressDialog();
        }
    }

    class OnContactAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncOnContact(strs[0]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + accountDetail.getMobile())));
                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
            Utils.disappearProgressDialog();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Utils.disappearProgressDialog();
        }
    }

    private void showUserDetail() {
        if (accountDetail == null)
            return;

        txt_name.setText(accountDetail.getRealname());
        txt_leixing.setText(accountDetail.getXyName());
        txt_chengxin_id.setText(accountDetail.getCode());
        if (accountDetail.getCredit() < Constants.LEVEL_ZERO)
            txt_chengxin_rate.setText(getText(R.string.rate_zero));
        else
            txt_chengxin_rate.setText(String.format("%d%%", accountDetail.getCredit()));
        txt_like_count.setText(String.valueOf(accountDetail.getElectCnt()));
        txt_eval_count.setText(String.valueOf(accountDetail.getFeedbackCnt()));

        txt_company_name.setText("testt-");
        txt_weburl.setText(accountDetail.getWeburl());
        txt_place.setText(accountDetail.getCityName() + " " + accountDetail.getAddr());
        txt_weixin.setText(accountDetail.getWeixin());
        txt_position.setText(accountDetail.getJob());
        if (accountDetail.getReqCodeSenderAkind() == ACCOUNT_TYPE_PERSON) {
            String senderName = accountDetail.getReqCodeSenderRealname();
            if (!accountDetail.getInviterFriendLevel().equals(""))
                senderName = accountDetail.getInviterFriendLevel() + " - " + senderName;
            txt_recommender.setText(senderName);
        }
        else {
            String senderName = accountDetail.getReqCodeSenderEnterName();
            if (!accountDetail.getInviterFriendLevel().equals(""))
                senderName = accountDetail.getInviterFriendLevel() + " - " + senderName;
            txt_recommender.setText(senderName);
        }

        Picasso.with(getActivity())
                .load(Constants.FILE_ADDR + accountDetail.getLogo())
                .placeholder(R.drawable.no_image)
                .into(img_avatar);

        mTabView1.setData(accountDetail.getEstimates());
        mTabView2.setData(accountDetail.getProducts());
        mTabView3.setData(accountDetail.getItems());
        mTabView4.setData(accountDetail.getServices());

        onChangedViewSize();

        if (accountDetail.getInterested() == 1)
            btnFollow.setChecked(true);
        else
            btnFollow.setChecked(false);
    }
}
