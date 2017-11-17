package com.beijing.chengxin.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.ItemModel;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class ItemDetailFragment extends Fragment {

	public final String TAG = ItemDetailFragment.class.getName();

    private View rootView;
    private ImageButton btnShare , btnBack, btnCall;
    private ImageView img_logo, img_logo_enter;
    private LinearLayout layout_enter;
    private TextView txt_name, txt_leixing, txt_city, txt_code, txt_comment, txt_weburl, txt_resource, txt_relation_name, txt_relation_mobile, txt_relation_weixin, txt_enter_name, txt_enter_code, txt_enter_rate, txt_write_time;

    SyncInfo info;
    ItemModel item;

    public void setItem(ItemModel item) {
        this.item = item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        // set title
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.item_detail));
        btnShare = (ImageButton)rootView.findViewById(R.id.btn_share);
        btnShare.setVisibility(View.VISIBLE);

        btnBack = (ImageButton)rootView.findViewById(R.id.btn_back);
        btnCall = (ImageButton)rootView.findViewById(R.id.btn_call);

        btnShare.setOnClickListener(mClickListener);
        btnBack.setOnClickListener(mClickListener);
        btnCall.setOnClickListener(mClickListener);

        img_logo = (ImageView)rootView.findViewById(R.id.img_logo);
        img_logo_enter = (ImageView)rootView.findViewById(R.id.img_logo_enter);

        txt_name = (TextView)rootView.findViewById(R.id.txt_name);
        txt_leixing = (TextView)rootView.findViewById(R.id.txt_leixing);
        txt_city = (TextView)rootView.findViewById(R.id.txt_city);
        txt_code = (TextView)rootView.findViewById(R.id.txt_code);
        txt_comment = (TextView)rootView.findViewById(R.id.txt_comment);
        txt_weburl = (TextView)rootView.findViewById(R.id.txt_weburl);
        txt_resource = (TextView)rootView.findViewById(R.id.txt_resource);
        txt_relation_name = (TextView)rootView.findViewById(R.id.txt_relation_name);
        txt_relation_mobile = (TextView)rootView.findViewById(R.id.txt_relation_mobile);
        txt_relation_weixin = (TextView)rootView.findViewById(R.id.txt_relation_weixin);
        txt_enter_name = (TextView)rootView.findViewById(R.id.txt_enter_name);
        txt_enter_code = (TextView)rootView.findViewById(R.id.txt_enter_code);
        txt_enter_rate = (TextView)rootView.findViewById(R.id.txt_enter_rate);
        txt_write_time = (TextView)rootView.findViewById(R.id.txt_write_time);
        layout_enter  = (LinearLayout) rootView.findViewById(R.id.layout_enter);

        txt_weburl.setOnClickListener(mClickListener);
        layout_enter.setOnClickListener(mClickListener);

        showUserDetail();

        info = new SyncInfo(getActivity());

        return rootView;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    ((BaseFragmentActivity)getActivity()).onBackPressed();
                    break;
                case R.id.btn_share:
                    break;
                case R.id.txt_weburl:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getWeburl())));
                    break;
                case R.id.layout_enter:
                    EnterpriseDetailFragment fragment = new EnterpriseDetailFragment();
                     fragment.setId(item.getAccountId());
                    ((BaseFragmentActivity)getActivity()).showFragment(fragment, true);
                    break;
                case R.id.btn_call:
                    new OnContactAsync().execute(String.valueOf(item.getAccountId()));
                    break;
            }
        }
    };

    class OnContactAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncOnContact(strs[0]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.getAccountMobile())));
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

    private void showUserDetail() {
        if (item == null)
            return;

        Picasso.with(getActivity())
                .load(Constants.FILE_ADDR + item.getLogo())
                .placeholder(R.drawable.no_image)
                .into(img_logo);
        Picasso.with(getActivity())
                .load(Constants.FILE_ADDR + item.getAccountLogo())
                .placeholder(R.drawable.no_image)
                .into(img_logo_enter);

        txt_name.setText(item.getName());
        txt_leixing.setText(item.getFenleiName());
        txt_city.setText(item.getCityName());
        txt_code.setText(item.getCode());
        txt_comment.setText(item.getComment());
        txt_weburl.setText(item.getWeburl());
        txt_resource.setText(item.getNeed());
        txt_relation_name.setText(item.getContactName());
        txt_relation_mobile.setText(item.getContactMobile());
        txt_relation_weixin.setText(item.getContactWeixin());
        txt_enter_name.setText(item.getAccountName());
        txt_enter_code.setText(item.getAccountCode());
        txt_enter_rate.setText(String.format("%d%%", item.getAccountCredit()));
        txt_write_time.setText(item.getWriteTimeString());
    }
}
