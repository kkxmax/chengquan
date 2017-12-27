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
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.ComedityDetailModel;
import com.beijing.chengxin.network.model.ComedityModel;
import com.beijing.chengxin.ui.activity.MainActivity;
import com.beijing.chengxin.ui.widget.AutoScrollViewPager;
import com.beijing.chengxin.ui.widget.PageIndicator;
import com.beijing.chengxin.ui.widget.UrlImagePagerAdapter;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.hy.chengxin.http.Api.HttpApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class ComedityDetailFragment extends Fragment{

	public final String TAG = ComedityDetailFragment.class.getName();
    private View rootView;

    private ImageButton btnShare , btnBack, btnCall;
    private TextView txt_name, txt_price, txt_code, txt_comment, txt_weburl, txt_place, txt_enter_name, txt_enter_type, txt_enter_code, txt_enter_rate, txt_main_comedity;
    ImageView img_avatar;
    ToggleButton btnFavorite;
    Button btn_buy;
    LinearLayout layoutAccount, layoutBottom;

    AutoScrollViewPager recommendViewPager;
    PageIndicator pageIndicator;
    UrlImagePagerAdapter recommendImageAdapter;
    List<String> listRecommendImageUrl;

    SyncInfo info;
    int productId;
    ComedityModel comedity;

    public void setId(int productId) {
        this.productId = productId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	rootView = inflater.inflate(R.layout.activity_comedity_detail, container, false);

        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.comedity_detail));
        btnShare = (ImageButton)rootView.findViewById(R.id.btn_share);
        btnShare.setVisibility(View.VISIBLE);
        btnShare.setOnClickListener(mClickListener);

        recommendViewPager = (AutoScrollViewPager) rootView.findViewById(R.id.autoScrollViewPager);
        pageIndicator = (PageIndicator) rootView.findViewById(R.id.pageIndicator);

        btnBack = (ImageButton)rootView.findViewById(R.id.btn_back);
        btnFavorite = (ToggleButton) rootView.findViewById(R.id.btn_favorite);
        btnCall = (ImageButton) rootView.findViewById(R.id.btn_call);
        btn_buy = (Button) rootView.findViewById(R.id.btn_buy);

        btnBack.setOnClickListener(mClickListener);
        btnFavorite.setOnClickListener(mClickListener);
        btn_buy.setOnClickListener(mClickListener);
        btnCall.setOnClickListener(mClickListener);

        txt_name = (TextView)rootView.findViewById(R.id.txt_name);
        txt_price = (TextView)rootView.findViewById(R.id.txt_price);
        txt_code = (TextView)rootView.findViewById(R.id.txt_code);
        txt_comment = (TextView)rootView.findViewById(R.id.txt_comment);
        txt_weburl = (TextView)rootView.findViewById(R.id.txt_weburl);
        txt_place = (TextView)rootView.findViewById(R.id.txt_place);
        txt_enter_name = (TextView)rootView.findViewById(R.id.txt_enter_name);
        txt_enter_type = (TextView)rootView.findViewById(R.id.txt_enter_type);
        txt_enter_code = (TextView)rootView.findViewById(R.id.txt_enter_code);
        txt_enter_rate = (TextView)rootView.findViewById(R.id.txt_enter_rate);
        txt_main_comedity = (TextView)rootView.findViewById(R.id.txt_main_comedity);

        txt_weburl.setOnClickListener(mClickListener);

        layoutBottom = (LinearLayout)rootView.findViewById(R.id.layout_bottom);
        layoutAccount = (LinearLayout)rootView.findViewById(R.id.layout_account);
        layoutAccount.setOnClickListener(mClickListener);

        img_avatar = (ImageView) rootView.findViewById(R.id.img_avatar);

        info = new SyncInfo(getActivity());

        if (productId != 0)
            new CommodityDetailAsync().execute(String.valueOf(productId));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        recommendViewPager.startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        recommendViewPager.stopAutoScroll();
    }

    // add dd -- 2017.12.07
    public void showShare(Context context, String platformToShare, boolean showContentEdit) {
        cn.sharesdk.onekeyshare.OnekeyShare oks = new cn.sharesdk.onekeyshare.OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }

        final String title = "【产品】您的好友给您分享了一个产品，立即查看！";

        final String url = Constants.COMEDITY_SHARE_URL + productId;

        oks.setTitle(title);
        oks.setTitleUrl(url);

        String descStr = comedity.getComment();

        final String desc = descStr;

        oks.setText(desc);
        oks.setImageUrl(Constants.BASE_URL + comedity.getImgPaths().get(0));
        oks.setUrl(url);

        oks.setShareContentCustomizeCallback(new
                                                     ShareContentCustomizeCallback() {
                                                         @Override
                                                         public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                                                             if ("WechatMoments".equals(platform.getName())) {
                                                                 paramsToShare.setTitle(title);
                                                                 paramsToShare.setText(desc);
                                                                 paramsToShare.setImageUrl(Constants.BASE_URL + comedity.getImgPaths().get(0));
                                                                 paramsToShare.setUrl(url);
                                                             }
                                                         }
                                                     });

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d(TAG, "onComplete ---->  分享成功");
                platform.isClientValid();

                HttpApi.onShare(Constants.SHARE_KIND.COMEDITY, productId);
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
                case R.id.btn_share:
                    showShare(getContext(), null, true);
                    break;
                case R.id.btn_back:
                    ((BaseFragmentActivity)getActivity()).onBackPressed();
                    break;
                case R.id.layout_account:
                    EnterpriseDetailFragment efragment = new EnterpriseDetailFragment();
                    efragment.setId(comedity.getAccountId());
                    ((BaseFragmentActivity)getActivity()).showFragment(efragment, true);
                    break;
                case R.id.txt_weburl:
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CommonUtils.getCompleteWebUrl(comedity.getWeburl()))));
                    } catch (Exception e) {}
                    break;
                case R.id.btn_call:
                    new OnContactAsync().execute(String.valueOf(comedity.getAccountId()));
                    break;
                case R.id.btn_favorite:
                    String favorite = (comedity.getIsFavourite() == 1) ? "0" : "1";
                    new SetFavouriteAsync().execute(String.valueOf(comedity.getId()), favorite, "1");
                    break;
                case R.id.btn_buy:
                    new OnPurchaseAsync().execute(String.valueOf(comedity.getId()));
                    break;
            }
        }
    };

    class CommodityDetailAsync extends AsyncTask<String, String, ComedityDetailModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected ComedityDetailModel doInBackground(String... strs) {
            return info.syncComedityDetail(strs[0]);
        }
        @Override
        protected void onPostExecute(ComedityDetailModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    comedity = result.getProduct();
                    showComedityDetail();
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
    }

    class SetFavouriteAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncSetFavourite(strs[0], strs[1], strs[2]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    int favorite = (comedity.getIsFavourite() == 1) ? 0 : 1;
                    comedity.setIsFavourite(favorite);
                    btnFavorite.setChecked(favorite == 1);
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
    }

    class OnPurchaseAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getActivity());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncOnPurchase(strs[0]);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CommonUtils.getCompleteWebUrl(comedity.getWeburl()))));
                    } catch (Exception e) {}
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
    }

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
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + comedity.getAccountMobile())));
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
    }

    private void showComedityDetail() {
        if (comedity == null)
            return;

        listRecommendImageUrl = new ArrayList<String>();
        for (int i = 0; i < comedity.getImgPaths().size(); i++) {
            listRecommendImageUrl.add(Constants.FILE_ADDR + comedity.getImgPaths().get(i));
        }
        recommendImageAdapter = new UrlImagePagerAdapter(listRecommendImageUrl).setInfiniteLoop(true);
        recommendViewPager.setAdapter(recommendImageAdapter);
        recommendViewPager.setInterval(5000);
        pageIndicator.setViewPager(recommendViewPager);

        recommendViewPager.startAutoScroll();

        txt_name.setText(comedity.getName());
        txt_price.setText(String.format("¥%.02f", comedity.getPrice()));
        txt_code.setText(comedity.getCode());
        txt_comment.setText(comedity.getComment());
        txt_weburl.setText(comedity.getWeburl());
        txt_place.setText(comedity.getCityName() + " " + comedity.getSaleAddr());
        txt_enter_name.setText(comedity.getEnterName());
        /*if (comedity.getEnterKind() == Constants.ENTER_KIND_PERSON)
            txt_enter_type.setText(getString(R.string.str_enterprise_personal));
        else
            txt_enter_type.setText(getString(R.string.str_enterprise));*/
        txt_enter_type.setText(getString(R.string.str_enterprise));
        txt_enter_code.setText(comedity.getAccountCode());
        if (comedity.getAccountCredit() < Constants.LEVEL_ZERO)
            txt_enter_rate.setText(getText(R.string.rate_zero));
        else
            txt_enter_rate.setText(String.format("%d%%", comedity.getAccountCredit()));

        if (comedity.getIsMain() == 0) {
            txt_main_comedity.setVisibility(View.GONE);
        }

        Picasso.with(getActivity())
                .load(Constants.FILE_ADDR + comedity.getAccountLogo())
                .placeholder(comedity.getEnterKind() == Constants.ENTER_KIND_PERSON ? R.drawable.no_image_person : R.drawable.no_image_enter)
                .into(img_avatar);

        btnFavorite.setChecked(comedity.getIsFavourite() == 1);
        if (URLUtil.isValidUrl(CommonUtils.getCompleteWebUrl(comedity.getWeburl())))
            layoutBottom.setVisibility(View.VISIBLE);
        else
            layoutBottom.setVisibility(View.GONE);
    }
}
