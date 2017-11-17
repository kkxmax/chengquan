package com.beijing.chengxin.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.MyWriteActivity;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.ui.widget.AutoScrollViewPager;
import com.beijing.chengxin.ui.widget.PageIndicator;
import com.beijing.chengxin.ui.widget.UrlImagePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyComedityDetailFragment extends Fragment {

	public final String TAG = MyComedityDetailFragment.class.getName();

    private ImageButton btnShare , btnBack ;
    AutoScrollViewPager recommendViewPager;
    PageIndicator pageIndicator;
    UrlImagePagerAdapter recommendImageAdapter;
    List<String> listRecommendImageUrl;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_my_comedity_detail, container, false);
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.comedity_detail));

        listRecommendImageUrl = new ArrayList<String>();
        listRecommendImageUrl.add(Constants.API_ADDR);
        listRecommendImageUrl.add(Constants.API_ADDR);
        listRecommendImageUrl.add(Constants.API_ADDR);
        recommendImageAdapter = new UrlImagePagerAdapter(listRecommendImageUrl).setInfiniteLoop(true);
        recommendViewPager = (AutoScrollViewPager) rootView.findViewById(R.id.autoScrollViewPager);
        recommendViewPager.setAdapter(recommendImageAdapter);
        recommendViewPager.setInterval(5000);
        pageIndicator = (PageIndicator) rootView.findViewById(R.id.pageIndicator);
        pageIndicator.setViewPager(recommendViewPager);

        btnBack = (ImageButton)rootView.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mClickListener);

        return rootView;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyWriteActivity activity = (MyWriteActivity) getActivity();
            switch (v.getId()){
                case R.id.btn_back:
                    activity.goBack();
                    break;
            }
        }
    };
}
