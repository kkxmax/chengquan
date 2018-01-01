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
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
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
    private Button btnLogin, btnRegister, btnForgotPw, btnPhoneDelete;
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
        btnPhoneDelete = (Button)rootView.findViewById(R.id.btn_phone_delete);

        etPhoneNumber    = (EditText)rootView.findViewById(R.id.edit_phone_number);
		etPw        = (EditText)rootView.findViewById(R.id.edit_password);

        btnLogin.setOnClickListener(mButtonClickListener);
        btnRegister.setOnClickListener(mButtonClickListener);
        btnForgotPw.setOnClickListener(mButtonClickListener);
        btnEye.setOnClickListener(mButtonClickListener);
        btnPhoneDelete.setOnClickListener(mButtonClickListener);

        etPhoneNumber.setText(AppConfig.getLoginMobile());
        etPw.setText(AppConfig.getLoginPwd());

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        info = new SyncInfo(getActivity());

//        boolean is_auto_login = getActivity().getIntent().getBooleanExtra(Constants.IS_AUTO_LOGIN, true);
//        if (is_auto_login && AppConfig.getLoginMobile().length() > 0 && AppConfig.getLoginPwd().length() > 0) {
//            new LoginAsync().execute(AppConfig.getLoginMobile(), AppConfig.getLoginPwd());
//        }
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    mPhoneNumber = etPhoneNumber.getText().toString().trim();
                    mPassword = etPw.getText().toString();
                    String errMsg = "";
                    if (mPhoneNumber.length() == 0) {
                        errMsg = getString(R.string.err_phone_number_password_not_space);
                    } else if (mPhoneNumber.length() < 11) {
                        errMsg = getString(R.string.err_phone_number_incorrect);
                    } else if (mPassword.length() == 0) {
                        errMsg = getString(R.string.err_phone_number_password_not_space);
//                    } else if (mPassword.length() < 6 || mPassword.length() > 20) {
//                        errMsg = getString(R.string.err_password_length);
                    }
                    if (!errMsg.equals("")) {
                        Toast.makeText(getActivity(), errMsg, Toast.LENGTH_LONG).show();
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
                case R.id.btn_phone_delete:
                    etPhoneNumber.setText("");
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
            LoginModel result =  info.syncLogin(strs[0], strs[1]);
            if (result.isValid()) {
                if (result.getRetCode() == ERROR_OK) {
                    AppConfig.setLoginMobile(strs[0]);
                    AppConfig.setLoginPwd(strs[1]);
                    ChengxinApplication.LOGIN_MOBILE = strs[0];
                    ChengxinApplication.LOGIN_PASSWORD = strs[1];
                }
            }
            return result;
        }
        @Override
        protected void onPostExecute(LoginModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    SessionInstance.clearInstance();
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
