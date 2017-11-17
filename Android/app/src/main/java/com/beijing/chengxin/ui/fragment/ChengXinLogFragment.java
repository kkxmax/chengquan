package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.ChengxinLogActivity;
import com.beijing.chengxin.ui.activity.ChengxinReportActivity;
import com.beijing.chengxin.ui.view.ChengXinRateView;

import java.util.List;

public class ChengXinLogFragment extends Fragment {

	public final String TAG = ChengXinLogFragment.class.getName();

    private View rootView;
    ListView listView;
    LogListAdapter listAdapter;
    ChengXinRateView rate_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chengxin_log, container, false);
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.chengxin_log));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_rule).setOnClickListener(mButtonClickListener);

        rate_view = (ChengXinRateView)rootView.findViewById(R.id.rate_view);
        rate_view.setRateValue(80);
        rate_view.start();

        listView = (ListView)rootView.findViewById(R.id.listView);
        listAdapter = new LogListAdapter(getActivity(), null, 0);
        listView.setAdapter(listAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ChengxinLogActivity parent = (ChengxinLogActivity)getActivity();
            switch (v.getId()) {
                case R.id.btn_rule:
                    parent.showFragment(new ChengXinRuleFragment(), true);
                    break;
                case R.id.btn_back:
                    parent.onBackActivity();
                    break;
            }
        }
    };

    public class LogListAdapter extends BaseAdapter {
        //    private List<PlayingItemResponse> listBoxs;
        private Context mContext;
        private View mRootView;

        public LogListAdapter(Context context, List list, int listType) {
//        listBoxs = list;
            mContext = context;
        }

        @Override
        public int getCount() {
            return 10;//listBoxs.size();
        }

        @Override
        public Object getItem(int position) {
            return 1;// listBoxs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false);

            return convertView;
        }

    }
}
