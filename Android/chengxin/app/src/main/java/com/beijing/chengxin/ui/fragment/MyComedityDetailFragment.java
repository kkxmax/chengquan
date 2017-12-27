package com.beijing.chengxin.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.ComedityDetailModel;
import com.beijing.chengxin.network.model.ComedityModel;
import com.beijing.chengxin.ui.activity.MakeComedityActivity;
import com.beijing.chengxin.ui.activity.MakeItemActivity;
import com.beijing.chengxin.ui.activity.MyWriteActivity;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.ui.listener.OnCallbackListener;
import com.beijing.chengxin.ui.listener.OnReloadListener;
import com.beijing.chengxin.ui.widget.AutoScrollViewPager;
import com.beijing.chengxin.ui.widget.PageIndicator;
import com.beijing.chengxin.ui.widget.UrlImagePagerAdapter;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class MyComedityDetailFragment extends Fragment {

    public final String TAG = MyComedityDetailFragment.class.getName();

    private View rootView;
    private Context mContext;
    private MyWriteFragment mWriteFragment;
    ComedityModel comedity;

    AutoScrollViewPager recommendViewPager;
    PageIndicator pageIndicator;
    UrlImagePagerAdapter recommendImageAdapter;
    //List<String> listRecommendImageUrl;

    ImageButton btnBack ;
    TextView txtName, txtPrice, txtStatus, txtCode, txtComment, txtWeburl, txtSaleAddr, txtMainComedity;
    Button btnEdit, btnDelete, btnAction;

    public void setData(MyWriteFragment writeFragment, ComedityModel item) {
        this.mWriteFragment = writeFragment;
        this.comedity = item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_comedity_detail, container, false);
        mContext = getContext();
        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.comedity_detail));

        recommendViewPager = (AutoScrollViewPager) rootView.findViewById(R.id.autoScrollViewPager);
        pageIndicator = (PageIndicator) rootView.findViewById(R.id.pageIndicator);

        btnBack = (ImageButton)rootView.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mButtonClickListener);

        txtName = (TextView)rootView.findViewById(R.id.txt_name);
        txtStatus = (TextView)rootView.findViewById(R.id.txt_status);
        txtPrice = (TextView)rootView.findViewById(R.id.txt_price);
        txtCode = (TextView)rootView.findViewById(R.id.txt_code);
        txtComment = (TextView)rootView.findViewById(R.id.txt_comment);
        txtWeburl = (TextView)rootView.findViewById(R.id.txt_weburl);
        txtSaleAddr = (TextView)rootView.findViewById(R.id.txt_sale_addr);
        txtMainComedity = (TextView)rootView.findViewById(R.id.txt_main_comedity );

        btnEdit = (Button) rootView.findViewById(R.id.btn_edit);
        btnDelete = (Button) rootView.findViewById(R.id.btn_delete);
        btnAction = (Button) rootView.findViewById(R.id.btn_action);
        txtWeburl.setOnClickListener(mButtonClickListener);
        btnEdit.setOnClickListener(mButtonClickListener);
        btnDelete.setOnClickListener(mButtonClickListener);
        btnAction.setOnClickListener(mButtonClickListener);

        initData();

        return rootView;
    }

    private void initData() {
        if (comedity == null)
            return;

        ArrayList<String> listRecommendImageUrl = new ArrayList<String>();
        for (int i = 0; i < comedity.getImgPaths().size(); i++) {
            listRecommendImageUrl.add(Constants.FILE_ADDR + comedity.getImgPaths().get(i));
        }
        recommendImageAdapter = new UrlImagePagerAdapter(listRecommendImageUrl).setInfiniteLoop(true);
        recommendViewPager.setAdapter(recommendImageAdapter);
        recommendViewPager.setInterval(5000);
        pageIndicator.setViewPager(recommendViewPager);

        recommendViewPager.startAutoScroll();

        txtName.setText(comedity.getName());
        txtPrice.setText(String.format("¥%.02f", comedity.getPrice()));
        txtCode.setText(comedity.getCode());
        txtComment.setText(comedity.getComment());
        txtWeburl.setText(comedity.getWeburl());
        txtSaleAddr.setText(comedity.getCityName() + " " + comedity.getSaleAddr());
        if (comedity.getStatus() == Constants.COMEDITY_STATUS_UPLOADED) {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            txtStatus.setText(R.string.already_raise_comedity);
            txtStatus.setTextColor(getResources().getColor(R.color.color_orange));
            btnAction.setText(R.string.down_comedity);
        } else {
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            txtStatus.setText(R.string.not_raise_comedity);
            txtStatus.setTextColor(getResources().getColor(R.color.txt_gray));
            btnAction.setText(R.string.raise_comedity);
        }
        if (comedity.getIsMain() == 0) {
            txtMainComedity.setVisibility(View.GONE);
        }
    }

    View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyWriteActivity activity = (MyWriteActivity) getActivity();
            switch (v.getId()){
                case R.id.btn_back:
                    activity.goBack();
                    break;
                case R.id.txt_weburl:
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CommonUtils.getCompleteWebUrl(comedity.getWeburl()))));
                    } catch (Exception e) {}
                    break;
                case R.id.btn_edit:
                    Intent intent = new Intent(getActivity(), MakeComedityActivity.class);
                    intent.putExtra("data", comedity);
                    intent.putExtra("from_where", "MyWriteFragment");
                    startActivityForResult(intent, Constants.REQEUST_CODE_MY_DEPLOY);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case R.id.btn_delete: {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder
                            .setMessage(R.string.msg_delete_confirm_commodity)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mWriteFragment.actionDeleteTask(1, comedity.getId(), new OnCallbackListener() {
                                        @Override
                                        public void onCallback() {
                                            Toast.makeText(mContext, R.string.msg_success_delete, Toast.LENGTH_SHORT).show();
                                            ((MyWriteActivity) getActivity()).goBack();
                                            mWriteFragment.reloadData(MyWriteFragment.INDEX_GET_DATA_ALL);
                                        }
                                    });
                                    dialog.cancel();
                                }
                            })
                            .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                break;
                case R.id.btn_action: {
                    final int actionIndex = (comedity.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? 0 : 1);
                    final int confirmStringId = (comedity.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? R.string.msg_down_confirm_comedity : R.string.msg_up_confirm_comedity);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder
                            .setMessage(confirmStringId)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mWriteFragment.actionUpDownTask(1, actionIndex, comedity.getId(), new OnCallbackListener() {
                                        @Override
                                        public void onCallback() {
                                            comedity.setStatus(comedity.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? Constants.COMEDITY_STATUS_DONWLOADED : Constants.COMEDITY_STATUS_UPLOADED);
                                            initData();
                                        }
                                    });
                                    dialog.cancel();
                                }
                            })
                            .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.REQEUST_CODE_MY_DEPLOY) {
            ((MyWriteActivity) getActivity()).goBack();
            mWriteFragment.reloadData(MyWriteFragment.INDEX_GET_DATA_ALL);
        }
    }

}
