package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.adapter.FollowAccountListAdapter;
import com.beijing.chengxin.ui.widget.IndexBarFollow;
import com.beijing.chengxin.utils.CommonUtils;
import com.beijing.chengxin.utils.PinyinUtils;

import java.util.ArrayList;

public class FollowAccountListView extends BaseView {
    public static final String INDEX_TITLE = "follow_account_title";

    public static final int INDEX_PERSON = 1;
    public static final int INDEX_ENTERPRISE = 2;
    public static final int INDEX_MYHOME = 3;
    public static final int INDEX_FRIEND_1_PERSON = 5;
    public static final int INDEX_FRIEND_1_ENTERPRISE = 6;
    public static final int INDEX_FRIEND_2_PERSON = 7;
    public static final int INDEX_FRIEND_2_ENTERPRISE = 8;
    public static final int INDEX_FRIEND_3_PERSON = 9;
    public static final int INDEX_FRIEND_3_ENTERPRISE = 10;

    private int mIndexFlag = INDEX_PERSON;

    private View mBlankView;
    private ListView mListView;
    private FollowAccountListAdapter mAdapter;

    private ArrayList<String> mFollowIdDatas;
    private ArrayList<UserModel> mDatas;

    public FollowAccountListView(Context context) {
        super(context);
        initialize();
    }

    public void setData(ArrayList<UserModel> datas, int indexFlag) {
        this.mDatas = datas;
        this.mIndexFlag = indexFlag;
        alignDatas();
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initUI() {
        super.initUI();

        setContentView(R.layout.view_follow_account);

        mBlankView = (View) findViewById(R.id.view_blank_part);
        mAdapter = new FollowAccountListAdapter(mActivity);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        IndexBarFollow indexbar = (IndexBarFollow) findViewById(R.id.indexbar);
        indexbar.setWidgets(mListView, mAdapter, (TextView) findViewById(R.id.txt_popup));
    }

    private void alignDatas() {
        if (mDatas != null) {
            new AsyncTask<Object, Object, Object>() {
                @Override
                protected void onPreExecute() {
                }
                @Override
                protected Object doInBackground(Object... params) {
                    PinyinUtils.create(mActivity, R.array.pinyin);

                    String title = FollowAccountListView.getIndexTitle(mIndexFlag);
                    mFollowIdDatas = AppConfig.getInstance().getStringArrayValue(title, new ArrayList<String>());

                    for (int i = 0; i < mDatas.size(); i++) {
                        UserModel item = mDatas.get(i);
                        String tmpName = item.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? item.getRealname() : item.getEnterName();
                        tmpName = tmpName.length() == 0 ? item.getMobile() : tmpName;
                        if (mFollowIdDatas != null && mFollowIdDatas.contains("" + item.getId())) {
                            item.alias = Constants.STR_STAR + tmpName;
                        } else {
                            item.alias = tmpName;
                        }

                        item.alias = PinyinUtils.convert(item.alias);
                    }

                    CommonUtils.sortAccountByChinese(mDatas);

                    return null;
                }
                @Override
                protected void onPostExecute(Object o) {
                    if (mDatas == null || mDatas.size() == 0) {
                        mBlankView.setVisibility(View.VISIBLE);
                    } else {
                        mBlankView.setVisibility(View.GONE);
                    }
                    mAdapter.setDatas(mIndexFlag, mDatas, mFollowIdDatas);
                    mAdapter.notifyDataSetChanged();
                }
                @Override
                protected void onCancelled() {
                    super.onCancelled();
                }
            }.execute();
        }
    }

    @Override
    protected void initData() {
    }

    public static String getIndexTitle(int indexFlag) {
        String title = "";
        switch (indexFlag) {
            case INDEX_PERSON:
                title = AppConfig.TITLE_FOLLOW_PERSON;
                break;
            case INDEX_ENTERPRISE:
                title = AppConfig.TITLE_FOLLOW_ENTERPRISE;
                break;
            case INDEX_MYHOME:
                title = AppConfig.TITLE_FOLLOW_MYHOME;
                break;
            case INDEX_FRIEND_1_PERSON:
                title = AppConfig.TITLE_FOLLOW_FRIEND_1_PERSON;
                break;
            case INDEX_FRIEND_1_ENTERPRISE:
                title = AppConfig.TITLE_FOLLOW_FRIEND_1_ENTERPRISE;
                break;
            case INDEX_FRIEND_2_PERSON:
                title = AppConfig.TITLE_FOLLOW_FRIEND_2_PERSON;
                break;
            case INDEX_FRIEND_2_ENTERPRISE:
                title = AppConfig.TITLE_FOLLOW_FRIEND_2_ENTERPRISE;
                break;
            case INDEX_FRIEND_3_PERSON:
                title = AppConfig.TITLE_FOLLOW_FRIEND_3_PERSON;
                break;
            case INDEX_FRIEND_3_ENTERPRISE:
                title = AppConfig.TITLE_FOLLOW_FRIEND_3_ENTERPRISE;
                break;
        }
        return title;
    }

    public static String getStarTitle(int indexFlag) {
        String title = "";
        switch (indexFlag) {
            case INDEX_FRIEND_1_PERSON:
                title = "个人列表-星标好友;1度好友-星标好友";
                break;
            case INDEX_FRIEND_1_ENTERPRISE:
                title = "企业列表-星标企业;1度好友-星标好友";
                break;
            case INDEX_FRIEND_2_PERSON:
                title = "个人列表-星标好友;2度好友-星标好友";
                break;
            case INDEX_FRIEND_2_ENTERPRISE:
                title = "企业列表-星标企业;2度好友-星标好友";
                break;
            case INDEX_FRIEND_3_PERSON:
                title = "个人列表-星标好友;3度好友-星标好友";
                break;
            case INDEX_FRIEND_3_ENTERPRISE:
                title = "企业列表-星标企业;3度好友-星标好友";
                break;
        }
        return title;
    }

}
