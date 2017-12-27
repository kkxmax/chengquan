package com.beijing.chengxin.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.ItemModel;
import com.beijing.chengxin.network.model.ServeModel;
import com.beijing.chengxin.ui.activity.MakeItemActivity;
import com.beijing.chengxin.ui.activity.MakeServeActivity;
import com.beijing.chengxin.ui.activity.MyWriteActivity;
import com.beijing.chengxin.ui.listener.OnCallbackListener;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

public class MyServeDetailFragment extends Fragment {

    public final String TAG = MyServeDetailFragment.class.getName();

    private View rootView;
    private Context mContext;
    private MyWriteFragment mWriteFragment;
    ServeModel item;

    ImageView imgLogo;
    ImageButton btnBack;
    Button btnAction, btnEdit, btnDelete, btnAction2;
    TextView txtName, txtPlace, txtStatus, txtCode, txtComment, txtWeburl, txtNeed, txtContactName, txtFenlei;
    TextView txtContactMobile, txtContactWeixin, txtTime;

    public void setData(MyWriteFragment writeFragment, ServeModel item) {
        this.mWriteFragment = writeFragment;
        this.item = item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_item_detail, container, false);
        mContext = getContext();
        ((TextView) rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.serve_detail));
        ((TextView) rootView.findViewById(R.id.label_comment)).setText(getString(R.string.serve_comment));

        btnBack = (ImageButton) rootView.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mClickListener);

        imgLogo = (ImageView) rootView.findViewById(R.id.img_logo);
        btnAction = (Button) rootView.findViewById(R.id.btn_action);
        txtName = (TextView) rootView.findViewById(R.id.txt_name);
        txtFenlei = (TextView) rootView.findViewById(R.id.btn_fenlei);
        txtPlace = (TextView) rootView.findViewById(R.id.txt_place);
        txtStatus = (TextView) rootView.findViewById(R.id.txt_status);
        txtCode = (TextView) rootView.findViewById(R.id.txt_code);
        txtComment = (TextView) rootView.findViewById(R.id.txt_comment);
        txtWeburl = (TextView) rootView.findViewById(R.id.txt_weburl);
        txtNeed = (TextView) rootView.findViewById(R.id.txt_need);
        txtContactName = (TextView) rootView.findViewById(R.id.txt_contact_name);
        txtContactMobile = (TextView) rootView.findViewById(R.id.txt_contact_mobile);
        txtContactWeixin = (TextView) rootView.findViewById(R.id.txt_contact_weixin);
        txtTime = (TextView) rootView.findViewById(R.id.txt_time);
        btnEdit = (Button) rootView.findViewById(R.id.btn_edit);
        btnDelete = (Button) rootView.findViewById(R.id.btn_delete);
        btnAction2 = (Button) rootView.findViewById(R.id.btn_action2);

        btnAction.setOnClickListener(mClickListener);
        txtWeburl.setOnClickListener(mClickListener);
        btnEdit.setOnClickListener(mClickListener);
        btnDelete.setOnClickListener(mClickListener);
        btnAction2.setOnClickListener(mClickListener);

        initData();

        return rootView;
    }

    private void initData() {
        if (item == null)
            return;

        Picasso.with(getActivity())
                .load(Constants.FILE_ADDR + item.getLogo())
                .placeholder(R.drawable.no_image_item)
                .into(imgLogo);

        txtName.setText(item.getName());
        txtFenlei.setText(item.getFenleiName());
        txtPlace.setText(item.getCityName());
        txtCode.setText(item.getCode());
        txtComment.setText(item.getComment());
        txtWeburl.setText(item.getWeburl());
        txtNeed.setText(item.getNeed());
        txtContactName.setText(item.getContactName());
        txtContactMobile.setText(item.getContactMobile());
        txtContactWeixin.setText(item.getContactWeixin());
        txtTime.setText(item.getWriteTime().getDateHourMinString());

        if (item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED) {
            btnAction.setText(R.string.down_comedity);
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            txtStatus.setText(R.string.already_raise_comedity);
            btnAction2.setText(R.string.down_comedity);
        } else {
            btnAction.setText(R.string.raise_comedity);
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            txtStatus.setText(R.string.not_raise_comedity);
            btnAction2.setText(R.string.raise_comedity);
        }
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyWriteActivity activity = (MyWriteActivity) getActivity();
            switch (v.getId()) {
                case R.id.btn_back:
                    activity.goBack();
                    break;
                case R.id.txt_weburl:
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CommonUtils.getCompleteWebUrl(item.getWeburl()))));
                    } catch (Exception e) {}
                    break;
                case R.id.btn_edit:
                    Intent intent = new Intent(getActivity(), MakeServeActivity.class);
                    intent.putExtra("data", item);
                    intent.putExtra("from_where", "MyWriteFragment");
                    startActivityForResult(intent, Constants.REQEUST_CODE_MY_DEPLOY);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case R.id.btn_delete: {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder
                            .setMessage(R.string.msg_delete_confirm_service)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mWriteFragment.actionDeleteTask(3, item.getId(), new OnCallbackListener() {
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
                case R.id.btn_action:
                case R.id.btn_action2: {
                    final int actionIndex = (item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? 0 : 1);
                    final int confirmStringId = (item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? R.string.msg_down_confirm_service : R.string.msg_up_confirm_service);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder
                            .setMessage(confirmStringId)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mWriteFragment.actionUpDownTask(3, actionIndex, item.getId(), new OnCallbackListener() {
                                        @Override
                                        public void onCallback() {
                                            item.setStatus(item.getStatus() == Constants.COMEDITY_STATUS_UPLOADED ? Constants.COMEDITY_STATUS_DONWLOADED : Constants.COMEDITY_STATUS_UPLOADED);
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