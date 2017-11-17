package com.beijing.chengxin.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.beijing.chengxin.R;

public class BaseFragmentActivity extends FragmentActivity {

    private static final String TAG = BaseFragmentActivity.class.getName();
    public static final int RESULT_LOGOUT = 100;

    public FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        fragmentManager = getSupportFragmentManager();
    }

    public void showFragment(Fragment fragment, boolean isStack) {
        showFragment(fragment, isStack, true);
    }

    public void showFragment(Fragment fragment, boolean isStack, boolean isAnimation) {
        showFragment(fragment, isStack, isAnimation, true);
    }

    public void showFragment(Fragment fragment, boolean isStack, boolean isAnimation, boolean fromRight) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if(isAnimation) {
            if (fromRight)
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            else
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        }

        transaction.replace(R.id.fragment_container, fragment);

        if(isStack)
            transaction.addToBackStack(null);

        transaction.commit();
    }

    public void onBackActivity() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void popFragment(Fragment fragment, boolean isAnimation) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if(isAnimation)
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);

        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
    }

    public void goBack() {
        if(fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

    public void goHome() {
        if (fragmentManager.getBackStackEntryCount() > 0)
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}