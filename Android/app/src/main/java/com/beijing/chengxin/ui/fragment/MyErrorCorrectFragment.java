package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.MyErrorCorrectActivity;
import com.beijing.chengxin.ui.activity.MyWriteActivity;
import com.beijing.chengxin.ui.listener.OnItemClickListener;

import java.util.List;

public class MyErrorCorrectFragment extends Fragment {

	public final String TAG = MyErrorCorrectFragment.class.getName();

    private View rootView;
    private static Context mContext;
    ToggleButton btnProgress, btnSuccess, btnFail;
    int mType;

    ViewPager mPager;
    PageAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_my_error_correct, container, false);

        mContext = getContext();

        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.my_error_correct));

        btnProgress = (ToggleButton)rootView.findViewById(R.id.btn_progress);
        btnSuccess = (ToggleButton)rootView.findViewById(R.id.btn_success);
        btnFail = (ToggleButton)rootView.findViewById(R.id.btn_fail);

        btnProgress.setOnClickListener(mButtonClickListener);
        btnSuccess.setOnClickListener(mButtonClickListener);
        btnFail.setOnClickListener(mButtonClickListener);

        mAdapter = new PageAdapter(getActivity().getSupportFragmentManager());
        mPager = (ViewPager)rootView.findViewById(R.id.viewPager);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                selectTopTabButton(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mType = 0;

        return rootView;
    }

    public class PageAdapter extends FragmentPagerAdapter {
        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            ListFragment fragment = new ListFragment();
            WriteListAdapter adapter = new WriteListAdapter(mContext, null, position);
            adapter.setOnItemClickListener(listItemClickListener);
            fragment.setListAdapter(adapter);


            return fragment;
        }
    };

    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
            MyErrorCorrectActivity parent = (MyErrorCorrectActivity)getActivity();
            if (mType == 0) {
                MyErrorDetailFragment fragment = new MyErrorDetailFragment();
                parent.showFragment(fragment, true);
            }
            else if (mType == 1) {

            }
            else if (mType == 2) {

            }
        }
    };

    public class WriteListAdapter extends BaseAdapter {
        private Context mContext;
        private View mRootView;
        private int mType;

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        public WriteListAdapter(Context context, List list, int listType) {
//        listBoxs = list;
            mContext = context;
            mType = listType;
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
            if (convertView == null) {
                if (mType == 0)
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_error_correct, parent, false);
                else
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_error_correct, parent, false);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onListItemClick(position, v);
                }
            });

            return convertView;
        }

    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyErrorCorrectActivity parent = (MyErrorCorrectActivity)getActivity();
            switch (v.getId()) {
                case R.id.btn_progress:
                    selectTopTabButton(0);
                    mPager.setCurrentItem(0);
                    break;
                case R.id.btn_success:
                    selectTopTabButton(1);
                    mPager.setCurrentItem(1);
                    break;
                case R.id.btn_fail:
                    selectTopTabButton(2);
                    mPager.setCurrentItem(2);
                    break;
                case R.id.btn_back:
                    parent.onBackActivity();
                    break;
            }
        }
    };

    private void selectTopTabButton(int position) {
        btnProgress.setChecked(false);
        btnSuccess.setChecked(false);
        btnFail.setChecked(false);
        mType = position;
        switch (position) {
            case 0:
                btnProgress.setChecked(true);
                break;
            case 1:
                btnSuccess.setChecked(true);
                break;
            case 2:
                btnFail.setChecked(true);
                break;
        }
    }
}
