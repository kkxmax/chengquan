package com.beijing.chengxin.ui.fragment;

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

import org.w3c.dom.Text;

public class RegisterFragment extends Fragment {

    public final String TAG = RegisterFragment.class.getName();
    private ImageButton btnBack;
    private Button btnRegister ;
    private EditText etPhoneNumber , etReq , etVerify , etPw , etConfirmPw;
    private View rootView;

    String mPhoneNumber, mRequest , mVerify , mConfirmPw ,  mPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.register));

        btnRegister = (Button)rootView.findViewById(R.id.btn_register_cert);
        btnBack = (ImageButton)rootView.findViewById(R.id.btn_back);

        etPhoneNumber = (EditText)rootView.findViewById(R.id.edit_phone_number);
        etReq = (EditText)rootView.findViewById(R.id.edit_request);
        etVerify = (EditText)rootView.findViewById(R.id.edit_read_accept_licence);
        etPw = (EditText)rootView.findViewById(R.id.edit_password);
        etConfirmPw = (EditText)rootView.findViewById(R.id.edit_confirm_pw);

        btnRegister.setOnClickListener(mButtonClickListener);
        btnBack.setOnClickListener(mButtonClickListener);
        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_register_cert:
                    mPhoneNumber = etPhoneNumber.getText().toString().trim();
                    mRequest = etReq.getText().toString().trim();
                    mVerify= etVerify.getText().toString().trim();
                    mConfirmPw= etConfirmPw.getText().toString().trim();
                    mPassword = etPw.getText().toString().trim();

                    if (mPhoneNumber.length() < 11) {
                        Toast.makeText(getActivity(), getString(R.string.err_phone_number_incorrect), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (mPassword.length() < 6 || mPassword.length() > 20) {
                        Toast.makeText(getActivity(), getString(R.string.err_password_length), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (mConfirmPw.length() < 6 || mConfirmPw.length() > 20) {
                        Toast.makeText(getActivity(), getString(R.string.err_confirm_length), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(!mPassword.equals(mConfirmPw)){
                        Toast.makeText(getActivity(), getString(R.string.err_confirm_incorrect), Toast.LENGTH_LONG).show();
                        return;
                    }
                    ((LoginActivity)getActivity()).showFragment(new RegisterCompleteFragment(), true, true);
                    break;
                case R.id.btn_back:
                    LoginActivity loginActivity = (LoginActivity)getActivity();
                    loginActivity.goBack();
                    break;
            }
        }
    };
}
