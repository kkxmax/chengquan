package com.beijing.chengxin.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.LoginModel;
import com.beijing.chengxin.network.model.VerifyCodeModel;
import com.beijing.chengxin.ui.activity.LoginActivity;
import com.beijing.chengxin.ui.widget.Utils;

import org.w3c.dom.Text;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.beijing.chengxin.config.Constants.DEBUG_MODE;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class RegisterFragment extends Fragment {

    public final String TAG = RegisterFragment.class.getName();
    private ImageButton btnBack;
    private Button btnRegister, btnRequest;
    private EditText etPhoneNumber , etReq , etCertNumber , etPw , etConfirmPw;
    private TextView txtAgreement;
    private View rootView;
    private CheckBox checkAccept;

    private SyncInfo info;
    private ScheduledExecutorService mTimerThread = null;
    private int descCounter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.register));

        btnRegister = (Button)rootView.findViewById(R.id.btn_register_cert);
        btnRequest  = (Button)rootView.findViewById(R.id.btn_request_cert) ;
        btnBack = (ImageButton)rootView.findViewById(R.id.btn_back);
        checkAccept = (CheckBox)rootView.findViewById(R.id.check_accept);

        etPhoneNumber = (EditText)rootView.findViewById(R.id.edit_phone_number);
        etReq = (EditText)rootView.findViewById(R.id.edit_request);
        etCertNumber = (EditText)rootView.findViewById(R.id.edit_cert_number);
        etPw = (EditText)rootView.findViewById(R.id.edit_password);
        etConfirmPw = (EditText)rootView.findViewById(R.id.edit_confirm_pw);
        txtAgreement = (TextView)rootView.findViewById(R.id.txt_agreement);

        btnRegister.setOnClickListener(mButtonClickListener);
        btnRequest.setOnClickListener(mButtonClickListener);
        btnBack.setOnClickListener(mButtonClickListener);
        txtAgreement.setOnClickListener(mButtonClickListener);

        info = new SyncInfo(getActivity());

        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String pn = etPhoneNumber.getText().toString().trim();
            String rcode = etReq.getText().toString().trim();
            String certNumber = etCertNumber.getText().toString().trim();
            String pw = etPw.getText().toString().trim();
            String cpw = etConfirmPw.getText().toString().trim();
            boolean accept = checkAccept.isChecked();

            switch (v.getId()) {
                case R.id.btn_register_cert:
                    String errMsg = "";
                    if (pn.length() == 0 || pw.length() == 0)
                        errMsg = getString(R.string.err_phone_number_password_not_space);
                    else if (pn.length() < 11)
                        errMsg = getString(R.string.err_phone_number_incorrect);
//                    else if (rcode.length() == 0)
//                        errMsg = getString(R.string.err_request_code_not_space);
                    else if (pw.length() < 6 || pw.length() > 20)
                        errMsg = getString(R.string.err_password_length);
                    else if (!pw.equals(cpw))
                        errMsg = getString(R.string.err_confirm_incorrect);
                    else if (certNumber.length() == 0)
                        errMsg = getString(R.string.err_phone_number_cert_number_not_space);
                    else if (!accept)
                        errMsg = getString(R.string.err_read_agreement);
                    if (errMsg.length() > 0)
                    {
                        Toast.makeText(getActivity(), errMsg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    new RegisterAsync().execute(pn, pw, rcode, certNumber);
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
                    LoginActivity loginActivity = (LoginActivity)getActivity();
                    loginActivity.goBack();
                    break;
                case R.id.txt_agreement:
                    ((BaseFragmentActivity)getActivity()).showFragment(new AgreementFragment(), true);
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

    class RegisterAsync extends AsyncTask<String, String, LoginModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected LoginModel doInBackground(String... strs) {
            return info.syncRegister(strs[0], strs[1], strs[2], strs[3]);
        }
        @Override
        protected void onPostExecute(LoginModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    SessionInstance.initialize(getActivity(), result);
                    ((LoginActivity)getActivity()).showFragment(new RegisterCompleteFragment(), false, true);
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
