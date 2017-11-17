package com.beijing.chengxin.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beijing.chengxin.R;

public class SystemNewsFragment extends Fragment {

	public final String TAG = SystemNewsFragment.class.getName();

    private View rootView;
    RelativeLayout layoutNotify, layoutMyEval, layoutEvalMe;
    TextView txtNotity, txtMyEval, txtEvalMe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_system_news, container, false);

        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.system_news));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        layoutNotify = (RelativeLayout)rootView.findViewById(R.id.layout_notify);
        layoutMyEval = (RelativeLayout)rootView.findViewById(R.id.layout_my_eval);
        layoutEvalMe = (RelativeLayout)rootView.findViewById(R.id.layout_eval_me);

        layoutNotify.setOnClickListener(mButtonClickListener);
        layoutMyEval.setOnClickListener(mButtonClickListener);
        layoutEvalMe.setOnClickListener(mButtonClickListener);

        txtNotity = (TextView)rootView.findViewById(R.id.txt_badge_notify);
        txtMyEval = (TextView)rootView.findViewById(R.id.txt_badge_my_eval);
        txtEvalMe = (TextView)rootView.findViewById(R.id.txt_badge_eval_me);

        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BaseFragmentActivity parent = (BaseFragmentActivity)getActivity();
            switch (v.getId()) {
                case R.id.layout_notify:
                    parent.showFragment(new SystemNotifyFragment(), true);
                    break;
                case R.id.layout_my_eval:
                    parent.showFragment(new MyEvalNotifyFragment(), true);
                    break;
                case R.id.layout_eval_me:
                    parent.showFragment(new EvalMeNotifyFragment(), true);
                    break;
                case R.id.btn_back:
                    parent.finish();
                    parent.overridePendingTransition(R.anim.fade_in, R.anim.slide_out_up);
                    break;
            }
        }
    };
}
