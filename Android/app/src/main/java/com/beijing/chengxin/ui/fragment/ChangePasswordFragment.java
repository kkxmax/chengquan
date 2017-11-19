package com.beijing.chengxin.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.beijing.chengxin.ui.activity.MySettingActivity;
import com.beijing.chengxin.ui.widget.Utils;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

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
                    if (isInvalide()) {
                        sendDataTask(etOldPw.getText().toString().trim(), etNewPw.getText().toString().trim());
                    }
                    break;
                case R.id.btn_back:
                    parent.goBack();
                    break;
            }
        }
    };

    private void sendDataTask(final String oldPwd, final String newPwd) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(getContext());
            }
            @Override
            protected Object doInBackground(Object... params) {
                return new SyncInfo(getContext()).syncResetPassword(oldPwd, newPwd);
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                BaseModel result = (BaseModel) o;
                if (result.isValid()) {
                    if (result.getRetCode() == ERROR_OK) {
                        Toast.makeText(getContext(), R.string.msg_success_submit, Toast.LENGTH_SHORT).show();
                        ((BaseFragmentActivity)getActivity()).goBack();
                    } else {
                        Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.err_server), Toast.LENGTH_SHORT).show();
                }
                Utils.disappearProgressDialog();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute();
    }

    private boolean isInvalide() {
        String oldPwd = etOldPw.getText().toString().trim();
        String newPwd = etNewPw.getText().toString().trim();
        String confirmPwd = etConfirmPw.getText().toString().trim();
        if (oldPwd.length() == 0) {
            Toast.makeText(getContext(), R.string.err_old_password_empty, Toast.LENGTH_SHORT).show();
            etOldPw.requestFocus();
            return false;
        }
        if (newPwd.length() < 6 || newPwd.length() > 20) {
            Toast.makeText(getContext(), R.string.err_password_length, Toast.LENGTH_SHORT).show();
            etNewPw.requestFocus();
            return false;
        }
        if (confirmPwd.length() < 6 || confirmPwd.length() > 20) {
            Toast.makeText(getContext(), R.string.err_password_length, Toast.LENGTH_SHORT).show();
            etConfirmPw.requestFocus();
            return false;
        }
        if (!newPwd.equals(confirmPwd)) {
            Toast.makeText(getContext(), R.string.err_confirm_incorrect, Toast.LENGTH_SHORT).show();
            etConfirmPw.requestFocus();
            return false;
        }
        return true;
    }
}
