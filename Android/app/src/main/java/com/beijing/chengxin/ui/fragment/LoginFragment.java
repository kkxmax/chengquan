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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.LoginActivity;

import static com.beijing.chengxin.ui.config.Constants.DEBUG_MODE;

public class LoginFragment extends Fragment {

	public final String TAG = LoginFragment.class.getName();

	private ToggleButton btnEye;
    private Button btnLogin, btnRegister, btnForgotPw;
    private EditText etPhoneNumber, etPw;
    private View rootView;

    String mPhoneNumber, mPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_login, container, false);

        btnLogin  = (Button)rootView.findViewById(R.id.btn_login) ;
        btnRegister  = (Button)rootView.findViewById(R.id.btn_register) ;
        btnForgotPw  = (Button)rootView.findViewById(R.id.btn_forgot_pw) ;
        btnEye     = (ToggleButton)rootView.findViewById(R.id.btn_eye) ;

        etPhoneNumber    = (EditText)rootView.findViewById(R.id.edit_phone_number);
		etPw        = (EditText)rootView.findViewById(R.id.edit_password);

        btnLogin.setOnClickListener(mButtonClickListener);
        btnRegister.setOnClickListener(mButtonClickListener);
        btnForgotPw.setOnClickListener(mButtonClickListener);
        btnEye.setOnClickListener(mButtonClickListener);

        // test code
        if (DEBUG_MODE) {
            etPhoneNumber.setText("12345678910");
            etPw.setText("12345678910");
        }

        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    mPhoneNumber = etPhoneNumber.getText().toString().trim();
                    mPassword = etPw.getText().toString().trim();

                    if (mPhoneNumber.length() < 11) {
                        Toast.makeText(getActivity(), getString(R.string.err_phone_number_incorrect), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (mPassword.length() < 6 || mPassword.length() > 20) {
                        Toast.makeText(getActivity(), getString(R.string.err_password_length), Toast.LENGTH_LONG).show();
                        return;
                    }
                    ((LoginActivity)getActivity()).toMainActivity();
                    break;
                case R.id.btn_register:
                    ((LoginActivity)getActivity()).showFragment(new RegisterFragment(), true, true);
                    break;
                case R.id.btn_forgot_pw:
                    ((LoginActivity)getActivity()).showFragment(new ForgotPasswordFragment(), true, true);
                    break;
                case R.id.btn_eye:
                    if (etPw.getInputType() == InputType.TYPE_CLASS_TEXT) {
                        etPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        btnEye.setChecked(false);
                    } else {
                        etPw.setInputType(InputType.TYPE_CLASS_TEXT);
                        btnEye.setChecked(true);
                    }
                    break;
            }
        }
    };
}
