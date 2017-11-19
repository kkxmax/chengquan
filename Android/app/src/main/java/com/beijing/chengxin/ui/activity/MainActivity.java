package com.beijing.chengxin.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.MainEvalFragment;
import com.beijing.chengxin.ui.fragment.MainFollowFragment;
import com.beijing.chengxin.ui.fragment.MainHomeFragment;
import com.beijing.chengxin.ui.fragment.MainHotFragment;
import com.beijing.chengxin.ui.fragment.MainMeFragment;

public class MainActivity extends BaseFragmentActivity {

    private static String TAG = MainActivity.class.getName();

    public static MainActivity mainActivity;
    public AppConfig appConfig;

    private ToggleButton btnTabHome, btnTabHot, btnTabEval, btnTabFollow, btnTabMe;

    private TextView txtTitle, txtMessageCount;
    private TextView txt_search;

    // Condition Layout part
    public LinearLayout layoutCondition;
    public FrameLayout layoutConditionBody;

    //    fragment index in main screen
    public final static int FRAGMENT_HOME = 0;
    public final static int FRAGMENT_HOT = 1;
    public final static int FRAGMENT_EVAL = 2;
    public final static int FRAGMENT_FOLLOW = 3;
    public final static int FRAGMENT_ME = 4;

    public MainHomeFragment homeFragment;
    public MainHotFragment hotFragment;
    public MainEvalFragment evalFragment;
    public MainFollowFragment followFragment;
    public MainMeFragment meFragment;

    public int currentFragmentIndex;

    MyAdapter mAdapter;
    String keyword;
//    ViewPager mPager;

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
        layoutConditionBody = (FrameLayout) findViewById(R.id.layout_condition_body);

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
    }

    public void showKeyword(String keyword) {
        if (keyword.equals("")) {
            if (currentFragmentIndex == FRAGMENT_HOME)
                txt_search.setText(getString(R.string.search_query_home));
            else
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
                    homeFragment.frg0.setKeyword(keyword, "");
                    if (homeFragment.currentFragmentIndex != Constants.INDEX_FAMILIAR)
                        homeFragment.setCurrentPage(Constants.INDEX_FAMILIAR);
                    break;
                case Constants.SEARCH_HOME_ENTERPRISE:
                    homeFragment.frg1.setKeyword(keyword);
                    if (homeFragment.currentFragmentIndex != Constants.INDEX_ENTERPRISE)
                        homeFragment.setCurrentPage(Constants.INDEX_ENTERPRISE);
                    break;
                case Constants.SEARCH_HOME_COMEDITY:
                    homeFragment.frg2.setKeyword(keyword);
                    if (homeFragment.currentFragmentIndex != Constants.INDEX_COMEDITY)
                        homeFragment.setCurrentPage(Constants.INDEX_COMEDITY);
                    break;
                case Constants.SEARCH_HOME_ITEM:
                    homeFragment.frg3.setKeyword(keyword);
                    if (homeFragment.currentFragmentIndex != Constants.INDEX_ITEM)
                        homeFragment.setCurrentPage(Constants.INDEX_ITEM);
                    break;
                case Constants.SEARCH_HOME_SERVE:
                    homeFragment.frg4.setKeyword(keyword);
                    if (homeFragment.currentFragmentIndex != Constants.INDEX_SERVE)
                        homeFragment.setCurrentPage(Constants.INDEX_SERVE);
                    break;
                case Constants.SEARCH_HOME_CODE:
                    homeFragment.frg0.setKeyword("",keyword);
                    if (homeFragment.currentFragmentIndex != Constants.INDEX_FAMILIAR)
                        homeFragment.setCurrentPage(Constants.INDEX_FAMILIAR);
                    break;
            }
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
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out);
                break;
            case R.id.txt_search:
                intent = new Intent(this, SearchActivity.class);
                intent.putExtra(Constants.SEARCH_KEYWORD, currentFragmentIndex);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
                break;
        }
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
                    txt_search.setHint(R.string.search_query_home);
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
                    txt_search.setHint(R.string.search_query_eval);
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

            if (evalFragment.conditionEvalView != null) {
                if (evalFragment.conditionEvalView.getVisibility() == View.VISIBLE) {
                    evalFragment.conditionEvalView.setVisibility(View.GONE);
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

}
