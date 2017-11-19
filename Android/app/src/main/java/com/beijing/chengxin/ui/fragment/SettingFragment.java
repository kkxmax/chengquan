package com.beijing.chengxin.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.activity.MySettingActivity;
import com.beijing.chengxin.ui.dialog.UpgradeVersionDialog;

import static com.beijing.chengxin.config.Constants.DEBUG_MODE;

public class SettingFragment extends Fragment {

	public final String TAG = SettingFragment.class.getName();

    private View rootView;
    TextView txtAgreement, txtAboutMe, txtChangePassword, txtUpgrade, txtLogout;

    UpgradeVersionDialog dlg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.setting));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        txtAgreement = (TextView)rootView.findViewById(R.id.txt_agreement);
        txtAboutMe = (TextView)rootView.findViewById(R.id.txt_about_me);
        txtChangePassword = (TextView)rootView.findViewById(R.id.txt_change_password);
        txtUpgrade = (TextView)rootView.findViewById(R.id.txt_upgrade);
        txtLogout = (TextView)rootView.findViewById(R.id.txt_logout);

        txtAgreement.setOnClickListener(mButtonClickListener);
        txtAboutMe.setOnClickListener(mButtonClickListener);
        txtChangePassword.setOnClickListener(mButtonClickListener);
        txtUpgrade.setOnClickListener(mButtonClickListener);
        txtLogout.setOnClickListener(mButtonClickListener);

        return rootView;
    }


    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MySettingActivity parent = (MySettingActivity)getActivity();
            switch (v.getId()) {
                case R.id.txt_agreement:
                    parent.showFragment(new AgreementFragment(), true);
                    break;
                case R.id.txt_about_me:
                    parent.showFragment(new AboutMeFragment(), true);
                    break;
                case R.id.txt_change_password:
                    parent.showFragment(new ChangePasswordFragment(), true);
                    break;
                case R.id.txt_upgrade:
                    dlg = new UpgradeVersionDialog(getActivity());
                    dlg.setCancelable(false);
                    dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.setOnUpgradeVersionClickListener(new UpgradeVersionDialog.OnUpgradeVersionClickListener() {
                        @Override
                        public void onUpgradeVersion() {
                            dlg.cancel();
                            Toast.makeText(getContext(), "Application was upgraded!", Toast.LENGTH_LONG).show();
                        }
                    });
                    try {
                        dlg.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.txt_logout:
                    parent.logout();
                    break;
                case R.id.btn_back:
                    parent.onBackActivity();
                    break;
            }
        }
    };
}
