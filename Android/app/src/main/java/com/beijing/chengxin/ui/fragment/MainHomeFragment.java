package com.beijing.chengxin.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.CarouselImageModel;
import com.beijing.chengxin.network.model.CarouselListModel;
import com.beijing.chengxin.network.model.ProvinceListModel;
import com.beijing.chengxin.network.model.XyleixingListModel;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.activity.VideoPlayActivity;
import com.beijing.chengxin.ui.view.ConditionComedityView;
import com.beijing.chengxin.ui.view.ConditionEnterpriseView;
import com.beijing.chengxin.ui.view.ConditionFamiliarView;
import com.beijing.chengxin.ui.view.ConditionItemServeView;
import com.beijing.chengxin.ui.widget.AutoScrollViewPager;
import com.beijing.chengxin.ui.widget.PageIndicator;
import com.beijing.chengxin.ui.widget.SimpleSortSpinner;
import com.beijing.chengxin.ui.widget.UrlImagePagerAdapter;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.TEST_STATUS_PASSED;

public class MainHomeFragment extends Fragment {

    public final String TAG = MainHomeFragment.class.getName();
    private View rootView;

//    public static MainHomeFragment instance;

    public ConditionFamiliarView cdtFamiliarView;
    public ConditionEnterpriseView cdtEnterpriseView;
    public ConditionComedityView cdtComedityView;
    public ConditionItemServeView cdtItemView;
    public ConditionItemServeView cdtServeView;

    public HomeFamiliarFragment frg0;
    public HomeEnterpriseFragment frg1;
    public HomeComedityFragment frg2;
    public HomeItemFragment frg3;
    public HomeServeFragment frg4;

    ToggleButton btnFamiliar, btnEnterprise, btnComedity, btnItem, btnServe;
    SimpleSortSpinner btnSortSet;
    TextView btnConditionSet;

    FrameLayout layoutSetting;
    ViewPager mPager;
    PageAdapter mAdapter;
    public int currentFragmentIndex;

    AutoScrollViewPager recommendViewPager;
    PageIndicator pageIndicator;
    UrlImagePagerAdapter recommendImageAdapter;
    List<String> listRecommendImageUrl;

    List<CarouselImageModel> listCarouselList;

    SyncInfo info;

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
        btnSortSet = (SimpleSortSpinner) rootView.findViewById(R.id.btn_sort_set);
        btnConditionSet = (TextView)rootView.findViewById(R.id.btn_condition_set);

        btnFamiliar.setOnClickListener(mClickListener);
        btnEnterprise.setOnClickListener(mClickListener);
        btnComedity.setOnClickListener(mClickListener);
        btnItem.setOnClickListener(mClickListener);
        btnServe.setOnClickListener(mClickListener);
        btnConditionSet.setOnClickListener(mClickListener);

        frg0 = new HomeFamiliarFragment();
        frg1 =new HomeEnterpriseFragment();
        frg2 = new HomeComedityFragment();
        frg3 = new HomeItemFragment();
        frg4 = new HomeServeFragment();

        mAdapter = new PageAdapter(getActivity().getSupportFragmentManager());
        mPager = (ViewPager)rootView.findViewById(R.id.viewPager);
        mPager.setOffscreenPageLimit(0);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                selectTopTabButton(position);
                String keyword = "";
                if (position == Constants.INDEX_FAMILIAR)
                    keyword = (frg0.keyword.equals("")) ? frg0.keywordCode : frg0.keyword;
                else if (position == Constants.INDEX_ENTERPRISE)
                    keyword = frg1.keyword;
                else if (position == Constants.INDEX_COMEDITY)
                    keyword = frg2.keyword;
                else if (position == Constants.INDEX_ITEM)
                    keyword = frg3.keyword;
                else if (position == Constants.INDEX_SERVE)
                    keyword = frg4.keyword;

                MainActivity.mainActivity.showKeyword(keyword);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        btnSortSet.setOnItemSelectListener(new SimpleSortSpinner.OnItemSelectListener() {
            @Override
            public void onItemSelected(int index) {
                switch (currentFragmentIndex) {
                    case Constants.INDEX_FAMILIAR:
                        frg0.mOrder = index;
                        frg0.reloadData();
                        break;
                    case Constants.INDEX_ENTERPRISE:
                        frg1.mOrder = index;
                        frg1.reloadData();
                        break;
                    case Constants.INDEX_COMEDITY:
                        frg2.mOrder = index;
                        frg2.reloadData();
                        break;
                    case Constants.INDEX_ITEM:
                        frg3.mOrder = index;
                        frg3.reloadData();
                        break;
                    case Constants.INDEX_SERVE:
                        frg4.mOrder = index;
                        frg4.reloadData();
                        break;
                }
//                Intent intent = new Intent(Constants.NOTIFY_SEARCH_COND_CHANGED);
//                intent.putExtra(Constants.SEARCH_ORDER, index);
//                getActivity().getBaseContext().sendBroadcast(intent);
            }
        });

        recommendViewPager = (AutoScrollViewPager) rootView.findViewById(R.id.autoScrollViewPager);

        info = new SyncInfo(getActivity());

        new CarouselAsync().execute();
        if (AppConfig.getInstance().cityList.size() == 0)
            new CityListAsync().execute();
        if (AppConfig.getInstance().xyleixingList.size() == 0)
            new XyleixingListAsync().execute();

//        instance = this;

        return rootView;
    }

    UrlImagePagerAdapter.OnItemClickListener imageClickListener = new UrlImagePagerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                CarouselImageModel item = listCarouselList.get(position);
                if (item.getKind() == Constants.MEDIA_TYPE_VIDEO) {
                    Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                    intent.putExtra("path", item.getVideoUrl());
                    intent.putExtra("title", item.getVideoTitle());
                    intent.putExtra("comment", item.getVideoComment());
                    startActivity(intent);
                }
            }
        }
    };

    public void setCurrentPage(int index) {
        selectTopTabButton(index);
        mPager.setCurrentItem(index);
    }

    @Override
    public void onResume() {
        super.onResume();
//        mPager.setCurrentItem(currentFragmentIndex);
        recommendViewPager.startAutoScroll();
    }

    @Override
    public void onPause() {
//        int tempPosition = (currentFragmentIndex + 3) % 5;
//        mPager.setCurrentItem(tempPosition);
        super.onPause();
        recommendViewPager.stopAutoScroll();
    }

    public class PageAdapter extends FragmentStatePagerAdapter {
        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    frg1 = (frg1 == null) ? new HomeEnterpriseFragment() : frg1;
                    return frg1;
                case 2:
                    frg2 = (frg2 == null) ? new HomeComedityFragment() : frg2;
                    return frg2;
                case 3:
                    frg3 = (frg3 == null) ? new HomeItemFragment() : frg3;
                    return frg3;
                case 4:
                    frg4 = (frg4 == null) ? new HomeServeFragment() : frg4;
                    return frg4;
                case 0:
                default:
                    frg0 = (frg0 == null) ? new HomeFamiliarFragment() : frg0;
                    return frg0;
            }
        }
    };

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_familiar:
                    setCurrentPage(Constants.INDEX_FAMILIAR);
                    break;
                case R.id.btn_enterprise:
                    setCurrentPage(Constants.INDEX_ENTERPRISE);
                    break;
                case R.id.btn_comedity:
                    setCurrentPage(Constants.INDEX_COMEDITY);
                    break;
                case R.id.btn_item:
                    setCurrentPage(Constants.INDEX_ITEM);
                    break;
                case R.id.btn_serve:
                    setCurrentPage(Constants.INDEX_SERVE);
                    break;
                case R.id.btn_condition_set:
                    onClickedConditionSet();
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
        int sortOrder = 0;
        switch (position) {
            case Constants.INDEX_FAMILIAR:
                btnFamiliar.setChecked(true);
                sortOrder = (frg0 != null) ? frg0.mOrder : 0;
                break;
            case Constants.INDEX_ENTERPRISE:
                btnEnterprise.setChecked(true);
                sortOrder = (frg1 != null) ? frg1.mOrder : 0;
                break;
            case Constants.INDEX_COMEDITY:
                btnComedity.setChecked(true);
                sortOrder = (frg2 != null) ? frg2.mOrder : 0;
                break;
            case Constants.INDEX_ITEM:
                btnItem.setChecked(true);
                sortOrder = (frg3 != null) ? frg3.mOrder : 0;
                break;
            case Constants.INDEX_SERVE:
                btnServe.setChecked(true);
                sortOrder = (frg4 != null) ? frg4.mOrder : 0;
                break;
        }
        btnSortSet.setSelection(sortOrder);
        currentFragmentIndex = position;
    }

    private void onClickedConditionSet() {
        int visibility = MainActivity.mainActivity.layoutCondition.getVisibility();
        if (visibility == View.GONE) {
            MainActivity.mainActivity.layoutCondition.setVisibility(View.VISIBLE);
            MainActivity.mainActivity.layoutConditionBody.removeAllViews();

            View cdtView;
            switch (currentFragmentIndex) {
                case Constants.INDEX_ENTERPRISE:
                    if (cdtEnterpriseView == null) {
                        cdtEnterpriseView = new ConditionEnterpriseView(getContext());
                        cdtEnterpriseView.setOnConditionClickListener(frg1.mConditionClickListener);
                    }
                    cdtEnterpriseView.setData(frg1.mCityName, frg1.enterKind, frg1.xyList);
                    MainActivity.mainActivity.layoutConditionBody.addView(cdtEnterpriseView);
                    cdtEnterpriseView.setVisibility(View.VISIBLE);
                    cdtView = cdtEnterpriseView;
                    break;
                case Constants.INDEX_COMEDITY:
                    if (cdtComedityView== null) {
                        cdtComedityView = new ConditionComedityView(getContext());
                        cdtComedityView.setOnConditionClickListener(frg2.mConditionClickListener);
                    }
                    cdtComedityView.setData(frg2.mCityName, 0, frg2.xyList);
                    MainActivity.mainActivity.layoutConditionBody.addView(cdtComedityView);
                    cdtComedityView.setVisibility(View.VISIBLE);
                    cdtView = cdtComedityView;
                    break;
                case Constants.INDEX_ITEM:
                    if (cdtItemView == null) {
                        cdtItemView = new ConditionItemServeView(getContext(), 0);
                        cdtItemView.setOnConditionClickListener(frg3.mConditionClickListener);
                    }
                    cdtItemView.setData(frg3.mCityName, frg3.aKind, frg3.xyList);
                    MainActivity.mainActivity.layoutConditionBody.addView(cdtItemView);
                    cdtItemView.setVisibility(View.VISIBLE);
                    cdtView = cdtItemView;
                    break;
                case Constants.INDEX_SERVE:
                    if (cdtServeView == null) {
                        cdtServeView = new ConditionItemServeView(getContext(), 1);
                        cdtServeView.setOnConditionClickListener(frg4.mConditionClickListener);
                    }
                    cdtServeView.setData(frg4.mCityName, frg4.aKind, frg4.xyList);
                    MainActivity.mainActivity.layoutConditionBody.addView(cdtServeView);
                    cdtServeView.setVisibility(View.VISIBLE);
                    cdtView = cdtServeView;
                    break;
                case Constants.INDEX_FAMILIAR:
                default:
                    if (cdtFamiliarView == null) {
                        cdtFamiliarView = new ConditionFamiliarView(getContext());
                        cdtFamiliarView.setOnConditionClickListener(frg0.mConditionClickListener);
                    }
                    cdtFamiliarView.setData(frg0.mCityName, frg0.aKind, frg0.xyList);
                    MainActivity.mainActivity.layoutConditionBody.addView(cdtFamiliarView);
                    cdtFamiliarView.setVisibility(View.VISIBLE);
                    cdtView = cdtFamiliarView;
                    break;
            }
            CommonUtils.animationShowFromRight(cdtView);
        } else {
            MainActivity.mainActivity.layoutCondition.setVisibility(View.GONE);
        }
    }

    class CarouselAsync extends AsyncTask<String, String, CarouselListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected CarouselListModel doInBackground(String... strs) {
            return info.syncCarousel();
        }
        @Override
        protected void onPostExecute(CarouselListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    listCarouselList = result.getImgList();
                    listRecommendImageUrl = new ArrayList<String>();
                    for (int i = 0; i < listCarouselList.size(); i++) {
                        listRecommendImageUrl.add(Constants.FILE_ADDR + listCarouselList.get(i).getImgUrl());
                    }
                    recommendImageAdapter = new UrlImagePagerAdapter(listRecommendImageUrl).setInfiniteLoop(true);
                    recommendImageAdapter.setOnItemClickListener(imageClickListener);
                    recommendViewPager.setAdapter(recommendImageAdapter);
                    recommendViewPager.setInterval(5000);
                    pageIndicator = (PageIndicator) rootView.findViewById(R.id.pageIndicator);
                    pageIndicator.setViewPager(recommendViewPager);

                    recommendViewPager.startAutoScroll();
                }
            }
            Utils.disappearProgressDialog();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Utils.disappearProgressDialog();
        }
    }

    class CityListAsync extends AsyncTask<String, String, ProvinceListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected ProvinceListModel doInBackground(String... strs) {
            return info.syncCityList();
        }
        @Override
        protected void onPostExecute(ProvinceListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    AppConfig.getInstance().cityList.clear();
                    AppConfig.getInstance().cityList.addAll(result.getList());
                }
            }
            //Utils.disappearProgressDialog();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Utils.disappearProgressDialog();
        }
    }

    class XyleixingListAsync extends AsyncTask<String, String, XyleixingListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected XyleixingListModel doInBackground(String... strs) {
            return info.syncXyleixingList("");
        }
        @Override
        protected void onPostExecute(XyleixingListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    AppConfig.getInstance().xyleixingList.clear();
                    AppConfig.getInstance().xyleixingList.addAll(result.getList());
                }
            }
            //Utils.disappearProgressDialog();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Utils.disappearProgressDialog();
        }
    }
}
