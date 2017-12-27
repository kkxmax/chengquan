package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.UserListModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.fragment.MainHomeFragment;
import com.beijing.chengxin.ui.listener.OnCallbackListener;
import com.beijing.chengxin.ui.widget.CustomToast;
import com.beijing.chengxin.ui.activity.DetailActivity;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.listener.OnConditionClickListener;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ACCOUNT_TYPE_PERSON;
import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.PAGE_ITEM_COUNT;
import static com.beijing.chengxin.config.Constants.TEST_STATUS_PASSED;

public class HomeFamiliarView extends BaseView {

    public final String TAG = HomeFamiliarView.class.getName();

    MainHomeFragment mParentFragment;
    LinearLayout mListView;
    ImageButton btnAdd;

    SyncInfo info;
    AppConfig appConfig;

    OnCallbackListener callBackListener;
    public List<UserModel> listFriend;
    public List<View> listFriendView;

    public int mOrder;
    public String mCityName = "";
    public int aKind;
    public List<Integer> xyList;
    public String keyword = "";
    public String keywordCode = "";

    boolean isDataLoading = false;

    public HomeFamiliarView(Context context) {
        super(context);
        initialize();
    }

    public HomeFamiliarView(Context context, MainHomeFragment parent) {
        super(context);
        this.mParentFragment = parent;
        initialize();
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.fragment_home);

        mListView = (LinearLayout)findViewById(R.id.listView);

        btnAdd = (ImageButton)findViewById(R.id.btn_add);
    }

    @Override
    protected void initData() {
        super.initData();

        info = new SyncInfo(mActivity);
        appConfig = new AppConfig(mActivity);

        if (listFriend == null)
            listFriend = new ArrayList<UserModel>();
        if (listFriendView == null)
            listFriendView = new ArrayList<View>();

        if (xyList == null)
            xyList = new ArrayList<Integer>();

        if (listFriend.size() == 0)
            loadData();
    }

    @Override
    public void refreshUI() {
        super.refreshUI();

        reloadData();
    }

    public void loadData() {
        if (isDataLoading)
            return;
        String start = String.valueOf(listFriend.size());
        String length = String.valueOf(PAGE_ITEM_COUNT);
        String order = String.valueOf(mOrder + 1);
        String akindStr = String.valueOf(aKind);
        String xyleixingId = "";
        for (int i = 0; i < xyList.size(); i ++) {
            if (!xyleixingId.equals(""))
                xyleixingId += ",";
            xyleixingId += String.valueOf(xyList.get(i));
        }

        new FriendListAsync().execute(start, length, order, mCityName, akindStr, xyleixingId, keyword, keywordCode);
    }

    public void setKeyword(String word, String code) {
        keyword = word;
        keywordCode = code;

        if (info != null)
            reloadData();
    }

    public void reloadData() {
        listFriend.clear();
        listFriendView.clear();
        mListView.removeAllViews();
        loadData();
    }

    public OnConditionClickListener mConditionClickListener = new OnConditionClickListener() {
        @Override
        public void onClickReset() {
        }
        @Override
        public void onClickOk(String cityName, int kind, List<Integer>typeList) {
            mCityName = cityName;
            aKind = kind;
            xyList = typeList;

            reloadData();
            MainActivity.mainActivity.hideConditionView(Constants.INDEX_FAMILIAR);
        }
    };

    private void initListData(List<UserModel> listData) {
        for (int i = 0; i < listData.size(); i++) {
            View itemView = getItemView(listData.get(i));

            listFriend.add(listData.get(i));
            listFriendView.add(itemView);

            mListView.addView(itemView);
        }
    }

    private View getItemView(final UserModel item) {
        final View convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_enterprise, null);

        ImageView img_avatar = (ImageView) convertView.findViewById(R.id.img_avatar);
        final ToggleButton btn_follow = (ToggleButton) convertView.findViewById(R.id.btn_follow);
        TextView txt_item_type = (TextView) convertView.findViewById(R.id.txt_item_type);
        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
        TextView txt_job_type = (TextView) convertView.findViewById(R.id.txt_job_type);
        TextView txt_chengxin_id = (TextView) convertView.findViewById(R.id.txt_chengxin_id);
        TextView txt_main_comedity = (TextView) convertView.findViewById(R.id.txt_main_comedity);
        TextView txt_item = (TextView) convertView.findViewById(R.id.txt_item);
        TextView txt_serve = (TextView) convertView.findViewById(R.id.txt_serve);
        TextView txt_suggest_man = (TextView) convertView.findViewById(R.id.txt_suggest_man);
        TextView txt_chengxin_rate = (TextView) convertView.findViewById(R.id.txt_chengxin_rate);
        TextView txt_like_count = (TextView) convertView.findViewById(R.id.txt_like_count);
        TextView txt_eval_count = (TextView) convertView.findViewById(R.id.txt_eval_count);
        LinearLayout layout_comedity = (LinearLayout) convertView.findViewById(R.id.layout_comedity);
        LinearLayout layout_item = (LinearLayout) convertView.findViewById(R.id.layout_item);
        LinearLayout layout_serve = (LinearLayout) convertView.findViewById(R.id.layout_serve);
        LinearLayout layout_recommender = (LinearLayout) convertView.findViewById(R.id.layout_recommender);

        if (item.getTestStatus() == Constants.TEST_STATUS_PASSED) {
            if (item.getAkind() == Constants.ACCOUNT_TYPE_PERSON) {
                Picasso.with(mActivity)
                        .load(Constants.FILE_ADDR + item.getLogo())
                        .placeholder(R.drawable.no_image_person)
                        .into(img_avatar);
                txt_item_type.setText(getString(R.string.str_person));
            }
            else {
                Picasso.with(mActivity)
                        .load(Constants.FILE_ADDR + item.getLogo())
                        .placeholder(R.drawable.no_image_enter)
                        .into(img_avatar);
                txt_item_type.setText(getString(R.string.str_enterprise));
            }
        } else {
            Picasso.with(mActivity)
                    .load(Constants.FILE_ADDR + item.getLogo())
                    .placeholder(R.drawable.no_image_person)
                    .into(img_avatar);
            txt_item_type.setText(getString(R.string.str_newer));
        }

        txt_name.setText(CommonUtils.getUserName(item));

        if (item.getReqCodeSenderId() > 0) {
            if (item.getReqCodeSenderAkind() == ACCOUNT_TYPE_PERSON) {
                String senderName = item.getReqCodeSenderRealname();
                if (senderName.equals(""))
                    senderName = item.getReqCodeSenderMobile();
                if (!item.getInviterFriendLevel().equals(""))
                    senderName = item.getInviterFriendLevel() + " - " + senderName;
                txt_suggest_man.setText(senderName);
            } else {
                String senderName = item.getReqCodeSenderEnterName();
                if (senderName.equals(""))
                    senderName = item.getReqCodeSenderMobile();
                if (!item.getInviterFriendLevel().equals(""))
                    senderName = item.getInviterFriendLevel() + " - " + senderName;
                txt_suggest_man.setText(senderName);
            }
            layout_recommender.setVisibility(View.VISIBLE);
        } else {
            layout_recommender.setVisibility(View.GONE);
        }

        if (item.getXyName().equals(""))
            txt_job_type.setVisibility(View.GONE);
        else {
            txt_job_type.setText(item.getXyName());
            txt_job_type.setVisibility(View.VISIBLE);
        }
        txt_chengxin_id.setText(item.getCode());

        if (item.getProducts().size() > 0) {
            String productStr = CommonUtils.getComedityListName(item.getProducts());
            txt_main_comedity.setText(productStr);
            layout_comedity.setVisibility(View.VISIBLE);
        } else
            layout_comedity.setVisibility(View.GONE);

        if (item.getItems().size() > 0) {
            String itemStr = CommonUtils.getItemListName(item.getItems());
            txt_item.setText(itemStr);
            layout_item.setVisibility(View.VISIBLE);
        } else
            layout_item.setVisibility(View.GONE);

        if (item.getServices().size() > 0) {
            String serveStr = CommonUtils.getServeListName(item.getServices());
            txt_serve.setText(serveStr);
            layout_serve.setVisibility(View.VISIBLE);
        } else
            layout_serve.setVisibility(View.GONE);

        if (item.getCredit() < Constants.LEVEL_ZERO)
            txt_chengxin_rate.setText(mActivity.getText(R.string.rate_zero));
        else
            txt_chengxin_rate.setText(String.format("%d%%", item.getCredit()));
        txt_like_count.setText(String.valueOf(item.getElectCnt()));
        txt_eval_count.setText(String.valueOf(item.getFeedbackCnt()));

        final boolean interest = (item.getInterested() != 0);
        btn_follow.setChecked(interest);

        btn_follow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_follow.setChecked(interest);
                int mInterest = (item.getInterested() + 1) % 2;
                setInterestTask(item, convertView, item.getId(), mInterest);
            }
        });

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getTestStatus() == TEST_STATUS_PASSED) {
                    increaseViewCountTask(item, Constants.VIEW_CNT_KIND_PERSONAL_OR_ENTER, item.getId(), "");
                } else {
                    CustomToast.makeText(mActivity, getString(R.string.err_not_test_passed_person), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    class FriendListAsync extends AsyncTask<String, String, UserListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDataLoading = true;
            Utils.displayProgressDialog(mActivity);
        }
        @Override
        protected UserListModel doInBackground(String... strs) {
            return info.syncFriendList(strs[0], strs[1], strs[2], strs[3], strs[4], strs[5], strs[6], strs[7]);
        }
        @Override
        protected void onPostExecute(UserListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    initListData(result.getList());
                    mListView.invalidate();
                } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                    ChengxinApplication.finishAndLoginActivityFromDuplicate(mActivity);
                } else {
                    CustomToast.makeText(mActivity, result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                CustomToast.makeText(mActivity, getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
            isDataLoading = false;
            Utils.disappearProgressDialog();
            mParentFragment.mPullRefreshScrollView.onRefreshComplete();
            mParentFragment.callBackDataLoad();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            isDataLoading = false;
            Utils.disappearProgressDialog();
        }
    }

    private void setInterestTask(final UserModel item, final View itemView, final int id, final int interest) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(mActivity);
            }
            @Override
            protected Object doInBackground(Object... strs) {
                return info.syncSetInterest("" + id, "" + interest);
            }
            @Override
            protected void onPostExecute(Object obj) {
                BaseModel result = (BaseModel) obj;
                if (result .isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        item.setInterested(interest);
                        ToggleButton btn_follow = (ToggleButton) itemView.findViewById(R.id.btn_follow);
                        btn_follow.setChecked(item.getInterested() != 0);
                    } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                        ChengxinApplication.finishAndLoginActivityFromDuplicate(mActivity);
                    } else {
                        CustomToast.makeText(mActivity, result.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    CustomToast.makeText(mActivity, getString(R.string.err_server), Toast.LENGTH_LONG).show();
                }
                Utils.disappearProgressDialog();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute();
    }

    private void increaseViewCountTask(final UserModel item, final int kind, final int id, final String hotId) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(mActivity);
            }
            @Override
            protected Object doInBackground(Object... strs) {
                return info.syncIncreaseViewCount("" + kind, "" + id, hotId);
            }
            @Override
            protected void onPostExecute(Object obj) {
                BaseModel result = (BaseModel) obj;
                if (result .isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        Intent intent = new Intent(mActivity, DetailActivity.class);
                        intent.putExtra("id", item.getId());
                        intent.putExtra("type", Constants.INDEX_ENTERPRISE);
                        intent.putExtra("akind", item.getAkind());
                        mActivity.startActivityForResult(intent, Constants.REQEUST_CODE_TEMP);
                        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                        ChengxinApplication.finishAndLoginActivityFromDuplicate(mActivity);
                    } else {
                        CustomToast.makeText(mActivity, result.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    CustomToast.makeText(mActivity, getString(R.string.err_server), Toast.LENGTH_LONG).show();
                }
                Utils.disappearProgressDialog();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute();
    }

}
