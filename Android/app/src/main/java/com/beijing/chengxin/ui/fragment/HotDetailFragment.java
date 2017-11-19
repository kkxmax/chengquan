package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.EvalListModel;
import com.beijing.chengxin.network.model.EvalModel;
import com.beijing.chengxin.network.model.HotModel;
import com.beijing.chengxin.network.model.UserListModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.view.BaseView;
import com.beijing.chengxin.ui.view.HotEvalListView;
import com.beijing.chengxin.ui.view.PartnerListView;
import com.beijing.chengxin.ui.view.TabComidityView;
import com.beijing.chengxin.ui.view.TabEvalView;
import com.beijing.chengxin.ui.view.TabItemView;
import com.beijing.chengxin.ui.view.TabServeView;
import com.beijing.chengxin.ui.widget.AutoScrollViewPager;
import com.beijing.chengxin.ui.widget.ListViewNoScroll;
import com.beijing.chengxin.ui.widget.PageIndicator;
import com.beijing.chengxin.ui.widget.UrlImagePagerAdapter;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ENTER_TYPE_PERSONAL;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class HotDetailFragment extends Fragment {

	public final String TAG = HotDetailFragment.class.getName();
    private View rootView;

    private ToggleButton btnEval ,btnEnter, btnFavourite;

    private TextView txt_hot_title, txt_read_count, txt_write_time, txt_content, txt_comment_count, txt_elect_count;
    ScrollView scrollView;

    AutoScrollViewPager recommendViewPager;
    PageIndicator pageIndicator;
    UrlImagePagerAdapter recommendImageAdapter;
    List<String> listRecommendImageUrl;

    MyAdapter mAdapter;
    ViewPager mPager;

    ArrayList<BaseView> mTabViews;
    HotEvalListView mListView1;
    PartnerListView mListView2;

    List<EvalModel> evalList;
    List<UserModel> enterList;
    int selectedIndex;

    SyncInfo info;

    HotModel item;

    public void setItem(HotModel item) {
        this.item = item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_hot_detail, container, false);

        // set title
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.hot_detail));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mClickListener);

        btnEval = (ToggleButton)rootView.findViewById(R.id.btn_eval);
        btnEnter = (ToggleButton)rootView.findViewById(R.id.btn_enterprise);
        btnFavourite = (ToggleButton)rootView.findViewById(R.id.btn_favorite);
        txt_elect_count = (TextView)rootView.findViewById(R.id.btn_elect_count);

        rootView.findViewById(R.id.btn_share_content).setOnClickListener(mClickListener);
        btnEval.setOnClickListener(mClickListener);
        btnEnter.setOnClickListener(mClickListener);
        btnFavourite.setOnClickListener(mClickListener);

        txt_hot_title = (TextView)rootView.findViewById(R.id.txt_hot_title);
        txt_read_count = (TextView)rootView.findViewById(R.id.txt_read_count);
        txt_write_time = (TextView)rootView.findViewById(R.id.txt_write_time);
        txt_content = (TextView)rootView.findViewById(R.id.txt_content);
        txt_comment_count = (TextView)rootView.findViewById(R.id.txt_comment_count);

        txt_hot_title.setText(item.getTitle());
        txt_content.setText(item.getContent());
        txt_read_count.setText(String.valueOf(item.getVisitCnt()));
        txt_write_time.setText(item.getWriteTimeString());
        txt_comment_count.setText(String.valueOf(item.getCommentCnt()));
        txt_elect_count.setText(String.valueOf(item.getElectCnt()));
        btnFavourite.setChecked(item.getIsFavourite() == 1);

        scrollView = (ScrollView)rootView.findViewById(R.id.scrollView);

        recommendViewPager = (AutoScrollViewPager) rootView.findViewById(R.id.autoScrollViewPager);
        listRecommendImageUrl = new ArrayList<String>();
        for (int i = 0; i < item.getImgPaths().size(); i++)
            listRecommendImageUrl.add(Constants.FILE_ADDR + item.getImgPaths().get(i));

        recommendImageAdapter = new UrlImagePagerAdapter(listRecommendImageUrl).setInfiniteLoop(true);
        recommendViewPager.setAdapter(recommendImageAdapter);
        recommendViewPager.setInterval(5000);
        pageIndicator = (PageIndicator) rootView.findViewById(R.id.pageIndicator);
        pageIndicator.setViewPager(recommendViewPager);
        recommendViewPager.startAutoScroll();

        evalList = new ArrayList<EvalModel>();
        enterList = new ArrayList<UserModel>() ;

        mListView1 = new HotEvalListView(getContext());
        mListView2 = new PartnerListView(getContext());

        mTabViews = new ArrayList<BaseView>();
        mTabViews.add(mListView1);
        mTabViews.add(mListView2);

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

        info = new SyncInfo(getActivity());

        new EvalListAsync().execute("0", "-1", String.valueOf(item.getId()));

        return rootView;
    }

    public void onChangedViewSize() {
        BaseView itemView = mTabViews.get(mPager.getCurrentItem());
        itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height = itemView.getMeasuredHeight();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPager.getLayoutParams();
        params.height = height;
        mPager.setLayoutParams(params);
    }

    @Override
    public void onResume() {
        super.onResume();
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0,0);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    ((BaseFragmentActivity)getActivity()).onBackPressed();
                    break;
                case R.id.btn_eval:
                    selectTabButton(0);
                    mPager.setCurrentItem(0);
                    break;
                case R.id.btn_enterprise:
                    selectTabButton(1);
                    mPager.setCurrentItem(1);
                    break;
                case R.id.btn_favorite:
                    int checked = (item.getIsFavourite() + 1) % 2;
                    btnFavourite.setChecked(item.getIsFavourite() == 1);
                    new SetFavouriteAsync().execute(String.valueOf(item.getId()), String.valueOf(checked), "2");
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
                btnEnter.setChecked(true);
                if (enterList.size() < 1)
                    new EnterpriseListAsync().execute("0", "-1", String.valueOf(item.getId()));
                break;
        }
    }

    private void resetTabBtn(){
        btnEval.setChecked(false);
        btnEnter.setChecked(false);
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

    class EvalListAsync extends AsyncTask<String, String, EvalListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected EvalListModel doInBackground(String... strs) {
            return info.syncEstimateListForHot(strs[0], strs[1], strs[2]);
        }
        @Override
        protected void onPostExecute(EvalListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    evalList.addAll(result.getList());
                    mListView1.setData(evalList, new OnItemClickListener() {
                        @Override
                        public void onListItemClick(int position, View view) {
                            EvalDetailFragment fragment = new EvalDetailFragment();
                            fragment.setEvalModel(evalList.get(position));
                            ((BaseFragmentActivity)getActivity()).addFragment(fragment);
                        }
                    });
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onChangedViewSize();
                        }
                    }, 100);
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

    class EnterpriseListAsync extends AsyncTask<String, String, UserListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected UserListModel doInBackground(String... strs) {
            return info.syncPartnerList(strs[0], strs[1], strs[2]);
        }
        @Override
        protected void onPostExecute(UserListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    enterList.clear();
                    enterList.addAll(result.getList());
                    mListView2.setData(enterList, new OnItemClickListener() {
                        @Override
                        public void onListItemClick(int position, View view) {
                            EnterpriseDetailFragment fragment = new EnterpriseDetailFragment();
                            fragment.setId(enterList.get(position).getId());
                            ((BaseFragmentActivity)getActivity()).addFragment(fragment);
                        }
                    });

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onChangedViewSize();
                        }
                    }, 100);
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

    class SetFavouriteAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncSetFavourite(strs[0], strs[1], strs[2]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    int checked = (item.getIsFavourite() + 1) % 2;
                    item.setIsFavourite(checked);
                    btnFavourite.setChecked(item.getIsFavourite() == 1);
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

}
