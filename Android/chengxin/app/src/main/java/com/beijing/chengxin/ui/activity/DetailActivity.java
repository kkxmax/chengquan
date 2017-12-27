package com.beijing.chengxin.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.fragment.AccountSelectFragment;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.ComedityDetailFragment;
import com.beijing.chengxin.ui.fragment.EnterpriseDetailFragment;
import com.beijing.chengxin.ui.fragment.HotDetailFragment;
import com.beijing.chengxin.ui.fragment.ItemDetailFragment;
import com.beijing.chengxin.ui.fragment.PersonDetailFragment;
import com.beijing.chengxin.ui.fragment.ServeDetailFragment;
import com.beijing.chengxin.ui.listener.OnUserSelectListener;

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
                ((HotDetailFragment)fragment).setId(id);
                break;
            case Constants.INDEX_ENTERPRISE:
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
            case Constants.INDEX_SELECT_ACCOUNT:
            default:
                akind = getIntent().getIntExtra("akind", Constants.ACCOUNT_TYPE_PERSON);
                final int userid = getIntent().getIntExtra("userid", -1);
                fragment = new AccountSelectFragment();
                ((AccountSelectFragment)fragment).setData(akind, userid);
                ((AccountSelectFragment)fragment).setOnUserSelectListener(new OnUserSelectListener() {
                    @Override
                    public void onCancel() {
                        DetailActivity.this.onBackPressed();
                    }

                    @Override
                    public void onUserSelected(UserModel user) {
                        Intent intent=new Intent();
                        intent.putExtra("userId", user.getId());
                        if (user.getAkind() == Constants.ACCOUNT_TYPE_PERSON)
                            intent.putExtra("name", user.getRealname());
                        else
                            intent.putExtra("name", user.getEnterName());
                        intent.putExtra("code", user.getCode());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
                break;
        }

        showFragment(fragment, false, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULT_CODE_LOGIN_DUPLICATE)
            ChengxinApplication.finishActivityFromDuplicate(this);
    }

}
