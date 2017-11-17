package com.beijing.chengxin.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.LoginActivity;

public class RegisterCompleteFragment extends Fragment {

    public final String TAG = RegisterCompleteFragment.class.getName();

    private ImageButton btnBack;
    private Button btnRegisterCert , btnSkip;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register_complete, container, false);

        btnRegisterCert = (Button)rootView.findViewById(R.id.btn_register_cert) ;
        btnSkip = (Button)rootView.findViewById(R.id.btn_register_skip);
        btnBack = (ImageButton)rootView.findViewById(R.id.btn_back);

        btnRegisterCert.setOnClickListener(mButtonClickListener);
        btnSkip.setOnClickListener(mButtonClickListener);
        btnBack.setOnClickListener(mButtonClickListener);

        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_register_cert:
                    ((LoginActivity)getActivity()).goHome();
                    break;
                case R.id.btn_register_skip:
                    Toast.makeText(getActivity() , "跳过" , Toast.LENGTH_LONG).show();
                    break;
                case R.id.btn_back:
                    LoginActivity registerLoginActivity = (LoginActivity)getActivity();
                    registerLoginActivity.goBack();
                    break;
            }
        }
    };
}
