package com.beijing.chengxin.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;

public class SelectSearchContentFragment extends Fragment {

	public final String TAG = SelectSearchContentFragment.class.getName();

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_select_search_content, container, false);

        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_familiar).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_enterprise).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_comedity).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_item).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_serve).setOnClickListener(mButtonClickListener);
        rootView.findViewById(R.id.btn_chengxin_number).setOnClickListener(mButtonClickListener);

        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BaseFragmentActivity parent = (BaseFragmentActivity)getActivity();
            SearchHistoryFragment fragment = new SearchHistoryFragment();
            switch (v.getId()) {
                case R.id.btn_familiar:
                    fragment.setCurrentFragmentIndex(Constants.SEARCH_IN_HOME, Constants.SEARCH_HOME_FAMILIAR);
                    parent.showFragment(fragment, true);
                    break;
                case R.id.btn_enterprise:
                    fragment.setCurrentFragmentIndex(Constants.SEARCH_IN_HOME, Constants.SEARCH_HOME_ENTERPRISE);
                    parent.showFragment(fragment, true);
                    break;
                case R.id.btn_comedity:
                    fragment.setCurrentFragmentIndex(Constants.SEARCH_IN_HOME, Constants.SEARCH_HOME_COMEDITY);
                    parent.showFragment(fragment, true);
                    break;
                case R.id.btn_item:
                    fragment.setCurrentFragmentIndex(Constants.SEARCH_IN_HOME, Constants.SEARCH_HOME_ITEM);
                    parent.showFragment(fragment, true);
                    break;
                case R.id.btn_serve:
                    fragment.setCurrentFragmentIndex(Constants.SEARCH_IN_HOME, Constants.SEARCH_HOME_SERVE);
                    parent.showFragment(fragment, true);
                    break;
                case R.id.btn_chengxin_number:
                    fragment.setCurrentFragmentIndex(Constants.SEARCH_IN_HOME, Constants.SEARCH_HOME_CODE);
                    parent.showFragment(fragment, true);
                    break;
                case R.id.btn_back:
                    parent.onBackActivity();
                    break;
            }
        }
    };
}
