package com.beijing.chengxin.ui.activity;

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
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.UserListModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.listener.OnItemClickListener;
import com.beijing.chengxin.ui.view.FollowAccountListView;
import com.beijing.chengxin.ui.widget.Utils;

import java.util.ArrayList;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class FollowTabAccountActivity extends ParentFragmentActivity {

    public final String TAG = FollowTabAccountActivity.class.getName();

    private Context mContext;
    private int mIndexFlag = FollowAccountListView.INDEX_PERSON;

    ToggleButton btnPerson, btnEnterprise;
    FrameLayout viewBody;
    FollowAccountListView mPersonView, mEnterpriseView;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constants.NOTIFY_FOLLOW_INFO_CHANGED) {
                getDataTask();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mIndexFlag = getIntent().getExtras().getInt(FollowAccountListView.INDEX_TITLE);

        setContentView(R.layout.activity_favorite_friend);

        String title = FollowAccountActivity.getHeaderTitle(this, mIndexFlag);
        ((TextView)findViewById(R.id.txt_nav_title)).setText(title);

        findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        btnPerson = (ToggleButton)findViewById(R.id.btn_person);
        btnEnterprise = (ToggleButton)findViewById(R.id.btn_enterprise);
        btnPerson.setOnClickListener(mButtonClickListener);
        btnEnterprise.setOnClickListener(mButtonClickListener);

        viewBody = (FrameLayout) findViewById(R.id.view_body);

        mPersonView = new FollowAccountListView(this);
        //mPersonView.setData(mIndexFlag);
        mEnterpriseView = new FollowAccountListView(this);
        //mEnterpriseView.setData(mIndexFlag + 1);

        viewBody.addView(mPersonView);
        viewBody.addView(mEnterpriseView);

        mPersonView.setVisibility(View.VISIBLE);
        mEnterpriseView.setVisibility(View.GONE);

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
                int friendLevel = -1;
                if (mIndexFlag == FollowAccountListView.INDEX_FRIEND_1_PERSON)
                    friendLevel = 1;
                if (mIndexFlag == FollowAccountListView.INDEX_FRIEND_2_PERSON)
                    friendLevel = 2;
                if (mIndexFlag == FollowAccountListView.INDEX_FRIEND_3_PERSON)
                    friendLevel = 3;

                UserListModel personDatas = new SyncInfo(mContext).syncMyInterestList(Constants.ACCOUNT_TYPE_PERSON, friendLevel);
                publishProgress(1, personDatas);

                UserListModel enterDatas = new SyncInfo(mContext).syncMyInterestList(Constants.ACCOUNT_TYPE_ENTERPRISE, friendLevel);
                publishProgress(2, enterDatas);

                return null;
            }
            @Override
            protected void onProgressUpdate(Object... values) {
                super.onProgressUpdate(values);
                int index = (int) values[0];
                UserListModel result = (UserListModel) values[1];
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        if (index == 1)
                            mPersonView.setData((ArrayList<UserModel>) result.getList(), mIndexFlag);

                        if (index == 2)
                            mEnterpriseView.setData((ArrayList<UserModel>) result.getList(), mIndexFlag+1);

                    } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                        ChengxinApplication.finishActivityFromDuplicate(FollowTabAccountActivity.this);
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.err_server), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Utils.disappearProgressDialog();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute();
    }

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
//            Intent intent = new Intent(this, DetailActivity.class);
//            intent.putExtra("type", Constants.INDEX_ENTERPRISE);
//            startActivity(intent);
        }
    };

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_person:
                    mPersonView.setVisibility(View.VISIBLE);
                    mEnterpriseView.setVisibility(View.GONE);
                    btnPerson.setChecked(true);
                    btnEnterprise.setChecked(false);
                    break;
                case R.id.btn_enterprise:
                    mPersonView.setVisibility(View.GONE);
                    mEnterpriseView.setVisibility(View.VISIBLE);
                    btnPerson.setChecked(false);
                    btnEnterprise.setChecked(true);
                    break;
                case R.id.btn_back:
                    onBackPressed();
                    break;
            }
        }
    };

}
