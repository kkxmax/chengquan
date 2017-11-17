package com.beijing.chengxin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.adapter.HotListAdapter;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.fragment.LoginFragment;

import java.util.List;

public class PersonDetailActivity extends ParentFragmentActivity {

    public final String TAG = PersonDetailActivity.class.getName();

    private ImageButton btnShare , btnBack;
    private ToggleButton btnEval , btnComedity , btnItem , btnServe;
    private LinearLayout scrollView;
    private Button btnArrowUp;

    MyAdapter mAdapter;
    ViewPager mPager;

    EvalAdapter evalAdapter;
    ComedityAdapter comedityAdapter;
    ItemAdapter itemAdapter;
    ServeAdapter serveAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        // set title
        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.person_detail));
        btnShare = (ImageButton)findViewById(R.id.btn_share);
        btnShare.setVisibility(View.VISIBLE);

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnEval = (ToggleButton)findViewById(R.id.btn_eval);
        btnComedity = (ToggleButton)findViewById(R.id.btn_comedity);
        btnItem = (ToggleButton)findViewById(R.id.btn_item);
        btnServe = (ToggleButton)findViewById(R.id.btn_serve);


        btnBack.setOnClickListener(mClickListener);
        btnEval.setOnClickListener(mClickListener);
        btnComedity.setOnClickListener(mClickListener);
        btnItem.setOnClickListener(mClickListener);
        btnServe.setOnClickListener(mClickListener);

        scrollView = (LinearLayout)findViewById(R.id.display_scrollview);
        btnArrowUp = (Button) findViewById(R.id.btn_arrow_up);
        btnArrowUp.setOnClickListener(mClickListener);

        evalAdapter = new EvalAdapter(this, null );
        comedityAdapter = new ComedityAdapter(this , null);
        itemAdapter = new ItemAdapter(this , null);
        serveAdapter = new ServeAdapter(this , null);

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

                    case R.id.btn_comedity:
                        selectTabButton(1);
                        mPager.setCurrentItem(1);
                        break;

                    case R.id.btn_item:
                        selectTabButton(2);
                        mPager.setCurrentItem(2);
                        break;

                    case R.id.btn_serve:
                        selectTabButton(3);
                        mPager.setCurrentItem(3);
                        break;

                    case R.id.btn_arrow_up:
                        if(scrollView.getVisibility() == View.GONE){
                            AnimationSet expandAndShrink = new AnimationSet(true);
                            ScaleAnimation expand = new ScaleAnimation(
                                    1f, 1f,
                                    0f, 1f,
                                    Animation.RELATIVE_TO_PARENT, 0,
                                    Animation.RELATIVE_TO_PARENT, 0);
                            expand.setDuration(150);
                            expandAndShrink.addAnimation(expand);
                            expandAndShrink.setInterpolator(new AccelerateInterpolator(1.0f));
                            scrollView.startAnimation(expandAndShrink);
                            expandAndShrink.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {}

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    scrollView.setVisibility(View.VISIBLE);
                                    btnArrowUp.setText(getText(R.string.arrow_up));
                                    btnArrowUp.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.arrow_up), null, null, null);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {}
                            });
                        }else{
                            AnimationSet expandAndShrink = new AnimationSet(true);
                            ScaleAnimation expand = new ScaleAnimation(
                                    1f, 1f,
                                    1f, 0f,
                                    Animation.RELATIVE_TO_PARENT, 0,
                                    Animation.RELATIVE_TO_PARENT, 0);
                            expand.setDuration(150);
                            expandAndShrink.addAnimation(expand);
                            expandAndShrink.setInterpolator(new AccelerateInterpolator(1.0f));
                            scrollView.startAnimation(expandAndShrink);
                            expandAndShrink.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {}

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    scrollView.setVisibility(View.GONE);
                                    btnArrowUp.setText(getText(R.string.arrow_down));
                                    btnArrowUp.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.arrow_down) , null, null, null);}

                                @Override
                                public void onAnimationRepeat(Animation animation) {}
                            });
                        }

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
                btnComedity.setChecked(true);
                break;
            case 2:
                btnItem.setChecked(true);
                break;
            case 3:
                btnServe.setChecked(true);
                break;
        }
    }

    private void resetTabBtn(){
        btnEval.setChecked(false);
        btnComedity.setChecked(false);
        btnItem.setChecked(false);
        btnServe.setChecked(false);
    }


    public class MyAdapter extends FragmentPagerAdapter {


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ListFragment fragment1 = new ListFragment();
                    fragment1.setListAdapter(evalAdapter);
                    return fragment1;
                case 1:
                    ListFragment fragment2 = new ListFragment();
                    fragment2.setListAdapter(comedityAdapter);
                    return fragment2;
                case 2:
                    ListFragment fragment3 = new ListFragment();
                    fragment3.setListAdapter(itemAdapter);
                    return fragment3;
                default:
                    ListFragment fragment4 = new ListFragment();
                    fragment4.setListAdapter(serveAdapter);
                    return fragment4;
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eval, parent, false);
                if(position == 0)
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eval_count, parent, false);
            }

            return convertView;
        }

    }

    public class ComedityAdapter extends BaseAdapter {

        private Context mContext;
        private View mRootView;

        public ComedityAdapter(Context context, List list) {
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

    public class ItemAdapter extends BaseAdapter {

        private Context mContext;
        private View mRootView;

        public ItemAdapter(Context context, List list) {
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_serve, parent, false);

            return convertView;
        }

    }

    public class ServeAdapter extends BaseAdapter {

        private Context mContext;
        private View mRootView;

        public ServeAdapter(Context context, List list) {
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_serve, parent, false);

            return convertView;
        }

    }
}
