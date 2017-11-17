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
import com.beijing.chengxin.ui.activity.MyErrorCorrectActivity;
import com.beijing.chengxin.ui.activity.MyWriteActivity;

public class MyErrorDetailFragment extends Fragment {

	public final String TAG = MyErrorDetailFragment.class.getName();

    private ImageButton btnBack ;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_my_error_detail, container, false);
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.error_detail));

        btnBack = (ImageButton)rootView.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mClickListener);

        return rootView;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyErrorCorrectActivity activity = (MyErrorCorrectActivity) getActivity();
            switch (v.getId()){
                case R.id.btn_back:
                    activity.goBack();
                    break;
            }
        }
    };
}
