package com.beijing.chengxin.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.MySettingActivity;

public class ChangePasswordFragment extends Fragment {

	public final String TAG = ChangePasswordFragment.class.getName();

	private ImageButton btnBack;
    private Button btnComplet;

    private EditText etOldPw, etNewPw, etConfirmPw;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.change_password));

        btnComplet  = (Button)rootView.findViewById(R.id.btn_complete) ;
        btnBack     = (ImageButton)rootView.findViewById(R.id.btn_back) ;

        etOldPw = (EditText)rootView.findViewById(R.id.edit_old_pw);
        etNewPw = (EditText)rootView.findViewById(R.id.edit_new_pw);
        etConfirmPw = (EditText)rootView.findViewById(R.id.edit_confirm_pw);

        btnComplet.setOnClickListener(mButtonClickListener);
        btnBack.setOnClickListener(mButtonClickListener);

        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BaseFragmentActivity parent = (BaseFragmentActivity)getActivity();
            switch (v.getId()) {
                case R.id.btn_complete:
//
                    parent.goBack();
                    break;
                case R.id.btn_back:
                    parent.goBack();
                    break;
            }
        }
    };
}
