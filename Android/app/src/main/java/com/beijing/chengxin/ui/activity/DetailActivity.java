package com.beijing.chengxin.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.ComedityDetailFragment;
import com.beijing.chengxin.ui.fragment.EnterpriseDetailFragment;
import com.beijing.chengxin.ui.fragment.HotDetailFragment;
import com.beijing.chengxin.ui.fragment.ItemDetailFragment;
import com.beijing.chengxin.ui.fragment.PersonDetailFragment;
import com.beijing.chengxin.ui.fragment.ServeDetailFragment;

public class DetailActivity extends BaseFragmentActivity {
    public final String TAG = DetailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int fragmentType = getIntent().getIntExtra("type", Constants.INDEX_ENTERPRISE);
        int id = getIntent().getIntExtra("id", 0);
        Fragment fragment;

        switch (fragmentType) {
            case Constants.INDEX_COMEDITY:
                fragment = new ComedityDetailFragment();
                ((ComedityDetailFragment)fragment).setId(id);
                break;
            case Constants.INDEX_ITEM:
                fragment = new ItemDetailFragment();
                ((ItemDetailFragment)fragment).setItem(AppConfig.getInstance().currentItem);
                break;
            case Constants.INDEX_SERVE:
                fragment = new ServeDetailFragment();
                ((ServeDetailFragment)fragment).setItem(AppConfig.getInstance().currentServe);
                break;
            case Constants.INDEX_HOT:
                fragment = new HotDetailFragment();
                ((HotDetailFragment)fragment).setItem(AppConfig.getInstance().currentHot);
                break;
            case Constants.INDEX_ENTERPRISE:
            default:
                int akind = getIntent().getIntExtra("akind", Constants.ACCOUNT_TYPE_ENTERPRISE);
                if (akind == Constants.ACCOUNT_TYPE_ENTERPRISE) {
                    fragment = new EnterpriseDetailFragment();
                    ((EnterpriseDetailFragment)fragment).setId(id);
                }
                else {
                    fragment = new PersonDetailFragment();
                    ((PersonDetailFragment)fragment).setId(id);
                }
                break;
        }

        showFragment(fragment, false, false);
    }
}
