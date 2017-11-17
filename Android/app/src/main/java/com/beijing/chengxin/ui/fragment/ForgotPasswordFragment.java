package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.LoginActivity;

public class ForgotPasswordFragment extends Fragment {

	public final String TAG = ForgotPasswordFragment.class.getName();

	private ImageButton btnBack;
    private Button btnComplet, btnRequest;

    private EditText etPhoneNumber, etCertNumber, etNewPw, etConfirmPw;
    private View rootView;

    String mPhoneNumber, mPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.forgot_password_title));

        btnComplet  = (Button)rootView.findViewById(R.id.btn_complete) ;
        btnRequest  = (Button)rootView.findViewById(R.id.btn_request_cert) ;
        btnBack     = (ImageButton)rootView.findViewById(R.id.btn_back) ;

        etPhoneNumber = (EditText)rootView.findViewById(R.id.edit_phone_number);
        etCertNumber = (EditText)rootView.findViewById(R.id.edit_cert_number);
        etNewPw = (EditText)rootView.findViewById(R.id.edit_new_pw);
        etConfirmPw = (EditText)rootView.findViewById(R.id.edit_confirm_pw);

        btnComplet.setOnClickListener(mButtonClickListener);
        btnRequest.setOnClickListener(mButtonClickListener);
        btnBack.setOnClickListener(mButtonClickListener);

        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_complete:
//                    mPhoneNumber = etPhoneNumber.getText().toString().trim();
//                    mPassword = etPw.getText().toString().trim();
//
//                    if (mPhoneNumber.length() < 11) {
//                        Toast.makeText(getActivity(), getString(R.string.err_phone_number_incorrect), Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    if (mPassword.length() < 6 || mPassword.length() > 20) {
//                        Toast.makeText(getActivity(), getString(R.string.err_password_length), Toast.LENGTH_LONG).show();
//                        return;
//                    }
                    ((LoginActivity)getActivity()).goBack();
                    break;
                case R.id.btn_request_cert:
                    break;
                case R.id.btn_back:
                    LoginActivity parent = (LoginActivity)getActivity();
                    parent.goBack();
                    break;
            }
        }
    };
}
