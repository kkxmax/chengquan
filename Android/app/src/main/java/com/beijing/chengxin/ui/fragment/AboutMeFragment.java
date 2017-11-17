package com.beijing.chengxin.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.MySettingActivity;

public class AboutMeFragment extends Fragment {

	public final String TAG = AboutMeFragment.class.getName();

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_about_me, container, false);

        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.about_me));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    BaseFragmentActivity parent = (BaseFragmentActivity)getActivity();
                    parent.goBack();
                    break;
            }
        }
    };
}
