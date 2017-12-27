package com.beijing.chengxin.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.NoticeCountModel;
import com.beijing.chengxin.ui.activity.MainActivity;

public class SystemNewsFragment extends Fragment {

	public final String TAG = SystemNewsFragment.class.getName();

    private View rootView;
    RelativeLayout layoutNotify, layoutMyEval, layoutEvalMe;
    TextView txtNotity, txtMyEval, txtEvalMe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_system_news, container, false);

        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.system_news));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        layoutNotify = (RelativeLayout)rootView.findViewById(R.id.layout_notify);
        layoutMyEval = (RelativeLayout)rootView.findViewById(R.id.layout_my_eval);
        layoutEvalMe = (RelativeLayout)rootView.findViewById(R.id.layout_eval_me);

        layoutNotify.setOnClickListener(mButtonClickListener);
        layoutMyEval.setOnClickListener(mButtonClickListener);
        layoutEvalMe.setOnClickListener(mButtonClickListener);

        txtNotity = (TextView)rootView.findViewById(R.id.txt_badge_notify);
        txtMyEval = (TextView)rootView.findViewById(R.id.txt_badge_my_eval);
        txtEvalMe = (TextView)rootView.findViewById(R.id.txt_badge_eval_me);

        showNotificationCount();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.mainActivity.loadNoticeCount();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Constants.NOTIFY_NEWS_COUNT_CHANGED));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constants.NOTIFY_NEWS_COUNT_CHANGED) {
                showNotificationCount();
            }
        }
    };

    private void showNotificationCount() {
        NoticeCountModel notice = AppConfig.getInstance().notice;
        if (notice.getMyEstimateCnt() > 0) {
            txtMyEval.setVisibility(View.VISIBLE);
            txtMyEval.setText(String.valueOf(notice.getMyEstimateCnt()));
        } else {
            txtMyEval.setVisibility(View.GONE);
        }

        if (notice.getEstimateToMeCnt() > 0) {
            txtEvalMe.setVisibility(View.VISIBLE);
            txtEvalMe.setText(String.valueOf(notice.getEstimateToMeCnt()));
        } else {
            txtEvalMe.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BaseFragmentActivity parent = (BaseFragmentActivity)getActivity();
            switch (v.getId()) {
                case R.id.layout_notify:
                    parent.showFragment(new SystemNotifyFragment(), true);
                    break;
                case R.id.layout_my_eval:
                    parent.showFragment(new MyEvalNotifyFragment(), true);
                    break;
                case R.id.layout_eval_me:
                    parent.showFragment(new EvalMeNotifyFragment(), true);
                    break;
                case R.id.btn_back:
                    parent.finish();
                    parent.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
            }
        }
    };
}
