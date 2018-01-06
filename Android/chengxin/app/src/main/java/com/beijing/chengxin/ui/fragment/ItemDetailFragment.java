package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.ItemModel;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.AppUtils;
import com.beijing.chengxin.utils.CommonUtils;
import com.hy.chengxin.http.Api.HttpApi;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import static android.app.Activity.RESULT_OK;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class ItemDetailFragment extends Fragment {

	public final String TAG = ItemDetailFragment.class.getName();

    private View rootView;
    private ImageButton btnShare , btnBack, btnCall;
    private ImageView img_logo, img_logo_enter;
    private LinearLayout layout_enter;
    private TextView txt_name, txt_leixing, txt_city, txt_code, txt_comment, txt_weburl, txt_resource, txt_relation_name, txt_relation_mobile, txt_relation_weixin, txt_enter_name, txt_enter_code, txt_enter_rate, txt_write_time, txt_enter_type;

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
        txt_enter_type = (TextView)rootView.findViewById(R.id.txt_enter_type);
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

    // add dd -- 2017.12.07
    public void showShare(Context context, String platformToShare, boolean showContentEdit) {
        cn.sharesdk.onekeyshare.OnekeyShare oks = new cn.sharesdk.onekeyshare.OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }

        final String title = "【项目】您的好友给您分享了一个项目，立即查看！";

        final String url = Constants.ITEM_SHARE_URL + item.getId();

        oks.setTitle(title);
        oks.setTitleUrl(url);

        String descStr = item.getComment();

        final String desc = descStr;

        oks.setText(desc);
        oks.setImageUrl(Constants.BASE_URL + item.getLogo());
        oks.setUrl(url);

        oks.setShareContentCustomizeCallback(new
                                                     ShareContentCustomizeCallback() {
                                                         @Override
                                                         public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                                                             if ("WechatMoments".equals(platform.getName())) {
                                                                 paramsToShare.setTitle(title);
                                                                 paramsToShare.setText(desc);
                                                                 paramsToShare.setImageUrl(Constants.BASE_URL + item.getLogo());
                                                                 paramsToShare.setUrl(url);
                                                             }
                                                         }
                                                     });
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d(TAG, "onComplete ---->  分享成功");
                platform.isClientValid();

                HttpApi.onShare(Constants.SHARE_KIND.ITEM, item.getId());
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d(TAG, "onError ---->  分享失败" + throwable.getStackTrace().toString());
                Log.d(TAG, "onError ---->  分享失败" + throwable.getMessage());
                throwable.getMessage();
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

        oks.show(context);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    ((BaseFragmentActivity)getActivity()).onBackPressed();
                    break;
                case R.id.btn_share:
                    showShare(getContext(), null, true);
                    break;
                case R.id.txt_weburl:
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CommonUtils.getCompleteWebUrl(item.getWeburl()))));
                    } catch (Exception e) {}
                    break;
                case R.id.layout_enter:
                    EnterpriseDetailFragment fragment = new EnterpriseDetailFragment();
                     fragment.setId(item.getAccountId());
                    ((BaseFragmentActivity)getActivity()).showFragment(fragment, true);
                    break;
                case R.id.btn_call:
                    //onContactDialTask();
                    if (item==null)
                        return;
                    AppUtils.openCall(getActivity(),item.getContactMobile());
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==0x2000&&item!=null){
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.getContactMobile())));
        }
    }

    private void onContactDialTask() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(getActivity());
            }
            @Override
            protected Object doInBackground(Object... params) {
                return info.syncOnContact("" + item.getAccountId());
            }
            @Override
            protected void onPostExecute(Object obj) {
                BaseModel result = (BaseModel) obj;
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.getContactMobile())));
                    } else if (result.getRetCode() == Constants.ERROR_DUPLICATE) {
                        ChengxinApplication.finishActivityFromDuplicate(getActivity());
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
        }.execute();
    }

    private void showUserDetail() {
        if (item == null)
            return;

        Picasso.with(getActivity())
                .load(Constants.FILE_ADDR + item.getLogo())
                .placeholder(R.drawable.no_image_item)
                .into(img_logo);
        Picasso.with(getActivity())
                .load(Constants.FILE_ADDR + item.getAccountLogo())
                .placeholder(item.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? R.drawable.no_image_person : R.drawable.no_image_enter)
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

        if (item.getAkind() == Constants.ACCOUNT_TYPE_PERSON) {
            txt_enter_type.setText(getString(R.string.str_person));
        } else {
            /*if (item.getEnterKind() == Constants.ENTER_KIND_PERSON)
                txt_enter_type.setText(getString(R.string.str_enterprise_personal));
            else
                txt_enter_type.setText(getString(R.string.str_enterprise));*/
            txt_enter_type.setText(getString(R.string.str_enterprise));
        }

        txt_enter_name.setText(item.getAccountName());
        txt_enter_code.setText(item.getAccountCode());
        txt_write_time.setText(CommonUtils.getDateStrFromStrFormat(item.getWriteTimeString(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"));
        if (item.getAccountCredit() < Constants.LEVEL_ZERO)
            txt_enter_rate.setText(getText(R.string.rate_zero));
        else
            txt_enter_rate.setText(String.format("%d%%", item.getAccountCredit()));
    }
}
