package com.beijing.chengxin.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.SearchHistoryFragment;
import com.beijing.chengxin.ui.fragment.SelectSearchContentFragment;

public class SearchActivity extends BaseFragmentActivity {

    public final String TAG = SearchActivity.class.getName();
    public int mainFragmentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainFragmentIndex = getIntent().getIntExtra(Constants.SEARCH_KEYWORD, 0);

        Fragment fragment;

        if (mainFragmentIndex == Constants.SEARCH_IN_HOME) {
            fragment = new SelectSearchContentFragment();
            showFragment(fragment, false, false);
        } else if (mainFragmentIndex == Constants.SEARCH_IN_EVAL) {
            fragment = new SearchHistoryFragment();
            ((SearchHistoryFragment)fragment).setCurrentFragmentIndex(Constants.SEARCH_IN_EVAL, 0);
            showFragment(fragment, false, false);
        }
//        showFragment(fragment, false, false);
    }
}
