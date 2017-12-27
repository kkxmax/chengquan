package com.beijing.chengxin.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
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
import com.beijing.chengxin.ui.activity.MakeComedityActivity;
import com.beijing.chengxin.ui.activity.MakeItemActivity;
import com.beijing.chengxin.ui.activity.MakeServeActivity;
import com.beijing.chengxin.ui.activity.VideoPlayActivity;
import com.beijing.chengxin.ui.adapter.HomeImageSliderAdapter;
import com.beijing.chengxin.ui.view.BaseView;
import com.beijing.chengxin.ui.view.ConditionComedityView;
import com.beijing.chengxin.ui.view.ConditionEnterpriseView;
import com.beijing.chengxin.ui.view.ConditionFamiliarView;
import com.beijing.chengxin.ui.view.ConditionItemServeView;
import com.beijing.chengxin.ui.view.HomeComedityView;
import com.beijing.chengxin.ui.view.HomeEnterpriseView;
import com.beijing.chengxin.ui.view.HomeFamiliarView;
import com.beijing.chengxin.ui.view.HomeItemView;
import com.beijing.chengxin.ui.view.HomeServeView;
import com.beijing.chengxin.ui.view.HotEvalListView;
import com.beijing.chengxin.ui.view.PartnerListView;
import com.beijing.chengxin.ui.widget.AutoScrollViewPager;
import com.beijing.chengxin.ui.widget.PageIndicator;
import com.beijing.chengxin.ui.widget.SimpleSortSpinner;
import com.beijing.chengxin.ui.widget.UrlImagePagerAdapter;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import handmark.pulltorefresh.library.PullToRefreshBase;
import handmark.pulltorefresh.library.PullToRefreshScrollView;

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

//    public HomeFamiliarFragment frg0;
//    public HomeEnterpriseFragment frg1;
//    public HomeComedityFragment frg2;
//    public HomeItemFragment frg3;
//    public HomeServeFragment frg4;

    public PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;
    FrameLayout mScrollBody;
    ImageButton mImgAdd;

    ToggleButton btnFamiliar, btnEnterprise, btnComedity, btnItem, btnServe;
    SimpleSortSpinner btnSortSet;
    TextView btnConditionSet;
    View viewBlankPart;

    public int currentFragmentIndex;

    AutoScrollViewPager recommendViewPager;
    PageIndicator pageIndicator;
    HomeImageSliderAdapter recommendImageAdapter;
    List<CarouselImageModel> listCarouselList;

    SyncInfo info;

    ArrayList<BaseView> mTabViews;
    public HomeFamiliarView homeFamiliarView;
    public HomeEnterpriseView homeEnterpriseView;
    public HomeComedityView homeComedityView;
    public HomeItemView homeItemView;
    public HomeServeView homeServeView;

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
        viewBlankPart = (View) rootView.findViewById(R.id.view_blank_part);

        btnFamiliar.setOnClickListener(mClickListener);
        btnEnterprise.setOnClickListener(mClickListener);
        btnComedity.setOnClickListener(mClickListener);
        btnItem.setOnClickListener(mClickListener);
        btnServe.setOnClickListener(mClickListener);
        btnConditionSet.setOnClickListener(mClickListener);

        mPullRefreshScrollView = (PullToRefreshScrollView) rootView.findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                switch (currentFragmentIndex) {
                    case Constants.INDEX_FAMILIAR:
                        homeFamiliarView.reloadData();
                        break;
                    case Constants.INDEX_ENTERPRISE:
                        homeEnterpriseView.reloadData();
                        break;
                    case Constants.INDEX_COMEDITY:
                        homeComedityView.reloadData();
                        break;
                    case Constants.INDEX_ITEM:
                        homeItemView.reloadData();
                        break;
                    case Constants.INDEX_SERVE:
                        homeServeView.reloadData();
                        break;
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                switch (currentFragmentIndex) {
                    case Constants.INDEX_FAMILIAR:
                        homeFamiliarView.loadData();
                        break;
                    case Constants.INDEX_ENTERPRISE:
                        homeEnterpriseView.loadData();
                        break;
                    case Constants.INDEX_COMEDITY:
                        homeComedityView.loadData();
                        break;
                    case Constants.INDEX_ITEM:
                        homeItemView.loadData();
                        break;
                    case Constants.INDEX_SERVE:
                        homeServeView.loadData();
                        break;
                }
            }
        });
        mScrollView = mPullRefreshScrollView.getRefreshableView();
        mScrollBody = (FrameLayout) rootView.findViewById(R.id.scroll_body);
        mImgAdd = (ImageButton) rootView.findViewById(R.id.btn_add);

        homeFamiliarView = new HomeFamiliarView(getContext(), this);
        homeEnterpriseView = new HomeEnterpriseView(getContext(), this);
        homeComedityView = new HomeComedityView(getContext(), this);
        homeItemView = new HomeItemView(getContext(), this);
        homeServeView = new HomeServeView(getContext(), this);

        mTabViews = new ArrayList<BaseView>();
        mTabViews.add(homeFamiliarView);
        mTabViews.add(homeEnterpriseView);
        mTabViews.add(homeComedityView);
        mTabViews.add(homeItemView);
        mTabViews.add(homeServeView);

        btnSortSet.setOnItemSelectListener(new SimpleSortSpinner.OnItemSelectListener() {
            @Override
            public void onItemSelected(int index) {
                switch (currentFragmentIndex) {
                    case Constants.INDEX_FAMILIAR:
                        homeFamiliarView.mOrder = index;
                        homeFamiliarView.reloadData();
                        break;
                    case Constants.INDEX_ENTERPRISE:
                        homeEnterpriseView.mOrder = index;
                        homeEnterpriseView.reloadData();
                        break;
                    case Constants.INDEX_COMEDITY:
                        homeComedityView.mOrder = index;
                        homeComedityView.reloadData();
                        break;
                    case Constants.INDEX_ITEM:
                        homeItemView.mOrder = index;
                        homeItemView.reloadData();
                        break;
                    case Constants.INDEX_SERVE:
                        homeServeView.mOrder = index;
                        homeServeView.reloadData();
                        break;
                }
            }
        });

        recommendViewPager = (AutoScrollViewPager) rootView.findViewById(R.id.autoScrollViewPager);
        pageIndicator = (PageIndicator) rootView.findViewById(R.id.pageIndicator);

        info = new SyncInfo(getActivity());

        new CarouselAsync().execute();
        if (AppConfig.getInstance().cityList.size() == 0)
            new CityListAsync().execute();
        if (AppConfig.getInstance().xyleixingList.size() == 0)
            new XyleixingListAsync().execute();

        setCurrentPage(currentFragmentIndex);

        return rootView;
    }

    HomeImageSliderAdapter.OnItemClickListener imageClickListener = new HomeImageSliderAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            CarouselImageModel item = listCarouselList.get(position);
            if (item.getKind() == Constants.MEDIA_TYPE_VIDEO) {
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                intent.putExtra("path", item.getVideoUrl());
                intent.putExtra("title", item.getVideoTitle());
                intent.putExtra("comment", item.getVideoComment());
                startActivity(intent);
            }
        }
    };

    public void setCurrentPage(int index) {
        selectTopTabButton(index);
        mScrollBody.removeAllViews();
        mImgAdd.setVisibility(View.GONE);

//        mPager.setCurrentItem(index);
        String keyword = "";
        if (index == Constants.INDEX_FAMILIAR) {
            keyword = (homeFamiliarView.keyword.equals("")) ? homeFamiliarView.keywordCode : homeFamiliarView.keyword;
            mScrollBody.addView(homeFamiliarView);
            if (homeFamiliarView.listFriend.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
        } else if (index == Constants.INDEX_ENTERPRISE) {
            keyword = homeEnterpriseView.keyword;
            mScrollBody.addView(homeEnterpriseView);
            if (homeEnterpriseView.listEnterprise.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
        } else if (index == Constants.INDEX_COMEDITY) {
            keyword = homeComedityView.keyword;
            mScrollBody.addView(homeComedityView);
            if (homeComedityView.listComedity.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
            if (SessionInstance.getInstance().getLoginData().getUser().getAkind() == Constants.ACCOUNT_TYPE_ENTERPRISE) {
                mImgAdd.setVisibility(View.VISIBLE);
                mImgAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                            Intent intent = new Intent(getActivity(), MakeComedityActivity.class);
                            getActivity().startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            CommonUtils.showRealnameCertAlert(getActivity());
                        }
                    }
                });
            }
        } else if (index == Constants.INDEX_ITEM) {
            keyword = homeItemView.keyword;
            mScrollBody.addView(homeItemView);
            mImgAdd.setVisibility(View.VISIBLE);
            if (homeItemView.listItem.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
            mImgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        Intent intent = new Intent(getActivity(), MakeItemActivity.class);
                        getActivity().startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        CommonUtils.showRealnameCertAlert(getActivity());
                    }
                }
            });
        } else if (index == Constants.INDEX_SERVE) {
            keyword = homeServeView.keyword;
            mScrollBody.addView(homeServeView);
            mImgAdd.setVisibility(View.VISIBLE);
            if (homeServeView.listServe.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
            mImgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SessionInstance.getInstance().getLoginData().getUser().getTestStatus() == TEST_STATUS_PASSED) {
                        Intent intent = new Intent(getActivity() , MakeServeActivity.class);
                        getActivity().startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        CommonUtils.showRealnameCertAlert(getActivity());
                    }
                }
            });
        }

        MainActivity.mainActivity.showKeyword(keyword);
    }

    public void callBackDataLoad() {
        if (currentFragmentIndex == Constants.INDEX_FAMILIAR) {
            if (homeFamiliarView.listFriend.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
        } else if (currentFragmentIndex == Constants.INDEX_ENTERPRISE) {
            if (homeEnterpriseView.listEnterprise.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
        } else if (currentFragmentIndex == Constants.INDEX_COMEDITY) {
            if (homeComedityView.listComedity.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
        } else if (currentFragmentIndex == Constants.INDEX_ITEM) {
            if (homeItemView.listItem.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
        } else if (currentFragmentIndex == Constants.INDEX_SERVE) {
            if (homeServeView.listServe.size() == 0)
                viewBlankPart.setVisibility(View.VISIBLE);
            else
                viewBlankPart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        recommendViewPager.startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        recommendViewPager.stopAutoScroll();
    }

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
                case R.id.btn_play:

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
                sortOrder = (homeFamiliarView != null) ? homeFamiliarView.mOrder : 0;
                break;
            case Constants.INDEX_ENTERPRISE:
                btnEnterprise.setChecked(true);
                sortOrder = (homeEnterpriseView != null) ? homeEnterpriseView.mOrder : 0;
                break;
            case Constants.INDEX_COMEDITY:
                btnComedity.setChecked(true);
                sortOrder = (homeComedityView != null) ? homeComedityView.mOrder : 0;
                break;
            case Constants.INDEX_ITEM:
                btnItem.setChecked(true);
                sortOrder = (homeItemView != null) ? homeItemView.mOrder : 0;
                break;
            case Constants.INDEX_SERVE:
                btnServe.setChecked(true);
                sortOrder = (homeServeView != null) ? homeServeView.mOrder : 0;
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
                        cdtEnterpriseView.setOnConditionClickListener(homeEnterpriseView.mConditionClickListener);
                    }
                    cdtEnterpriseView.setData(homeEnterpriseView.mCityName, homeEnterpriseView.enterKind, homeEnterpriseView.xyList);
                    MainActivity.mainActivity.layoutConditionBody.addView(cdtEnterpriseView);
                    cdtEnterpriseView.setVisibility(View.VISIBLE);
                    cdtView = cdtEnterpriseView;
                    break;
                case Constants.INDEX_COMEDITY:
                    if (cdtComedityView== null) {
                        cdtComedityView = new ConditionComedityView(getContext());
                        cdtComedityView.setOnConditionClickListener(homeComedityView.mConditionClickListener);
                    }
                    cdtComedityView.setData(homeComedityView.mCityName, 0, homeComedityView.xyList);
                    MainActivity.mainActivity.layoutConditionBody.addView(cdtComedityView);
                    cdtComedityView.setVisibility(View.VISIBLE);
                    cdtView = cdtComedityView;
                    break;
                case Constants.INDEX_ITEM:
                    if (cdtItemView == null) {
                        cdtItemView = new ConditionItemServeView(getContext(), 0);
                        cdtItemView.setOnConditionClickListener(homeItemView.mConditionClickListener);
                    }
                    cdtItemView.setData(homeItemView.mCityName, homeItemView.aKind, homeItemView.xyList);
                    MainActivity.mainActivity.layoutConditionBody.addView(cdtItemView);
                    cdtItemView.setVisibility(View.VISIBLE);
                    cdtView = cdtItemView;
                    break;
                case Constants.INDEX_SERVE:
                    if (cdtServeView == null) {
                        cdtServeView = new ConditionItemServeView(getContext(), 1);
                        cdtServeView.setOnConditionClickListener(homeServeView.mConditionClickListener);
                    }
                    cdtServeView.setData(homeServeView.mCityName, homeServeView.aKind, homeServeView.xyList);
                    MainActivity.mainActivity.layoutConditionBody.addView(cdtServeView);
                    cdtServeView.setVisibility(View.VISIBLE);
                    cdtView = cdtServeView;
                    break;
                case Constants.INDEX_FAMILIAR:
                default:
                    if (cdtFamiliarView == null) {
                        cdtFamiliarView = new ConditionFamiliarView(getContext());
                        cdtFamiliarView.setOnConditionClickListener(homeFamiliarView.mConditionClickListener);
                    }
                    cdtFamiliarView.setData(homeFamiliarView.mCityName, homeFamiliarView.aKind, homeFamiliarView.xyList);
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
            Utils.disappearProgressDialog();
            if (getActivity() != null && !getActivity().isDestroyed() && !getActivity().isFinishing()) {
                if (result.isValid()) {
                    if (result.getRetCode() == ERROR_OK && result.getImgList().size() > 0) {
                        listCarouselList = result.getImgList();

                        recommendImageAdapter = new HomeImageSliderAdapter(listCarouselList).setInfiniteLoop(true);
                        recommendImageAdapter.setOnItemClickListener(imageClickListener);
                        recommendViewPager.setAdapter(recommendImageAdapter);
                        recommendViewPager.setInterval(5000);
                        pageIndicator.setViewPager(recommendViewPager);

                        recommendViewPager.startAutoScroll();
                    } else {
                        Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
                }
            }
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
            if (AppConfig.getInstance() != null) {
                if (result.isValid()) {
                    if (result.getRetCode() == ERROR_OK) {
                        AppConfig.getInstance().cityList.clear();
                        AppConfig.getInstance().cityList.addAll(result.getList());
                    }
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
            if (AppConfig.getInstance() != null) {
                if (result.isValid()) {
                    if (result.getRetCode() == ERROR_OK) {
                        AppConfig.getInstance().xyleixingList.clear();
                        AppConfig.getInstance().xyleixingList.addAll(result.getList());
                    }
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
