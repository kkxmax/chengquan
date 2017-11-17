package com.beijing.chengxin.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.HotDetailActivity;
import com.beijing.chengxin.ui.activity.MakeEvaluationActivity;
import com.beijing.chengxin.ui.adapter.HotListAdapter;

public class MainHotFragment extends Fragment {

	public final String TAG = MainHotFragment.class.getName();
    private View rootView;
    ListView listView;
    HotListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_main_hot, container, false);

        listView = (ListView)rootView.findViewById(R.id.listView);
        listAdapter = new HotListAdapter(getActivity(), null, 0);
        listView.setAdapter(listAdapter);
        listAdapter.setOnListItemClickListener(new HotListAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(getActivity(), HotDetailActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    break;
            }
        }
    };
}
