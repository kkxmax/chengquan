package com.beijing.chengxin.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.ProvinceListModel;
import com.beijing.chengxin.network.model.XyleixingListModel;
import com.beijing.chengxin.ui.activity.LoginActivity;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class RegisterCompleteFragment extends Fragment {

    public final String TAG = RegisterCompleteFragment.class.getName();

    private ImageButton btnBack;
    private Button btnRegisterCert , btnSkip;
    private View rootView;
    SyncInfo info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register_complete, container, false);

        btnRegisterCert = (Button)rootView.findViewById(R.id.btn_register_cert) ;
        btnSkip = (Button)rootView.findViewById(R.id.btn_register_skip);
        btnBack = (ImageButton)rootView.findViewById(R.id.btn_back);

        btnRegisterCert.setOnClickListener(mButtonClickListener);
        btnSkip.setOnClickListener(mButtonClickListener);
        btnBack.setOnClickListener(mButtonClickListener);

        info = new SyncInfo(getActivity());

        if (AppConfig.getInstance().cityList.size() == 0)
            new CityListAsync().execute();
        if (AppConfig.getInstance().xyleixingList.size() == 0)
            new XyleixingListAsync().execute();

        return rootView;
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LoginActivity parent = (LoginActivity)getActivity();
            switch (v.getId()) {
                case R.id.btn_register_cert:
                    RealnameCertFragment fragment = new RealnameCertFragment();
                    parent.showFragment(fragment, true);
                    break;
                case R.id.btn_register_skip:
                    parent.toMainActivity();
                    break;
                case R.id.btn_back:
                    parent.goHome();
                    break;
            }
        }
    };
    class CityListAsync extends AsyncTask<String, String, ProvinceListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected ProvinceListModel doInBackground(String... strs) {
            return info.syncCityList();
        }
        @Override
        protected void onPostExecute(ProvinceListModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    AppConfig.getInstance().cityList.clear();
                    AppConfig.getInstance().cityList.addAll(result.getList());
                }
            }
            //Utils.disappearProgressDialog();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Utils.disappearProgressDialog();
        }
    }

    class XyleixingListAsync extends AsyncTask<String, String, XyleixingListModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected XyleixingListModel doInBackground(String... strs) {
            return info.syncXyleixingList("");
        }
        @Override
        protected void onPostExecute(XyleixingListModel result) {
            super.onPostExecute(result);
            if (result.isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    AppConfig.getInstance().xyleixingList.clear();
                    AppConfig.getInstance().xyleixingList.addAll(result.getList());
                }
            }
            //Utils.disappearProgressDialog();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Utils.disappearProgressDialog();
        }
    }
}
