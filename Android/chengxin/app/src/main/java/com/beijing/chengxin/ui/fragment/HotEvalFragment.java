package com.beijing.chengxin.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class HotEvalFragment extends Fragment {

	public final String TAG = HotEvalFragment.class.getName();
    private  static int MAX_LETTER_COUNT = 200;
    private View rootView;

    private EditText edtText;
    private TextView txtLetterCount;
    SyncInfo info;

    int hotId;

    public void setId(int id) {
        hotId = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_hot_eval, container, false);

        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.hot_eval));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mClickListener);
        rootView.findViewById(R.id.btn_send).setOnClickListener(mClickListener);

        txtLetterCount = (TextView)rootView.findViewById(R.id.txt_letter_count);
        txtLetterCount.setText(String.format("0/%d",  MAX_LETTER_COUNT));

        edtText = (EditText)rootView.findViewById(R.id.edit_eval_content);
        edtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = edtText.getText().toString();
                int length = content.length();
                txtLetterCount.setText(String.format("%d/%d", length, MAX_LETTER_COUNT));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        info = new SyncInfo(getActivity());

        return rootView;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    CommonUtils.hideKeyboardFrom(getActivity());
                    ((BaseFragmentActivity)getActivity()).goHome();
                    break;
                case R.id.btn_send:
                    CommonUtils.hideKeyboardFrom(getActivity());
                    String msg = edtText.getText().toString();
                    if (!msg.equals(""))
                        new LeaveEstimateAsync().execute(String.valueOf(hotId), msg);
                    else
                        Toast.makeText(getContext(), getString(R.string.err_eval_content_not_empty), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    class LeaveEstimateAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncLeaveEstimate(2, "", strs[0], "", "", "", strs[1], null);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    Toast.makeText(getActivity(), "成功发布评价", Toast.LENGTH_LONG).show();
                    ((BaseFragmentActivity)getActivity()).goHome();
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
