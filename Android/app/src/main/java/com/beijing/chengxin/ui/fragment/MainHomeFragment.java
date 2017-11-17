package com.beijing.chengxin.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;

public class MainHomeFragment extends Fragment {

    public final String TAG = MainHomeFragment.class.getName();
    private View rootView;

    public final static int INDEX_FAMILIAR = 0;
    public final static int INDEX_ENTERPRISE = 1;
    public final static int INDEX_COMEDITY = 2;
    public final static int INDEX_ITEM = 3;
    public final static int INDEX_SERVE = 4;

    ToggleButton btnFamiliar, btnEnterprise, btnComedity, btnItem, btnServe, btnSortSet, btnConditionSet;

    FrameLayout layoutSetting;
    ViewPager mPager;
    PageAdapter mAdapter;
    HomeFragment frg0 ,frg1, frg2, frg3, frg4;
    public int currentFragmentIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_home, container, false);

        btnFamiliar = (ToggleButton)rootView.findViewById(R.id.btn_familiar);
        btnEnterprise = (ToggleButton)rootView.findViewById(R.id.btn_enterprise);
        btnComedity = (ToggleButton)rootView.findViewById(R.id.btn_comedity);
        btnItem = (ToggleButton)rootView.findViewById(R.id.btn_item);
        btnServe = (ToggleButton)rootView.findViewById(R.id.btn_serve);
        btnSortSet = (ToggleButton)rootView.findViewById(R.id.btn_sort_set);
        btnConditionSet = (ToggleButton)rootView.findViewById(R.id.btn_condition_set);

        btnFamiliar.setOnClickListener(mClickListener);
        btnEnterprise.setOnClickListener(mClickListener);
        btnComedity.setOnClickListener(mClickListener);
        btnItem.setOnClickListener(mClickListener);
        btnServe.setOnClickListener(mClickListener);
        btnSortSet.setOnClickListener(mClickListener);
        btnConditionSet.setOnClickListener(mClickListener);

        frg0 = new HomeFragment();
        frg1 = new HomeFragment();
        frg2 = new HomeFragment();
        frg3 = new HomeFragment();
        frg4 = new HomeFragment();

        frg0.setType(0);
        frg1.setType(1);
        frg2.setType(2);
        frg3.setType(3);
        frg4.setType(4);

        mAdapter = new PageAdapter(getActivity().getSupportFragmentManager());
        mPager = (ViewPager)rootView.findViewById(R.id.viewPager);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                selectTopTabButton(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        currentFragmentIndex = INDEX_FAMILIAR;

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class PageAdapter extends FragmentPagerAdapter {
        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return frg1;
                case 2:
                    return frg2;
                case 3:
                    return frg3;
                case 4:
                    return frg4;
                case 0:
                default:
                    return frg0;
            }
        }
    };

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_familiar:
                    selectTopTabButton(INDEX_FAMILIAR);
                    mPager.setCurrentItem(INDEX_FAMILIAR);
                    break;
                case R.id.btn_enterprise:
                    selectTopTabButton(INDEX_ENTERPRISE);
                    mPager.setCurrentItem(INDEX_ENTERPRISE);
                    break;
                case R.id.btn_comedity:
                    selectTopTabButton(INDEX_COMEDITY);
                    mPager.setCurrentItem(INDEX_COMEDITY);
                    break;
                case R.id.btn_item:
                    selectTopTabButton(INDEX_ITEM);
                    mPager.setCurrentItem(INDEX_ITEM);
                    break;
                case R.id.btn_serve:
                    selectTopTabButton(INDEX_SERVE);
                    mPager.setCurrentItem(INDEX_SERVE);
                    break;
                case R.id.btn_sort_set:
                    btnSortSet.setChecked(false);
                    break;
                case R.id.btn_condition_set:
                    btnConditionSet.setChecked(false);
                    break;
            }
        }
    };

    private void resetTopButton() {
        btnFamiliar.setChecked(false);
        btnEnterprise.setChecked(false);
        btnComedity.setChecked(false);
        btnItem.setChecked(false);
        btnServe.setChecked(false);
    }

    private void selectTopTabButton(int position) {
        resetTopButton();
        switch (position) {
            case INDEX_FAMILIAR:
                btnFamiliar.setChecked(true);
                break;
            case INDEX_ENTERPRISE:
                btnEnterprise.setChecked(true);
                break;
            case INDEX_COMEDITY:
                btnComedity.setChecked(true);
                break;
            case INDEX_ITEM:
                btnItem.setChecked(true);
                break;
            case INDEX_SERVE:
                btnServe.setChecked(true);
                break;
        }
        currentFragmentIndex = position;
    }
}
