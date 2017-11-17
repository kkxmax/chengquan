package com.beijing.chengxin.ui.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.MainEvalFragment;
import com.beijing.chengxin.ui.listener.OnItemClickListener;

import java.util.List;

public class HotDetailActivity extends ParentFragmentActivity {

    public final String TAG = HotDetailActivity.class.getName();

    private ImageButton btnShare , btnBack;
    private ToggleButton btnEval ,btnProfession;

    MyAdapter mAdapter;
    ViewPager mPager;

    EvalAdapter evalAdapter;
    ProfessionAdapter professionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_detail);
        // set title

        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.hot_detail));
        btnShare = (ImageButton)findViewById(R.id.btn_share);
        btnShare.setVisibility(View.VISIBLE);

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mClickListener);

        btnEval = (ToggleButton)findViewById(R.id.btn_eval);
        btnProfession = (ToggleButton)findViewById(R.id.btn_profession);

        btnEval.setOnClickListener(mClickListener);
        btnProfession.setOnClickListener(mClickListener);

        evalAdapter = new EvalAdapter(this, null );
        professionAdapter = new ProfessionAdapter(this , null);

        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                selectTabButton(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


    OnItemClickListener listItemClickListener = new OnItemClickListener() {
        @Override
        public void onListItemClick(int position, View view) {
//            Intent intent = new Intent(getActivity(), EnterpriseDetailActivity.class);
//            startActivity(intent);
        }
    };



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    onBackPressed();
                    break;
                case R.id.btn_eval:
                    selectTabButton(0);
                    mPager.setCurrentItem(0);
                    break;
                case R.id.btn_profession:
                    selectTabButton(1);
                    mPager.setCurrentItem(1);
                    break;
            }
        }
    };

    private void selectTabButton(int position){
        resetTabBtn();
        switch(position){
            case 0:
                btnEval.setChecked(true);
                break;
            case 1:
                btnProfession.setChecked(true);
                break;
        }
    }

    private void resetTabBtn(){
        btnEval.setChecked(false);
        btnProfession.setChecked(false);
    }

    public class MyAdapter extends FragmentPagerAdapter {


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ListFragment fragment1 = new ListFragment();
                    fragment1.setListAdapter(evalAdapter);
                    return fragment1;
                default:
                    ListFragment fragment2 = new ListFragment();
                    fragment2.setListAdapter(professionAdapter);
                    return fragment2;
            }
        }
    };

    public class EvalAdapter extends BaseAdapter {

        private Context mContext;
        private View mRootView;

        public EvalAdapter(Context context, List list) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return 5;//listBoxs.size();
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eval_hot_detail, parent, false);
            }

            return convertView;
        }

    }

    public class ProfessionAdapter extends BaseAdapter {

        private Context mContext;
        private View mRootView;

        public ProfessionAdapter(Context context, List list) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return 5;//listBoxs.size();
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comedity, parent, false);

            return convertView;
        }

    }

}
