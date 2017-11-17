package com.beijing.chengxin.ui.view;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.adapter.FollowAccountListAdapter;
import com.beijing.chengxin.ui.widget.IndexBarFollow;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.beijing.chengxin.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FollowAccountListView extends BaseView {
    public static final String INDEX_TITLE = "follow_account_title";

    public static final int INDEX_PERSON = 1;
    public static final int INDEX_ENTERPRISE = 2;
    public static final int INDEX_MYHOME_PERSON = 3;
    public static final int INDEX_MYHOME_ENTERPRISE = 4;
    public static final int INDEX_FRIEND_1_PERSON = 5;
    public static final int INDEX_FRIEND_1_ENTERPRISE = 6;
    public static final int INDEX_FRIEND_2_PERSON = 7;
    public static final int INDEX_FRIEND_2_ENTERPRISE = 8;
    public static final int INDEX_FRIEND_3_PERSON = 9;
    public static final int INDEX_FRIEND_3_ENTERPRISE = 10;

    private int mIndexFlag = INDEX_PERSON;

    private ListView mListView;
    private FollowAccountListAdapter mAdapter;

    private AppConfig mAppConfig;
    private Set<String> mFollowIdDatas;
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

        mAdapter = new FollowAccountListAdapter();
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        IndexBarFollow indexbar = (IndexBarFollow) findViewById(R.id.indexbar);
        indexbar.setWidgets(mListView, mAdapter, (TextView) findViewById(R.id.txt_popup));

        mAppConfig = new AppConfig(mActivity);
    }

    private void alignDatas() {
        if (mDatas != null) {
            new AsyncTask<Object, Object, Object>() {
                @Override
                protected void onPreExecute() {
                    Utils.displayProgressDialog(mActivity);
                }
                @Override
                protected Object doInBackground(Object... params) {
                    PinyinUtils.create(mActivity, R.array.pinyin);

                    String title = FollowAccountListView.getIndexTitle(mIndexFlag);
                    mFollowIdDatas = mAppConfig.getStringSetValue(title, new HashSet<String>());

                    for (int i = 0; i < mDatas.size(); i++) {
                        UserModel item = mDatas.get(i);
                        if (mFollowIdDatas != null && mFollowIdDatas.contains("" + item.getId())) {
                            item.alias = Constants.STR_STAR + (item.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? item.getRealname() : item.getEnterName());
                        } else {
                            item.alias = (item.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? item.getRealname() : item.getEnterName());
                        }

                        item.alias = PinyinUtils.convert(item.alias);
                    }

                    CommonUtils.sortAccountByChinese(mDatas);

                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    mAdapter.setDatas(mIndexFlag, mAppConfig, mDatas, mFollowIdDatas);
                    mAdapter.notifyDataSetChanged();

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
            case INDEX_MYHOME_PERSON:
                title = AppConfig.TITLE_FOLLOW_MYHOME_PERSON;
                break;
            case INDEX_MYHOME_ENTERPRISE:
                title = AppConfig.TITLE_FOLLOW_MYHOME_ENTERPRISE;
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

}
