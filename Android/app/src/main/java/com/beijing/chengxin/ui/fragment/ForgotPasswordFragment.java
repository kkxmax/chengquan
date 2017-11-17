package com.beijing.chengxin.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.VerifyCodeModel;
import com.beijing.chengxin.ui.activity.LoginActivity;
import com.beijing.chengxin.ui.widget.Utils;

import java.security.PublicKey;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.beijing.chengxin.config.Constants.DEBUG_MODE;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class ForgotPasswordFragment extends Fragment {

	public final String TAG = ForgotPasswordFragment.class.getName();

	private ImageButton btnBack;
    private Button btnComplet, btnRequest;

    private EditText etPhoneNumber, etCertNumber, etNewPw, etConfirmPw;
    private View rootView;

    private SyncInfo info;
    private ScheduledExecutorService mTimerThread = null;
    private int descCounter;
    String mobileNumber;

    public void setMobile(String mobile) {
        mobileNumber = mobile;
    }

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

        if (DEBUG_MODE) {
            etPhoneNumber.setText("19135411631");
        }

        if (mobileNumber != null && !mobileNumber.equals(""))
            etPhoneNumber.setText(mobileNumber);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        info = new SyncInfo(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTimerThread != null)  {
            mTimerThread.shutdown();
            mTimerThread = null;
        }
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String pn = etPhoneNumber.getText().toString().trim();
            String pw = etNewPw.getText().toString().trim();
            String cpw = etConfirmPw.getText().toString().trim();
            String certNumber = etCertNumber.getText().toString().trim();

            switch (v.getId()) {
                case R.id.btn_complete:
                    String errMsg = "";
                    if (pn.length() == 0 || pw.length() == 0)
                        errMsg = getString(R.string.err_phone_number_password_not_space);
                    else if (pn.length() < 11)
                        errMsg = getString(R.string.err_phone_number_incorrect);
                    else if (pw.length() < 6 || pw.length() > 20)
                        errMsg = getString(R.string.err_password_length);
                    else if (!pw.equals(cpw))
                        errMsg = getString(R.string.err_confirm_incorrect);
                    else if (certNumber.length() == 0)
                        errMsg = getString(R.string.err_phone_number_cert_number_not_space);
                    if (errMsg.length() > 0)
                    {
                        Toast.makeText(getActivity(), errMsg, Toast.LENGTH_LONG).show();
                        return;
                    }
                    new ForgotPasswordAsync().execute(pn, certNumber, pw);
                    break;
                case R.id.btn_request_cert:
                    if (pn.length() == 0)
                    {
                        Toast.makeText(getActivity(), getString(R.string.err_phone_number_not_space), Toast.LENGTH_LONG).show();
                        return;
                    }
                    new CertNumberAsync().execute(pn);
                    break;
                case R.id.btn_back:
                    LoginActivity parent = (LoginActivity)getActivity();
                    parent.goBack();
                    break;
            }
        }
    };

    class CertNumberAsync extends AsyncTask<String, String, VerifyCodeModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected VerifyCodeModel doInBackground(String... strs) {
            return info.syncVerifycode(strs[0]);
        }
        @Override
        protected void onPostExecute(VerifyCodeModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    Toast.makeText(getActivity(), getString(R.string.send_cert_number_already), Toast.LENGTH_LONG).show();
                    if (DEBUG_MODE)
                        etCertNumber.setText(result.getVerifyCode());
                    else {
                        btnRequest.setEnabled(false);
                        descCounter = 60;
                        mTimerThread = Executors.newSingleThreadScheduledExecutor();
                        mTimerThread.scheduleWithFixedDelay(new Runnable() {
                            @Override
                            public void run() {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (descCounter > 0) {
                                            btnRequest.setText(String.format("%d秒可重发", descCounter));
                                            descCounter --;
                                        } else {
                                            btnRequest.setEnabled(true);
                                            btnRequest.setText(getString(R.string.get_cert_number));
                                            mTimerThread.shutdown();
                                            mTimerThread = null;
                                        }
                                    }
                                });
                            }
                        }, 0, 1, TimeUnit.SECONDS);
                    }

                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
            }
            Utils.disappearProgressDialog();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Utils.disappearProgressDialog();
        }
    }

    class ForgotPasswordAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncForgotPassword(strs[0], strs[1], strs[2]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    Toast.makeText(getActivity(), getString(R.string.password_changed_success), Toast.LENGTH_LONG).show();
                    ((LoginActivity)getActivity()).goBack();
                } else {
                    Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
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
