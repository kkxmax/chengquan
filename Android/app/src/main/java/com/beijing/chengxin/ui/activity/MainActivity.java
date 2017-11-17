package com.beijing.chengxin.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.MainEvalFragment;
import com.beijing.chengxin.ui.fragment.MainFollowFragment;
import com.beijing.chengxin.ui.fragment.MainHomeFragment;
import com.beijing.chengxin.ui.fragment.MainHotFragment;
import com.beijing.chengxin.ui.fragment.MainMeFragment;

public class MainActivity extends FragmentActivity {

    private static String TAG = MainActivity.class.getName();

    public static MainActivity mainActivity;

    private ToggleButton btnTabHome, btnTabHot, btnTabEval, btnTabFollow, btnTabMe;
    private TextView txtTitle, txtMessageCount;
    private TextView txt_search;

    //    fragment index in main screen
    public final static int FRAGMENT_HOME = 0;
    public final static int FRAGMENT_HOT = 1;
    public final static int FRAGMENT_EVAL = 2;
    public final static int FRAGMENT_FOLLOW = 3;
    public final static int FRAGMENT_ME = 4;

    private MainHomeFragment homeFragment;
    private MainHotFragment hotFragment;
    private MainEvalFragment evalFragment;
    private MainFollowFragment followFragment;
    private MainMeFragment meFragment;

    public int currentFragmentIndex;

    MyAdapter mAdapter;
    ViewPager mPager;

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

        homeFragment = new MainHomeFragment();
        hotFragment = new MainHotFragment();
        evalFragment = new MainEvalFragment();
        followFragment = new MainFollowFragment();
        meFragment = new MainMeFragment();

        // show homeFragment at first;.
        currentFragmentIndex = FRAGMENT_HOME;

        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                selectTabButton(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mainActivity = this;
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
        switch (v.getId()) {
            case R.id.btnTabHome:
                selectTabButton(FRAGMENT_HOME);
                mPager.setCurrentItem(FRAGMENT_HOME, true);
                break;
            case R.id.btnTabHot:
                selectTabButton(FRAGMENT_HOT);
                mPager.setCurrentItem(FRAGMENT_HOT, true);
                break;
            case R.id.btnTabEval:
                selectTabButton(FRAGMENT_EVAL);
                mPager.setCurrentItem(FRAGMENT_EVAL, true);
                break;
            case R.id.btnTabFollow:
                selectTabButton(FRAGMENT_FOLLOW);
                mPager.setCurrentItem(FRAGMENT_FOLLOW, true);
                break;
            case R.id.btnTabMe:
                selectTabButton(FRAGMENT_ME);
                mPager.setCurrentItem(FRAGMENT_ME, true);
                break;
            case R.id.btn_message:
                Intent intent = new Intent(this, SystemNewsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.fade_out);
                break;
            case R.id.txt_search:
                intent = new Intent(this, SearchActivity.class);
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
                }
                break;
            case FRAGMENT_HOT:
                btnTabHot.setChecked(true);
                if (currentFragmentIndex != FRAGMENT_HOT) {
                    txt_search.setVisibility(View.GONE);
                    txtTitle.setVisibility(View.VISIBLE);
                    txtTitle.setText(getText(R.string.tab_hot));
                }
                break;
            case FRAGMENT_EVAL:
                btnTabEval.setChecked(true);
                if (currentFragmentIndex != FRAGMENT_EVAL) {
                    txt_search.setVisibility(View.VISIBLE);
                    txt_search.setHint(R.string.search_query_eval);
                    txtTitle.setVisibility(View.GONE);
                }
                break;
            case FRAGMENT_FOLLOW:
                btnTabFollow.setChecked(true);
                if (currentFragmentIndex != FRAGMENT_FOLLOW) {
                    txt_search.setVisibility(View.GONE);
                    txtTitle.setVisibility(View.VISIBLE);
                    txtTitle.setText(getText(R.string.tab_follow));
                }
                break;
            case FRAGMENT_ME:
                btnTabMe.setChecked(true);
                if (currentFragmentIndex != FRAGMENT_ME) {
                    txt_search.setVisibility(View.GONE);
                    txtTitle.setVisibility(View.VISIBLE);
                    txtTitle.setText(getText(R.string.tab_me));
                }
                break;
        }
        currentFragmentIndex = position;
    }
}
