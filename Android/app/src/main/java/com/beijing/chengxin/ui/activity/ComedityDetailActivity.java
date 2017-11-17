package com.beijing.chengxin.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.config.Constants;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.beijing.chengxin.ui.widget.AutoScrollViewPager;
import com.beijing.chengxin.ui.widget.PageIndicator;
import com.beijing.chengxin.ui.widget.UrlImagePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

public class ComedityDetailActivity extends ParentFragmentActivity {

    public final String TAG = ComedityDetailActivity.class.getName();

    private ImageButton btnShare , btnBack , btnRate , btnNoRate , btnPhone;
    AutoScrollViewPager recommendViewPager;
    PageIndicator pageIndicator;
    UrlImagePagerAdapter recommendImageAdapter;
    List<String> listRecommendImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comedity_detail);
        // set title

        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.comedity_detail));
        btnShare = (ImageButton)findViewById(R.id.btn_share);
        btnShare.setVisibility(View.VISIBLE);

        listRecommendImageUrl = new ArrayList<String>();
        listRecommendImageUrl.add(Constants.WEB_HOST_ADDRESS);
        listRecommendImageUrl.add(Constants.WEB_HOST_ADDRESS);
        listRecommendImageUrl.add(Constants.WEB_HOST_ADDRESS);
        recommendImageAdapter = new UrlImagePagerAdapter(listRecommendImageUrl).setInfiniteLoop(true);
        recommendViewPager = (AutoScrollViewPager) findViewById(R.id.autoScrollViewPager);
        recommendViewPager.setAdapter(recommendImageAdapter);
        recommendViewPager.setInterval(5000);
        pageIndicator = (PageIndicator) findViewById(R.id.pageIndicator);
        pageIndicator.setViewPager(recommendViewPager);

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnRate = (ImageButton)findViewById(R.id.btn_rating);
        btnNoRate = (ImageButton)findViewById(R.id.btn_no_rating);
        btnPhone= (ImageButton)findViewById(R.id.btn_phone);

        btnBack.setOnClickListener(mClickListener);
        btnRate.setOnClickListener(mClickListener);
        btnNoRate.setOnClickListener(mClickListener);
        btnPhone.setOnClickListener(mClickListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        recommendViewPager.startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        recommendViewPager.stopAutoScroll();
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

                case R.id.btn_rating:
                    btnRate.setVisibility(View.INVISIBLE);
                    btnNoRate.setVisibility(View.VISIBLE);
                    break;

                case R.id.btn_no_rating:
                    btnNoRate.setVisibility(View.INVISIBLE);
                    btnRate.setVisibility(View.VISIBLE);
                    break;

                case R.id.btn_phone:

                    break;
            }
        }
    };
}
