package com.beijing.chengxin.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.NoticeCountModel;
import com.beijing.chengxin.network.model.UserDetailModel;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.MainEvalFragment;
import com.beijing.chengxin.ui.fragment.MainFollowFragment;
import com.beijing.chengxin.ui.fragment.MainHomeFragment;
import com.beijing.chengxin.ui.fragment.MainHotFragment;
import com.beijing.chengxin.ui.fragment.MainMeFragment;
import com.beijing.chengxin.ui.widget.Utils;

import static com.beijing.chengxin.config.Constants.ERROR_DUPLICATE;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class MainActivity extends BaseFragmentActivity {

    private static String TAG = MainActivity.class.getName();

    public static MainActivity mainActivity;
    public AppConfig appConfig;

    private ToggleButton btnTabHome, btnTabHot, btnTabEval, btnTabFollow, btnTabMe;

    private TextView txtTitle, txtMessageCount;
    private TextView txt_search;

    // Condition Layout part
    public LinearLayout layoutCondition;
    public LinearLayout layoutOut;
    public FrameLayout layoutConditionBody;

    //    fragment index in main screen
    public final static int FRAGMENT_HOME = 0;
    public final static int FRAGMENT_HOT = 1;
    public final static int FRAGMENT_EVAL = 2;
    public final static int FRAGMENT_FOLLOW = 3;
    public final static int FRAGMENT_ME = 4;
    private final static long DURATION_MILI_SEC = 30000;

    public MainHomeFragment homeFragment;
    public MainHotFragment hotFragment;
    public MainEvalFragment evalFragment;
    public MainFollowFragment followFragment;
    public MainMeFragment meFragment;

    public int currentFragmentIndex;

    private final Handler handler = new Handler();

    MyAdapter mAdapter;
    String keyword = "";
//    ViewPager mPager;
    SyncInfo info;
    boolean shouldLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTabHome = (ToggleButton) findViewById(R.id.btnTabHome);
        btnTabHot = (ToggleButton) findViewById(R.id.btnTabHot);
        btnTabEval = (ToggleButton) findViewById(R.id.btnTabEval);
        btnTabFollow = (ToggleButton) findViewById(R.id.btnTabFollow);
        btnTabMe = (ToggleButton) findViewById(R.id.btnTabMe);

        txt_search = (TextView) findViewById(R.id.txt_search);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtMessageCount = (TextView) findViewById(R.id.txt_message_count);

        layoutCondition = (LinearLayout) findViewById(R.id.layout_condition);
        layoutOut = (LinearLayout) findViewById(R.id.layout_out);
        layoutConditionBody = (FrameLayout) findViewById(R.id.layout_condition_body);
        layoutOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hideConditionView(homeFragment.currentFragmentIndex))
                    return;

                if (evalFragment.mHangyeListView != null) {
                    if (evalFragment.mHangyeListView.getVisibility() == View.VISIBLE) {
                        evalFragment.mHangyeListView.setVisibility(View.GONE);
                        layoutCondition.setVisibility(View.GONE);
                        return;
                    }
                }
            }
        });

        homeFragment = new MainHomeFragment();
//        hotFragment = new MainHotFragment();
//        evalFragment = new MainEvalFragment();
//        followFragment = new MainFollowFragment();
//        meFragment = new MainMeFragment();

//        mAdapter = new MyAdapter(getSupportFragmentManager());
//        mPager = (ViewPager)findViewById(R.id.pager);
//        mPager.setAdapter(mAdapter);
//        mPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//            @Override
//            public void onPageSelected(int position) {
//                selectTabButton(position);
//            }
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });

        mainActivity = this;
        appConfig = new AppConfig(this);

        // show homeFragment at first;.
        currentFragmentIndex = FRAGMENT_HOME;
        showFragment(homeFragment, false, false);

        info = new SyncInfo(this);
        new NoticeCountAsync().execute();
        getNotificationCount();
    }

    public void loadNoticeCount() {
        new NoticeCountAsync().execute();
    }

    private void getNotificationCount() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (shouldLoad)
                    loadNoticeCount();
                getNotificationCount();
            }
        }, DURATION_MILI_SEC);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shouldLoad = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        shouldLoad = false;
    }

    public void showKeyword(String keyword) {
        if (keyword.equals("")) {
            if (currentFragmentIndex == FRAGMENT_HOME)
                txt_search.setText(getString(R.string.search_query_home));
            else if (currentFragmentIndex == FRAGMENT_EVAL)
                txt_search.setText(getString(R.string.search_query_eval));
        } else
            txt_search.setText(keyword);
    }

    public void setKeyword(String keyword, int subindex) {
        this.keyword = keyword;
        if (keyword.equals("")) {
            if (currentFragmentIndex == FRAGMENT_HOME)
                txt_search.setText(getString(R.string.search_query_home));
            else
                txt_search.setText(getString(R.string.search_query_eval));
        } else
            txt_search.setText(keyword);
        if (currentFragmentIndex == FRAGMENT_HOME) {
            switch (subindex) {
                case Constants.SEARCH_HOME_FAMILIAR:
                    homeFragment.homeFamiliarView.setKeyword(keyword, "");
                    if (homeFragment.currentFragmentIndex != Constants.INDEX_FAMILIAR)
                        homeFragment.setCurrentPage(Constants.INDEX_FAMILIAR);
                    break;
                case Constants.SEARCH_HOME_ENTERPRISE:
                    homeFragment.homeEnterpriseView.setKeyword(keyword);
                    if (homeFragment.currentFragmentIndex != Constants.INDEX_ENTERPRISE)
                        homeFragment.setCurrentPage(Constants.INDEX_ENTERPRISE);
                    break;
                case Constants.SEARCH_HOME_COMEDITY:
                    homeFragment.homeComedityView.setKeyword(keyword);
                    if (homeFragment.currentFragmentIndex != Constants.INDEX_COMEDITY)
                        homeFragment.setCurrentPage(Constants.INDEX_COMEDITY);
                    break;
                case Constants.SEARCH_HOME_ITEM:
                    homeFragment.homeItemView.setKeyword(keyword);
                    if (homeFragment.currentFragmentIndex != Constants.INDEX_ITEM)
                        homeFragment.setCurrentPage(Constants.INDEX_ITEM);
                    break;
                case Constants.SEARCH_HOME_SERVE:
                    homeFragment.homeServeView.setKeyword(keyword);
                    if (homeFragment.currentFragmentIndex != Constants.INDEX_SERVE)
                        homeFragment.setCurrentPage(Constants.INDEX_SERVE);
                    break;
                case Constants.SEARCH_HOME_CODE:
                    homeFragment.homeFamiliarView.setKeyword("",keyword);
                    if (homeFragment.currentFragmentIndex != Constants.INDEX_FAMILIAR)
                        homeFragment.setCurrentPage(Constants.INDEX_FAMILIAR);
                    break;
            }
        } else  if (currentFragmentIndex == FRAGMENT_EVAL) {
            evalFragment.setKeyword(keyword);
        }
    }

    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case FRAGMENT_HOT:
                    return hotFragment;
                case FRAGMENT_EVAL:
                    return evalFragment;
                case FRAGMENT_FOLLOW:
                    return followFragment;
                case FRAGMENT_ME:
                    return meFragment;
                case FRAGMENT_HOME:
                default:
                    return homeFragment;
            }
        }
    };

    public void onButtonClick(View v) {
        boolean isfromRight = true;
        switch (v.getId()) {
            case R.id.btnTabHome:
                selectTabButton(FRAGMENT_HOME);
//                mPager.setCurrentItem(FRAGMENT_HOME, true);
                break;
            case R.id.btnTabHot:
                selectTabButton(FRAGMENT_HOT);
//                mPager.setCurrentItem(FRAGMENT_HOT, true);
                break;
            case R.id.btnTabEval:
                selectTabButton(FRAGMENT_EVAL);
//                mPager.setCurrentItem(FRAGMENT_EVAL, true);
                break;
            case R.id.btnTabFollow:
                selectTabButton(FRAGMENT_FOLLOW);
//                mPager.setCurrentItem(FRAGMENT_FOLLOW, true);
                break;
            case R.id.btnTabMe:
                selectTabButton(FRAGMENT_ME);
//                mPager.setCurrentItem(FRAGMENT_ME, true);
                break;
            case R.id.btn_message:
                Intent intent = new Intent(this, SystemNewsActivity.class);
                startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.txt_search:
                intent = new Intent(this, SearchActivity.class);
                intent.putExtra(Constants.SEARCH_KEYWORD, currentFragmentIndex);
                startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULT_CODE_LOGIN_DUPLICATE)
            ChengxinApplication.finishAndLoginActivityFromDuplicate(this);
    }

    private void resetTabButton() {
        btnTabHome.setChecked(false);
        btnTabHot.setChecked(false);
        btnTabEval.setChecked(false);
        btnTabFollow.setChecked(false);
        btnTabMe.setChecked(false);
    }

    private void selectTabButton(int position) {
        resetTabButton();
        switch (position) {
            case FRAGMENT_HOME:
                btnTabHome.setChecked(true);
                if (currentFragmentIndex != FRAGMENT_HOME) {
                    txt_search.setVisibility(View.VISIBLE);
                    txt_search.setText(R.string.search_query_home);
                    txtTitle.setVisibility(View.GONE);

                    showFragment(homeFragment, false, true, false);
//                    goHome();
                }
                break;
            case FRAGMENT_HOT:
                btnTabHot.setChecked(true);
                if (currentFragmentIndex != FRAGMENT_HOT) {
                    txt_search.setVisibility(View.GONE);
                    txtTitle.setVisibility(View.VISIBLE);
                    txtTitle.setText(getText(R.string.tab_hot));

                    if (hotFragment == null)
                        hotFragment = new MainHotFragment();
                    boolean isFromRight = (currentFragmentIndex < FRAGMENT_HOT);
//                    if (currentFragmentIndex == FRAGMENT_HOME)
//                        showFragment(hotFragment, true, true, isFromRight);
//                    else
                        showFragment(hotFragment, false, true, isFromRight);
                }
                break;
            case FRAGMENT_EVAL:
                btnTabEval.setChecked(true);
                if (currentFragmentIndex != FRAGMENT_EVAL) {
                    txt_search.setVisibility(View.VISIBLE);
                    txt_search.setText(R.string.search_query_eval);
                    txtTitle.setVisibility(View.GONE);

                    if (evalFragment == null)
                        evalFragment = new MainEvalFragment();
                    boolean isFromRight = (currentFragmentIndex < FRAGMENT_EVAL);

//                    if (currentFragmentIndex == FRAGMENT_HOME)
//                        showFragment(evalFragment, true, true, isFromRight);
//                    else
                        showFragment(evalFragment, false, true, isFromRight);
                }
                break;
            case FRAGMENT_FOLLOW:
                btnTabFollow.setChecked(true);
                if (currentFragmentIndex != FRAGMENT_FOLLOW) {
                    txt_search.setVisibility(View.GONE);
                    txtTitle.setVisibility(View.VISIBLE);
                    txtTitle.setText(getText(R.string.tab_follow));

                    if (followFragment == null)
                        followFragment = new MainFollowFragment();
                    boolean isFromRight = (currentFragmentIndex < FRAGMENT_FOLLOW);
//                    if (currentFragmentIndex == FRAGMENT_HOME)
//                        showFragment(followFragment, true, true, isFromRight);
//                    else
                        showFragment(followFragment, false, true, isFromRight);
                }
                break;
            case FRAGMENT_ME:
                btnTabMe.setChecked(true);
                if (currentFragmentIndex != FRAGMENT_ME) {
                    txt_search.setVisibility(View.GONE);
                    txtTitle.setVisibility(View.VISIBLE);
                    txtTitle.setText(getText(R.string.tab_me));

                    if (meFragment == null)
                        meFragment = new MainMeFragment();
//                    if (currentFragmentIndex == FRAGMENT_HOME)
//                        showFragment(meFragment, true, true, true);
//                    else
                        showFragment(meFragment, false, true, true);
                }
                break;
        }
        currentFragmentIndex = position;
    }

    @Override
    public void onBackPressed() {
        if (layoutCondition.getVisibility() == View.VISIBLE) {
            if (hideConditionView(homeFragment.currentFragmentIndex))
                return;

            if (evalFragment.mHangyeListView != null) {
                if (evalFragment.mHangyeListView.getVisibility() == View.VISIBLE) {
                    evalFragment.mHangyeListView.setVisibility(View.GONE);
                    layoutCondition.setVisibility(View.GONE);
                    return;
                }
            }
            //layoutCondition.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public boolean hideConditionView(int index) {
        switch (index) {
            case Constants.INDEX_FAMILIAR:
                if (homeFragment.cdtFamiliarView != null) {
                    if (homeFragment.cdtFamiliarView.mCityListView != null && homeFragment.cdtFamiliarView.mCityListView.getVisibility() == View.VISIBLE) {
                        homeFragment.cdtFamiliarView.mCityListView.setVisibility(View.GONE);
                        return true;
                    }
                    if (homeFragment.cdtFamiliarView.mHangyeListView != null && homeFragment.cdtFamiliarView.mHangyeListView.getVisibility() == View.VISIBLE) {
                        homeFragment.cdtFamiliarView.mHangyeListView.setVisibility(View.GONE);
                        return true;
                    }
                    if (homeFragment.cdtFamiliarView.getVisibility() == View.VISIBLE) {
                        homeFragment.cdtFamiliarView.setVisibility(View.GONE);
                        layoutCondition.setVisibility(View.GONE);
                        return true;
                    }
                }
                break;
            case Constants.INDEX_ENTERPRISE:
                if (homeFragment.cdtEnterpriseView != null) {
                    if (homeFragment.cdtEnterpriseView.mCityListView != null && homeFragment.cdtEnterpriseView.mCityListView.getVisibility() == View.VISIBLE) {
                        homeFragment.cdtEnterpriseView.mCityListView.setVisibility(View.GONE);
                        return true;
                    }
                    if (homeFragment.cdtEnterpriseView.mHangyeListView != null && homeFragment.cdtEnterpriseView.mHangyeListView.getVisibility() == View.VISIBLE) {
                        homeFragment.cdtEnterpriseView.mHangyeListView.setVisibility(View.GONE);
                        return true;
                    }
                    if (homeFragment.cdtEnterpriseView.getVisibility() == View.VISIBLE) {
                        homeFragment.cdtEnterpriseView.setVisibility(View.GONE);
                        layoutCondition.setVisibility(View.GONE);
                        return true;
                    }
                }
                break;
            case Constants.INDEX_COMEDITY:
                if (homeFragment.cdtComedityView != null) {
                    if (homeFragment.cdtComedityView.getVisibility() == View.VISIBLE) {
                        homeFragment.cdtComedityView.setVisibility(View.GONE);
                        layoutCondition.setVisibility(View.GONE);
                        return true;
                    }
                }
                break;
            case Constants.INDEX_ITEM:
                if (homeFragment.cdtItemView != null) {
                    if (homeFragment.cdtItemView.getVisibility() == View.VISIBLE) {
                        homeFragment.cdtItemView.setVisibility(View.GONE);
                        layoutCondition.setVisibility(View.GONE);
                        return true;
                    }
                }
                break;
            case Constants.INDEX_SERVE:
                if (homeFragment.cdtServeView != null) {
                    if (homeFragment.cdtServeView.getVisibility() == View.VISIBLE) {
                        homeFragment.cdtServeView.setVisibility(View.GONE);
                        layoutCondition.setVisibility(View.GONE);
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    class NoticeCountAsync extends AsyncTask<String, String, NoticeCountModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected NoticeCountModel doInBackground(String... strs) {
            return info.syncNoticeCount();
        }
        @Override
        protected void onPostExecute(NoticeCountModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    AppConfig.getInstance().notice = result;
                    if (result.getTotalCnt() > 0) {
                        txtMessageCount.setText(String.valueOf(result.getTotalCnt()));
                        txtMessageCount.setVisibility(View.VISIBLE);
                    } else {
                        txtMessageCount.setVisibility(View.INVISIBLE);
                    }
                } else if (result.getRetCode() == ERROR_DUPLICATE) {
                    ChengxinApplication.finishAndLoginActivityFromDuplicate(MainActivity.this);
                }
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}
