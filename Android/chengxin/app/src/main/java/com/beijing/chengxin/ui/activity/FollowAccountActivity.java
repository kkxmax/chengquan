package com.beijing.chengxin.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.UserListModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.adapter.FollowAccountListAdapter;
import com.beijing.chengxin.ui.view.FollowAccountListView;
import com.beijing.chengxin.ui.widget.Utils;

import java.util.ArrayList;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class FollowAccountActivity extends ParentFragmentActivity {

    public final String TAG = FollowAccountActivity.class.getName();

    private Context mContext;
    private int mIndexFlag = FollowAccountListView.INDEX_PERSON;

    private FrameLayout mViewBody;
    private FollowAccountListView mListView;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constants.NOTIFY_FOLLOW_INFO_CHANGED) {
                getDataTask();
                Intent notifyIntent = new Intent(Constants.NOTIFY_FOLLOW_INFO_CHANGED_FRAGMENT);
                mContext.sendBroadcast(notifyIntent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mIndexFlag = getIntent().getExtras().getInt(FollowAccountListView.INDEX_TITLE);

        setContentView(R.layout.activity_favorite_account);
        // set title
        String title = getHeaderTitle(this, mIndexFlag);
        ((TextView)findViewById(R.id.txt_nav_title)).setText(title);

        findViewById(R.id.btn_back).setOnClickListener(mClickListener);

        mViewBody = (FrameLayout) findViewById(R.id.view_body);

        mListView = new FollowAccountListView(this);

        mViewBody.addView(mListView);
        getDataTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mBroadcastReceiver, new IntentFilter(Constants.NOTIFY_FOLLOW_INFO_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void getDataTask() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(mContext);
            }
            @Override
            protected Object doInBackground(Object... params) {
                int akind = Constants.ACCOUNT_TYPE_PERSON;
                if (mIndexFlag == FollowAccountListView.INDEX_ENTERPRISE)
                    akind = Constants.ACCOUNT_TYPE_ENTERPRISE;

                int friendLevel = -1;
                if (mIndexFlag == FollowAccountListView.INDEX_MYHOME)
                    friendLevel = 0;

                return new SyncInfo(mContext).syncMyInterestList(akind, friendLevel);
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                UserListModel result = (UserListModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        mListView.setData((ArrayList<UserModel>) result.getList(), mIndexFlag);
                    } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                        ChengxinApplication.finishActivityFromDuplicate(FollowAccountActivity.this);
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.err_server), Toast.LENGTH_LONG).show();
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

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    onBackPressed();
                    break;
            }
        }
    };

    public static String getHeaderTitle(Activity activity, int indexFlag) {
        String title = "";
        switch (indexFlag) {
            case FollowAccountListView.INDEX_PERSON:
                title = activity.getString(R.string.str_follow_title_person);
                break;
            case FollowAccountListView.INDEX_ENTERPRISE:
                title = activity.getString(R.string.str_follow_title_enterprise);
                break;
            case FollowAccountListView.INDEX_MYHOME:
                title = activity.getString(R.string.str_in_my_home);
                break;
            case FollowAccountListView.INDEX_FRIEND_1_PERSON:
            case FollowAccountListView.INDEX_FRIEND_1_ENTERPRISE:
                title = activity.getString(R.string.str_1_du_friend);
                break;
            case FollowAccountListView.INDEX_FRIEND_2_PERSON:
            case FollowAccountListView.INDEX_FRIEND_2_ENTERPRISE:
                title = activity.getString(R.string.str_2_du_friend);
                break;
            case FollowAccountListView.INDEX_FRIEND_3_PERSON:
            case FollowAccountListView.INDEX_FRIEND_3_ENTERPRISE:
                title = activity.getString(R.string.str_3_du_friend);
                break;
        }

        return title;
    }

}
