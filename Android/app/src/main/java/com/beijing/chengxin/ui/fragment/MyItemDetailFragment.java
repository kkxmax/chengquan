package com.beijing.chengxin.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.MyWriteActivity;
import com.beijing.chengxin.ui.config.Constants;
import com.beijing.chengxin.ui.widget.AutoScrollViewPager;
import com.beijing.chengxin.ui.widget.PageIndicator;
import com.beijing.chengxin.ui.widget.UrlImagePagerAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyItemDetailFragment extends Fragment {

	public final String TAG = MyItemDetailFragment.class.getName();

    private ImageButton btnBack ;
    private Button btnStatus;
    private TextView txtStatus;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_my_item_detail, container, false);
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.item_detail));

        btnBack = (ImageButton)rootView.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mClickListener);

        btnStatus = (Button)rootView.findViewById(R.id.btn_status_item);
        btnStatus.setOnClickListener(mClickListener);

        txtStatus = (TextView)rootView.findViewById(R.id.txt_item_status);

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
                case R.id.btn_status_item:
                    if(btnStatus.getText().equals(getText(R.string.raise_comedity))){
                        btnStatus.setText(getText(R.string.down_comedity));
                        txtStatus.setText(getText(R.string.already_raise_comedity));
                    }else{
                        btnStatus.setText(getText(R.string.raise_comedity));
                        txtStatus.setText(getText(R.string.already_down_comedity));
                    }
                    break;
            }
        }
    };
}
