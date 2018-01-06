package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.EvalListModel;
import com.beijing.chengxin.network.model.EvalModel;
import com.beijing.chengxin.network.model.HotDetailModel;
import com.beijing.chengxin.network.model.HotModel;
import com.beijing.chengxin.network.model.UserListModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.activity.HotEvalActivity;
import com.beijing.chengxin.ui.view.BaseView;
import com.beijing.chengxin.ui.view.HotEvalListView;
import com.beijing.chengxin.ui.view.PartnerListView;
import com.beijing.chengxin.ui.widget.Utils;
import com.hy.chengxin.http.Api.HttpApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class HotDetailFragment extends Fragment {

    public final String TAG = HotDetailFragment.class.getName();
    private View rootView;

    private ToggleButton btnEval ,btnEnter, btnFavourite, btnElectCount;

    private TextView txt_hot_title, txt_read_count, txt_write_time, txt_comment;
    private Button btnCommentCnt, btnShare;
    private WebView webView;

    ScrollView scrollView;
    FrameLayout listBody;

    ArrayList<BaseView> mTabViews;
    HotEvalListView mListView1;
    PartnerListView mListView2;

    List<EvalModel> evalList;
    List<UserModel> enterList;
    int selectedIndex;

    SyncInfo info;

    int hotId;
    HotModel item;

    public void setId(int id) {
        hotId = id;
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
        btnEval.setOnClickListener(mClickListener);
        btnEnter.setOnClickListener(mClickListener);

        btnShare = (Button) rootView.findViewById(R.id.btn_share_content);
        btnShare.setOnClickListener(mClickListener);
        btnCommentCnt = (Button) rootView.findViewById(R.id.btn_comment_count);
        btnElectCount = (ToggleButton) rootView.findViewById(R.id.btn_elect_count);
        btnFavourite = (ToggleButton)rootView.findViewById(R.id.btn_favorite);
        btnShare = (Button) rootView.findViewById(R.id.btn_share_content);
        btnCommentCnt.setOnClickListener(mClickListener);
        btnElectCount.setOnClickListener(mClickListener);
        btnFavourite.setOnClickListener(mClickListener);
        btnShare.setOnClickListener(mClickListener);

        txt_comment = (TextView)rootView.findViewById(R.id.txt_comment);
        txt_comment.setOnClickListener(mClickListener);

        txt_hot_title = (TextView)rootView.findViewById(R.id.txt_hot_title);
        txt_read_count = (TextView)rootView.findViewById(R.id.txt_read_count);
        txt_write_time = (TextView)rootView.findViewById(R.id.txt_write_time);

        scrollView = (ScrollView)rootView.findViewById(R.id.scrollView);
        listBody = (FrameLayout) rootView.findViewById(R.id.list_body);
        webView = (WebView) rootView.findViewById(R.id.webView);

        evalList = new ArrayList<EvalModel>();
        enterList = new ArrayList<UserModel>() ;

        mListView1 = new HotEvalListView(getContext());
        mListView2 = new PartnerListView(getContext());

        mTabViews = new ArrayList<BaseView>();
        mTabViews.add(mListView1);
        mTabViews.add(mListView2);

        info = new SyncInfo(getActivity());

        selectTabButton(0);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        new HotDetailAsync().execute(String.valueOf(hotId));
    }

    // add dd -- 2017.12.07
    public void showShare(Context context, String platformToShare, boolean showContentEdit) {
        cn.sharesdk.onekeyshare.OnekeyShare oks = new cn.sharesdk.onekeyshare.OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }

        final String title = item.getTitle();

        final String url = Constants.HOT_SHARE_URL + item.getId();

        oks.setTitle(title);
        oks.setTitleUrl(url);

        String descStr = item.getSummary();

        final String desc = descStr;

        oks.setText(desc);
        oks.setImageUrl(Constants.BASE_URL + item.getImgPaths().get(0));
        oks.setUrl(url);

        oks.setShareContentCustomizeCallback(new
                                                     ShareContentCustomizeCallback() {
                                                         @Override
                                                         public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                                                             if ("WechatMoments".equals(platform.getName())) {
                                                                 paramsToShare.setTitle(title);
                                                                 paramsToShare.setText(desc);
                                                                 paramsToShare.setImageUrl(Constants.BASE_URL + item.getImgPaths().get(0));
                                                                 paramsToShare.setUrl(url);
                                                             }
                                                         }
                                                     });

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d(TAG, "onComplete ---->  分享成功");
                platform.isClientValid();

                HttpApi.onShare(Constants.SHARE_KIND.HOT, item.getId());
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d(TAG, "onError ---->  分享失败" + throwable.getStackTrace().toString());
                Log.d(TAG, "onError ---->  分享失败" + throwable.getMessage());
                throwable.getMessage();
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

        oks.show(context);
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
                    break;
                case R.id.btn_enterprise:
                    selectTabButton(1);
                    break;
                case R.id.txt_comment:
                    Intent intent = new Intent(getActivity(), HotEvalActivity.class);
                    intent.putExtra("hotId", item.getId());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case R.id.btn_comment_count:
                    HotEvalListFragment fragment1 = new HotEvalListFragment();
                    fragment1.setHotData(item, evalList);
                    ((BaseFragmentActivity)getActivity()).showFragment(fragment1, true);
                    break;
                case R.id.btn_elect_count:
                    int elected = (item.getIsElectedByMe() + 1) % 2;
                    btnElectCount.setChecked(item.getIsElectedByMe() == 1);
                    new SetElectHotAsync().execute(String.valueOf(hotId), String.valueOf(elected));
                    break;
                case R.id.btn_favorite:
                    int checked = (item.getIsFavourite() + 1) % 2;
                    btnFavourite.setChecked(item.getIsFavourite() == 1);
                    new SetFavouriteAsync().execute(String.valueOf(hotId), String.valueOf(checked), "2");
                    break;
                case R.id.btn_share_content:
                    showShare(getContext(), null, true);
                    break;
            }
        }
    };

    private void selectTabButton(int position){
        resetTabBtn();
        listBody.removeAllViews();
        switch(position){
            case 0:
                btnEval.setChecked(true);
                listBody.addView(mListView1);
                break;
            case 1:
                btnEnter.setChecked(true);
                if (enterList.size() < 1)
                    new EnterpriseListAsync().execute("0", "-1", String.valueOf(hotId));
                listBody.addView(mListView2);
                break;
        }
    }

    private void resetTabBtn(){
        btnEval.setChecked(false);
        btnEnter.setChecked(false);
    }

    private void refreshUI() {
        if (item == null)
            return;

        txt_hot_title.setText(item.getTitle());
        txt_read_count.setText(String.valueOf(item.getVisitCnt()));
        txt_write_time.setText(item.getWriteTime().getDateHourMinString());
        btnCommentCnt.setText(String.valueOf(item.getCommentCnt()));

        btnElectCount.setTextOn(String.valueOf(item.getElectCnt()));
        btnElectCount.setTextOff(String.valueOf(item.getElectCnt()));
        if (item.getIsElectedByMe() == 1) {
            btnElectCount.setChecked(true);
        } else {
            btnElectCount.setChecked(false);
        }

        if (item.getIsFavourite() == 1) {
            btnFavourite.setTextOn(getString(R.string.str_already_collect));
            btnFavourite.setTextOff(getString(R.string.str_already_collect));
        } else {
            btnFavourite.setTextOn(getString(R.string.str_collect));
            btnFavourite.setTextOff(getString(R.string.str_collect));
        }
        btnFavourite.setChecked(item.getIsFavourite() == 1);

        webView.loadUrl(Constants.FILE_ADDR + item.getContent());

        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.smoothScrollTo(0,0);

//        if (evalList.size() < 1)
            new EvalListAsync().execute("0", "-1", String.valueOf(hotId));
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

    class HotDetailAsync extends AsyncTask<String, String, HotDetailModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected HotDetailModel doInBackground(String... strs) {
            return info.syncHotDetail(strs[0]);
        }
        @Override
        protected void onPostExecute(HotDetailModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    item = result.getHot();
                    refreshUI();
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishActivityFromDuplicate(getActivity());
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
                    evalList.clear();
                    evalList.addAll(result.getList());
                    mListView1.setData(evalList);
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishActivityFromDuplicate(getActivity());
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
                    mListView2.setData(enterList);
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishActivityFromDuplicate(getActivity());
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

                    if (item.getIsFavourite() == 1) {
                        btnFavourite.setTextOn(getString(R.string.str_already_collect));
                        btnFavourite.setTextOff(getString(R.string.str_already_collect));
                        Toast.makeText(getActivity(), getString(R.string.msg_collect_success), Toast.LENGTH_SHORT).show();
                    } else {
                        btnFavourite.setTextOn(getString(R.string.str_collect));
                        btnFavourite.setTextOff(getString(R.string.str_collect));
                        Toast.makeText(getActivity(), getString(R.string.msg_cancel_collect_success), Toast.LENGTH_SHORT).show();
                    }
                    btnFavourite.setChecked(item.getIsFavourite() == 1);
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishActivityFromDuplicate(getActivity());
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

    class SetElectHotAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncElectHot(strs[0], strs[1]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    int checked = (item.getIsElectedByMe() + 1) % 2;
                    item.setIsElectedByMe(checked);

                    if (item.getIsElectedByMe() == 1) {
                        item.setElectCnt(item.getElectCnt() + 1);
                    } else {
                        item.setElectCnt(item.getElectCnt() - 1);
                    }
                    btnElectCount.setTextOn(String.valueOf(item.getElectCnt()));
                    btnElectCount.setTextOff(String.valueOf(item.getElectCnt()));

                    btnElectCount.setChecked(item.getIsElectedByMe() == 1);
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishActivityFromDuplicate(getActivity());
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
