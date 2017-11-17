package com.beijing.chengxin.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.LoginModel;
import com.beijing.chengxin.ui.activity.LoginActivity;
import com.beijing.chengxin.ui.widget.Utils;

import static com.beijing.chengxin.config.Constants.DEBUG_MODE;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class LoginFragment extends Fragment {

	public final String TAG = LoginFragment.class.getName();

	private ToggleButton btnEye;
    private Button btnLogin, btnRegister, btnForgotPw;
    private EditText etPhoneNumber, etPw;
    private View rootView;

    String mPhoneNumber, mPassword;

    SyncInfo info;

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
            etPhoneNumber.setText("19135411632");
            etPw.setText("123456");
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        info = new SyncInfo(getActivity());
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
                    new LoginAsync().execute(mPhoneNumber, mPassword);
                    break;
                case R.id.btn_register:
                    ((LoginActivity)getActivity()).showFragment(new RegisterFragment(), true, true);
                    break;
                case R.id.btn_forgot_pw:
                    ForgotPasswordFragment fragment = new ForgotPasswordFragment();
                    fragment.setMobile(etPhoneNumber.getText().toString().trim());
                    ((LoginActivity)getActivity()).showFragment(fragment, true, true);
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

    class LoginAsync extends AsyncTask<String, String, LoginModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected LoginModel doInBackground(String... strs) {
            return info.syncLogin(strs[0], strs[1]);
        }
        @Override
        protected void onPostExecute(LoginModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    ChengxinApplication.LOGIN_MOBILE = mPhoneNumber;
                    ChengxinApplication.LOGIN_PASSWORD = mPassword;
                    SessionInstance.initialize(getActivity(), result);
                    ((LoginActivity)getActivity()).toMainActivity();
                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.error_login_failed), Toast.LENGTH_LONG).show();
            }
            Utils.disappearProgressDialog();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Utils.disappearProgressDialog();
        }
    }
}
