package com.beijing.chengxin.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.AccountModel;
import com.beijing.chengxin.network.model.CityModel;
import com.beijing.chengxin.network.model.LoginModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.network.model.XyleixingListModel;
import com.beijing.chengxin.network.model.XyleixingModel;
import com.beijing.chengxin.ui.activity.ImageViewActivity;
import com.beijing.chengxin.ui.activity.LoginActivity;
import com.beijing.chengxin.ui.activity.MyRealnameCertActivity;
import com.beijing.chengxin.ui.dialog.MeHangyeListDialog;
import com.beijing.chengxin.ui.dialog.SelectCityDialog;
import com.beijing.chengxin.ui.dialog.SelectGalleryDialog;
import com.beijing.chengxin.ui.listener.OnCancelListener;
import com.beijing.chengxin.ui.view.CompanyListView;
import com.beijing.chengxin.ui.view.MeHangyeListView;
import com.beijing.chengxin.ui.widget.GridView;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.beijing.chengxin.utils.ResUtils;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class RealnameCertFragment extends Fragment {
    // add dd -- 2017.12.07
    public static RealnameCertFragment instance;
    private int mIndex;
    public final String TAG = RealnameCertFragment.class.getName();

    private static final int PICK_FROM_CAMERA_PERSON_LOGO = 1;
    private static final int PICK_FROM_CAMERA_PERSON_CERT = 2;
    private static final int PICK_FROM_GALLERY_PERSON_LOGO = 3;
    private static final int PICK_FROM_GALLERY_PERSON_CERT = 4;
    private static final int PICK_FROM_CAMERA_ENTER_LOGO = 5;
    private static final int PICK_FROM_CAMERA_ENTER_CERT = 6;
    private static final int PICK_FROM_GALLERY_ENTER_LOGO = 7;
    private static final int PICK_FROM_GALLERY_ENTER_CERT = 8;

    private View rootView;
    private UserModel mUser;
    private ArrayList<XyleixingModel> mXyleixingList;
    private Context mContext;

    private CompanyListView mCompanyListView;
    private SelectCityDialog mCityDialog;
    private MeHangyeListView mHangyeListViewPerson, mHangyeListViewEnter;

    private CustomWatchListAdapter mAdapter1, mAdapter2;
    private CustomWatchListAdapter mAdapter1Enter, mAdapter2Enter;

    private int mEnterpriseId;
    private int mCityId, mHangyeId;
    private int mCityIdEnter, mHangyeIdEnter;

    // Common UI Controller
    ToggleButton btnPerson, btnEnterprise;
    View viewPerson, viewEnterprise, viewSubmit;
    ImageView imgStatus;
    Button btnSubmit;
    LinearLayout viewCondition;
    LinearLayout layoutOut;
    FrameLayout viewConditionBody;

    // Person UI Controller
    ImageView imgLogo, imgCertPhoto;
    TextView txtCertPhotoAdd, txtSelectHangye, txtSelectCity, txtSelectEnterprise;
    EditText edtRealname, edtCertNum, edtJob, edtWeixin, edtExperience, edtHistory;
    GridView gridView1, gridView2;

    // Enterprise UI Controller
    ImageView imgLogoEnter, imgCertPhotoEnter;
    ToggleButton btnEnter1, btnEnter2;
    EditText edtNameEnter, edtAddrEnter, edtWeburlEnter, edtWeixinEnter, edtMainJobEnter, edtCertNumEnter;
    EditText edtCommentEnter, edtRecommendEnter, edtBossNameEnter, edtBossJobEnter, edtBossMobileEnter, edtBossWeixinEnter;
    TextView txtCertPhotoAddEnter, txtSelectHangyeEnter, txtSelectCityEnter;
    GridView gridView1Enter, gridView2Enter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // ddd dd -- 2017.12.07
        instance = this;
        rootView = inflater.inflate(R.layout.activity_my_realname_cert, container, false);
        mContext = getContext();

        ((TextView)rootView.findViewById(R.id.txt_nav_title)).setText(getString(R.string.realname_cert));
        rootView.findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        // Common UI Controller
        btnPerson = (ToggleButton) rootView.findViewById(R.id.btn_person);
        btnEnterprise = (ToggleButton) rootView.findViewById(R.id.btn_enterprise);
        btnPerson.setOnClickListener(mButtonClickListener);
        btnEnterprise.setOnClickListener(mButtonClickListener);

        viewPerson = (View) rootView.findViewById(R.id.view_person);
        viewEnterprise = (View) rootView.findViewById(R.id.view_enterprise);
        viewSubmit = (View) rootView.findViewById(R.id.view_submit);
        imgStatus = (ImageView) rootView.findViewById(R.id.img_status);
        btnSubmit = (Button) rootView.findViewById(R.id.btn_submit);
        viewCondition = (LinearLayout) rootView.findViewById(R.id.view_condition);
        viewConditionBody = (FrameLayout) rootView.findViewById(R.id.view_condition_body);
        layoutOut = (LinearLayout) rootView.findViewById(R.id.layout_out);
        layoutOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCondition.setVisibility(View.GONE);
                viewConditionBody.setVisibility(View.GONE);

                if (mCompanyListView != null && mCompanyListView.getVisibility() != View.GONE) {
                    mCompanyListView.setVisibility(View.GONE);
                }
                if (mHangyeListViewPerson != null && mHangyeListViewPerson.getVisibility() != View.GONE) {
                    mHangyeListViewPerson.setVisibility(View.GONE);
                }
                if (mHangyeListViewEnter != null && mHangyeListViewEnter.getVisibility() != View.GONE) {
                    mHangyeListViewEnter.setVisibility(View.GONE);
                }
            }
        });

        //Person UI Controller
        imgLogo = (ImageView) rootView.findViewById(R.id.img_logo);
        imgCertPhoto = (ImageView) rootView.findViewById(R.id.img_cert_photo);
        txtCertPhotoAdd = (TextView) rootView.findViewById(R.id.txt_cert_photo_add);
        txtSelectEnterprise = (TextView) rootView.findViewById(R.id.txt_select_enterprise);
        txtSelectHangye = (TextView) rootView.findViewById(R.id.txt_select_hangye);
        txtSelectCity = (TextView) rootView.findViewById(R.id.txt_select_city);
        edtRealname = (EditText) rootView.findViewById(R.id.edit_realname);
        edtCertNum = (EditText) rootView.findViewById(R.id.edit_cert_num);
        edtJob = (EditText) rootView.findViewById(R.id.edit_job);
        edtWeixin = (EditText) rootView.findViewById(R.id.edit_weixin);
        edtExperience = (EditText) rootView.findViewById(R.id.edit_experience);
        edtHistory = (EditText) rootView.findViewById(R.id.edit_history);
        gridView1 = (GridView) rootView.findViewById(R.id.grid_view1);
        gridView2 = (GridView) rootView.findViewById(R.id.grid_view2);

        txtSelectEnterprise.setOnClickListener(mButtonClickListener);
        txtSelectHangye.setOnClickListener(mButtonClickListener);
        txtSelectCity.setOnClickListener(mButtonClickListener);

        //Enterprise UI Controller
        imgLogoEnter = (ImageView) rootView.findViewById(R.id.img_logo_enter);
        imgCertPhotoEnter = (ImageView) rootView.findViewById(R.id.img_cert_photo_enter);
        btnEnter1 = (ToggleButton) rootView.findViewById(R.id.btn_enter_1);
        btnEnter2 = (ToggleButton) rootView.findViewById(R.id.btn_enter_2);
        edtNameEnter = (EditText) rootView.findViewById(R.id.edit_name_enter);
        edtAddrEnter = (EditText) rootView.findViewById(R.id.edit_addr_enter);
        edtWeburlEnter = (EditText) rootView.findViewById(R.id.edit_weburl_enter);
        edtWeixinEnter = (EditText) rootView.findViewById(R.id.edit_weixin_enter);
        edtMainJobEnter = (EditText) rootView.findViewById(R.id.edit_mainjob_enter);
        edtCertNumEnter = (EditText) rootView.findViewById(R.id.edit_cert_num_enter);
        edtCommentEnter = (EditText) rootView.findViewById(R.id.edit_comment_enter);
        edtRecommendEnter = (EditText) rootView.findViewById(R.id.edit_recommend_enter);
        edtBossNameEnter = (EditText) rootView.findViewById(R.id.edit_boss_name_enter);
        edtBossJobEnter = (EditText) rootView.findViewById(R.id.edit_boss_job_enter);
        edtBossMobileEnter = (EditText) rootView.findViewById(R.id.edit_boss_mobile_enter);
        edtBossWeixinEnter = (EditText) rootView.findViewById(R.id.edit_boss_weixin_enter);
        txtCertPhotoAddEnter = (TextView) rootView.findViewById(R.id.txt_cert_photo_add_enter);
        txtSelectHangyeEnter = (TextView) rootView.findViewById(R.id.txt_select_hangye_enter);
        txtSelectCityEnter = (TextView) rootView.findViewById(R.id.txt_select_city_enter);
        gridView1Enter = (GridView) rootView.findViewById(R.id.grid_view1_enter);
        gridView2Enter = (GridView) rootView.findViewById(R.id.grid_view2_enter);

        btnEnter1.setOnClickListener(mButtonClickListener);
        btnEnter2.setOnClickListener(mButtonClickListener);
        txtSelectHangyeEnter.setOnClickListener(mButtonClickListener);
        txtSelectCityEnter.setOnClickListener(mButtonClickListener);

        mAdapter1 = new CustomWatchListAdapter();
        mAdapter2 = new CustomWatchListAdapter();
        mAdapter1Enter = new CustomWatchListAdapter();
        mAdapter2Enter = new CustomWatchListAdapter();

        getDataTask();

        return rootView;
    }

    private void initUI() {
        // 审核状态, 提交状态
        int imgStatusResId = 0;
        int submitStringId = 0;
        switch (mUser.getTestStatus()) {
            case Constants.TEST_STATUS_READY:
                imgStatusResId = R.drawable.label_status_ready;
                break;
            case Constants.TEST_STATUS_PASSED:
                imgStatusResId = R.drawable.label_status_passed;
                submitStringId = R.string.save;
                break;
            case Constants.TEST_STATUS_REJECT:
                imgStatusResId = R.drawable.label_status_reject;
                submitStringId = R.string.realname_cert_button_reply;
                break;
            default:
                submitStringId = R.string.realname_cert_button;
                break;
        }
        if (imgStatusResId != 0) {
            imgStatus.setVisibility(View.VISIBLE);
            imgStatus.setImageResource(imgStatusResId);
        } else {
            imgStatus.setVisibility(View.GONE);
        }
        if (submitStringId != 0) {
            viewSubmit.setVisibility(View.VISIBLE);
            btnSubmit.setText(submitStringId);
        } else {
            viewSubmit.setVisibility(View.GONE);
        }
        btnSubmit.setOnClickListener(mButtonClickListener);
        if (mUser.getAkind() == Constants.ACCOUNT_TYPE_PERSON) {
            btnPerson.setChecked(true);
            btnEnterprise.setChecked(false);
            viewPerson.setVisibility(View.VISIBLE);
            viewEnterprise.setVisibility(View.GONE);
        } else if (mUser.getAkind() == Constants.ACCOUNT_TYPE_ENTERPRISE) {
            btnPerson.setChecked(false);
            btnEnterprise.setChecked(true);
            viewPerson.setVisibility(View.GONE);
            viewEnterprise.setVisibility(View.VISIBLE);
        }
        // Person UI Controller
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imgLogo.setLayoutParams(params1);
        Picasso.with(getContext()).load(Constants.FILE_ADDR + mUser.getLogo()).placeholder(R.drawable.add_touxiang).into(imgLogo);
        Picasso.with(getContext()).load(Constants.FILE_ADDR + mUser.getCertImage()).placeholder(R.drawable.cert_person).into(imgCertPhoto);
        mEnterpriseId = mUser.getEnterId();
        txtSelectEnterprise.setText(mUser.getEnterName());
        txtSelectHangye.setText(mUser.getXyName());
        mHangyeId = mUser.getXyleixingId();
        txtSelectCity.setText(mUser.getCityName());
        mCityId = mUser.getCityId();
        edtRealname.setText(mUser.getRealname());
        edtCertNum.setText(mUser.getCertNum());
        edtJob.setText(mUser.getJob());
        edtWeixin.setText(mUser.getWeixin());
        edtExperience.setText(mUser.getExperience());
        edtHistory.setText(mUser.getHistory());

        ArrayList<Integer> curIds1_person = new ArrayList<Integer>();
        ArrayList<Integer> curIds2_person = new ArrayList<Integer>();
        for (int i = 0; i < mXyleixingList.size(); i++) {
            XyleixingModel item = mXyleixingList.get(i);
            for (int j = 0; j < item.getList().size(); j++) {
                if (item.getList().get(j).getIsMyWatched() == 1) {
                    if (!curIds1_person.contains(item.getList().get(j).getId()))
                        curIds1_person.add(item.getList().get(j).getId());
                }
                if (item.getList().get(j).getIsMyWatch() == 1) {
                    if (!curIds2_person.contains(item.getList().get(j).getId()))
                        curIds2_person.add(item.getList().get(j).getId());
                }
            }
        }
        mAdapter1.setDatas(true, (ArrayList<XyleixingModel>) mXyleixingList, curIds1_person);
        mAdapter2.setDatas(false, (ArrayList<XyleixingModel>) mXyleixingList, curIds2_person);

        gridView1.setAdapter(mAdapter1);
        gridView2.setAdapter(mAdapter2);

        imgLogo.setOnClickListener(mButtonClickListener);
        switch (mUser.getTestStatus()) {
            case Constants.TEST_STATUS_PASSED:
            case Constants.TEST_STATUS_READY:
                txtCertPhotoAdd.setVisibility(View.GONE);
                imgCertPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ImageViewActivity.class);
                        intent.putExtra("path", Constants.FILE_ADDR + mUser.getCertImage());
                        startActivity(intent);
                    }
                });
                break;
            case Constants.TEST_STATUS_REJECT:
            default:
                txtCertPhotoAdd.setVisibility(View.VISIBLE);
                txtCertPhotoAdd.setOnClickListener(mButtonClickListener);
                break;
        }

        // EnterPrise UI Controller
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imgLogoEnter.setLayoutParams(params2);
        Picasso.with(getContext()).load(Constants.FILE_ADDR + mUser.getLogo()).placeholder(R.drawable.add_touxiang).into(imgLogoEnter);
        Picasso.with(getContext()).load(Constants.FILE_ADDR + mUser.getEnterCertImage()).placeholder(R.drawable.cert_enter).into(imgCertPhotoEnter);
        if (mUser.getEnterKind() == Constants.ENTER_KIND_ENTERPRISE) {
            btnEnter1.setChecked(true);
            btnEnter2.setChecked(false);
        } else { // Constants.ENTER_KIND_PERSON
            btnEnter1.setChecked(false);
            btnEnter2.setChecked(true);
        }
        edtNameEnter.setText(mUser.getEnterName());
        edtAddrEnter.setText(mUser.getAddr());
        edtWeburlEnter.setText(mUser.getWeburl());
        edtWeixinEnter.setText(mUser.getWeixin());
        edtMainJobEnter.setText(mUser.getMainJob());
        edtCertNumEnter.setText(mUser.getEnterCertNum());
        edtCommentEnter.setText(mUser.getComment());
        edtRecommendEnter.setText(mUser.getRecommend());
        edtBossNameEnter.setText(mUser.getBossName());
        edtBossJobEnter.setText(mUser.getBossJob());
        edtBossMobileEnter.setText(mUser.getBossMobile());
        edtBossWeixinEnter.setText(mUser.getBossWeixin());
        txtSelectHangyeEnter.setText(mUser.getXyName());
        mHangyeIdEnter = mUser.getXyleixingId();
        txtSelectCityEnter.setText(mUser.getCityName());
        mCityIdEnter = mUser.getCityId();

        ArrayList<Integer> curIds1_enter = new ArrayList<Integer>();
        ArrayList<Integer> curIds2_enter = new ArrayList<Integer>();
        for (int i = 0; i < mXyleixingList.size(); i++) {
            XyleixingModel item = mXyleixingList.get(i);
            for (int j = 0; j < item.getList().size(); j++) {
                if (item.getList().get(j).getIsMyWatched() == 1) {
                    if (!curIds1_enter.contains(item.getList().get(j).getId()))
                        curIds1_enter.add(item.getList().get(j).getId());
                }
                if (item.getList().get(j).getIsMyWatch() == 1) {
                    if (!curIds2_enter.contains(item.getList().get(j).getId()))
                        curIds2_enter.add(item.getList().get(j).getId());
                }
            }
        }
        mAdapter1Enter.setDatas(true, (ArrayList<XyleixingModel>) mXyleixingList, curIds1_enter);
        mAdapter2Enter.setDatas(false, (ArrayList<XyleixingModel>) mXyleixingList, curIds2_enter);

        gridView1Enter.setAdapter(mAdapter1Enter);
        gridView2Enter.setAdapter(mAdapter2Enter);

        imgLogoEnter.setOnClickListener(mButtonClickListener);
        switch (mUser.getTestStatus()) {
            case Constants.TEST_STATUS_PASSED:
            case Constants.TEST_STATUS_READY:
                txtCertPhotoAddEnter.setVisibility(View.GONE);
                imgCertPhotoEnter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ImageViewActivity.class);
                        intent.putExtra("path", Constants.FILE_ADDR + mUser.getEnterCertImage());
                        startActivity(intent);
                    }
                });
                break;
            case Constants.TEST_STATUS_REJECT:
            default:
                txtCertPhotoAddEnter.setVisibility(View.VISIBLE);
                txtCertPhotoAddEnter.setOnClickListener(mButtonClickListener);
                break;
        }

        setDisableView();
    }

    private void setDisableView() {
        switch (mUser.getTestStatus()) {
            case Constants.TEST_STATUS_READY:
                // Common UI Controller
                btnPerson.setEnabled(false);
                btnEnterprise.setEnabled(false);
                btnSubmit.setEnabled(false);
                // Person UI Controller
                imgLogo.setEnabled(false);
                txtCertPhotoAdd.setEnabled(false);
                txtSelectHangye.setEnabled(false);
                txtSelectCity.setEnabled(false);
                edtRealname.setEnabled(false);
                edtCertNum.setEnabled(false);
                edtJob.setEnabled(false);
                edtWeixin.setEnabled(false);
                edtExperience.setEnabled(false);
                edtHistory.setEnabled(false);
                txtSelectEnterprise.setEnabled(false);
                // Enterprise UI Controller
                imgLogoEnter.setEnabled(false);
                btnEnter1.setEnabled(false);
                btnEnter2.setEnabled(false);
                edtNameEnter.setEnabled(false);
                edtAddrEnter.setEnabled(false);
                edtWeburlEnter.setEnabled(false);
                edtWeixinEnter.setEnabled(false);
                edtMainJobEnter.setEnabled(false);
                edtCertNumEnter.setEnabled(false);
                edtCommentEnter.setEnabled(false);
                edtRecommendEnter.setEnabled(false);
                edtBossNameEnter.setEnabled(false);
                edtBossJobEnter.setEnabled(false);
                edtBossMobileEnter.setEnabled(false);
                edtBossWeixinEnter.setEnabled(false);
                txtCertPhotoAddEnter.setEnabled(false);
                txtSelectHangyeEnter.setEnabled(false);
                txtSelectCityEnter.setEnabled(false);
                break;
            case Constants.TEST_STATUS_PASSED:
                // Common
                btnPerson.setEnabled(false);
                btnEnterprise.setEnabled(false);
                // Person
                edtRealname.setEnabled(false);
                txtCertPhotoAdd.setEnabled(false);
                // Enterprise
                btnEnter1.setEnabled(false);
                btnEnter2.setEnabled(false);
                edtNameEnter.setEnabled(false);
                edtMainJobEnter.setEnabled(false);
                txtCertPhotoAddEnter.setEnabled(false);
                txtSelectHangyeEnter.setEnabled(false);
//                edtAddrEnter.setEnabled(false);
                txtSelectCityEnter.setEnabled(false);
                break;
            case Constants.TEST_STATUS_REJECT:
                break;
            default:
                break;
        }
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            SelectGalleryDialog dlg;
            switch (v.getId()) {
                case R.id.btn_back:
                    BaseFragmentActivity parent = (BaseFragmentActivity)getActivity();
                    String parentClassName = getActivity().getClass().getName();
                    if (parentClassName.equals(MyRealnameCertActivity.class.getName()))
                        parent.onBackActivity();
                    else
                        parent.goBack();
                    break;
                case R.id.btn_person:
                    viewPerson.setVisibility(View.VISIBLE);
                    viewEnterprise.setVisibility(View.GONE);
                    btnPerson.setChecked(true);
                    btnEnterprise.setChecked(false);
                    break;
                case R.id.btn_enterprise:
                    viewPerson.setVisibility(View.GONE);
                    viewEnterprise.setVisibility(View.VISIBLE);
                    btnPerson.setChecked(false);
                    btnEnterprise.setChecked(true);
                    break;
                case R.id.btn_enter_1:
                    btnEnter1.setChecked(true);
                    btnEnter2.setChecked(false);
                    break;
                case R.id.btn_enter_2:
                    btnEnter1.setChecked(false);
                    btnEnter2.setChecked(true);
                    break;
                case R.id.txt_select_enterprise:
                    CommonUtils.hideKeyboardFrom(getActivity());
                    onSelectCompany();
                    break;
                case R.id.txt_select_city:
                case R.id.txt_select_city_enter:
                    CommonUtils.hideKeyboardFrom(getActivity());
                    onSelectCity();
                    break;
                case R.id.txt_select_hangye:
                case R.id.txt_select_hangye_enter:
                    CommonUtils.hideKeyboardFrom(getActivity());
                    onSelectHangye(v.getId());
                    break;
                case R.id.img_logo:
                case R.id.img_logo_enter:
                case R.id.txt_cert_photo_add:
                case R.id.txt_cert_photo_add_enter:
                    CommonUtils.hideKeyboardFrom(getActivity());
                    // add dd -- 2017.12.07
                    mIndex = PICK_FROM_GALLERY_PERSON_LOGO;
                    switch (v.getId()) {
                        case R.id.img_logo:
                            mIndex = PICK_FROM_GALLERY_PERSON_LOGO;
                            break;
                        case R.id.img_logo_enter:
                            mIndex = PICK_FROM_GALLERY_ENTER_LOGO;
                            break;
                        case R.id.txt_cert_photo_add:
                            mIndex = PICK_FROM_GALLERY_PERSON_CERT;
                            break;
                        case R.id.txt_cert_photo_add_enter:
                            mIndex = PICK_FROM_GALLERY_ENTER_CERT;
                            break;
                    }
                    CropImage.startPickImageActivity(getActivity());

//                    dlg = new SelectGalleryDialog(getContext(), new SelectGalleryDialog.OnSelectListener() {
//                        @Override
//                        public void onSelectCamera() {
//                            int index = PICK_FROM_CAMERA_PERSON_LOGO;
//                            switch (v.getId()) {
//                                case R.id.img_logo:
//                                    index = PICK_FROM_CAMERA_PERSON_LOGO;
//                                    break;
//                                case R.id.img_logo_enter:
//                                    index = PICK_FROM_CAMERA_ENTER_LOGO;
//                                    break;
//                                case R.id.txt_cert_photo_add:
//                                    index = PICK_FROM_CAMERA_PERSON_CERT;
//                                    break;
//                                case R.id.txt_cert_photo_add_enter:
//                                    index = PICK_FROM_CAMERA_ENTER_CERT;
//                                    break;
//                            }
//                            doTakeCameraAction(index);
//                        }
//                        @Override
//                        public void onSelectGallery() {
//                            int index = PICK_FROM_GALLERY_PERSON_LOGO;
//                            switch (v.getId()) {
//                                case R.id.img_logo:
//                                    index = PICK_FROM_GALLERY_PERSON_LOGO;
//                                    break;
//                                case R.id.img_logo_enter:
//                                    index = PICK_FROM_GALLERY_ENTER_LOGO;
//                                    break;
//                                case R.id.txt_cert_photo_add:
//                                    index = PICK_FROM_GALLERY_PERSON_CERT;
//                                    break;
//                                case R.id.txt_cert_photo_add_enter:
//                                    index = PICK_FROM_GALLERY_ENTER_CERT;
//                                    break;
//                            }
//                            doTakeGalleryAction(index);
//                        }
//                    });
//                    dlg.setCancelable(true);
//                    dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    dlg.show();
                    break;
                case R.id.btn_submit:
                    if (isCheckValidation()) {
                        onSubmit();
                    }
                    break;
            }
        }
    };

    private void onSubmit() {
        Map<String, String> sendParams = new HashMap<String, String>();
        ArrayList<Constants.UploadFileModel> fileModelList = new ArrayList<Constants.UploadFileModel>();

        String xyWatchStr = "", xyWatchedStr = "";
        for (int i = 0; i < mXyleixingList.size(); i++) {
            XyleixingModel item = mXyleixingList.get(i);
            for (int j = 0; j < item.getList().size(); j++) {
                if (item.getList().get(j).getIsMyWatch() == 1) {
                    xyWatchStr += item.getList().get(j).getId() + ",";
                }
                if (item.getList().get(j).getIsMyWatched() == 1) {
                    xyWatchedStr += item.getList().get(j).getId() + ",";
                }
            }
        }
        if (xyWatchStr.length() > 0) {
            xyWatchStr = xyWatchStr.substring(0, xyWatchStr.length() - 1);
        }
        if (xyWatchedStr.length() > 0) {
            xyWatchedStr = xyWatchedStr.substring(0, xyWatchedStr.length() - 1);
        }

        if (btnPerson.isChecked()) { // Person
            sendParams.put("realname", edtRealname.getText().toString().trim());
            sendParams.put("certNum", edtCertNum.getText().toString().trim());
            sendParams.put("enterId", "" + mEnterpriseId);
            sendParams.put("xyleixingId", "" + mHangyeId);
            sendParams.put("cityId", "" + mCityId);
            sendParams.put("job", edtJob.getText().toString().trim());
            sendParams.put("weixin", edtWeixin.getText().toString().trim());
            sendParams.put("experience", edtExperience.getText().toString().trim());
            sendParams.put("history", edtHistory.getText().toString().trim());
            sendParams.put("xyWatch", xyWatchStr);
            sendParams.put("xyWatched", xyWatchedStr);

            String tmpPath = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_PERSON_LOGO  + Constants.FILE_EXTENTION_AUTH_IMAGE);
            File tmpFile = new File(tmpPath);
            if (tmpFile.exists()) {
                Constants.UploadFileModel fileModel = new Constants.UploadFileModel();
                fileModel.fileTitle = "logo";
                fileModel.fileName = Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_PERSON_LOGO + Constants.FILE_EXTENTION_AUTH_IMAGE;
                fileModel.filePath = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_PERSON_LOGO + Constants.FILE_EXTENTION_AUTH_IMAGE);
                fileModelList.add(fileModel);
            }
            tmpPath = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_PERSON_CERT + Constants.FILE_EXTENTION_AUTH_IMAGE);
            tmpFile = new File(tmpPath);
            if (tmpFile.exists()) {
                Constants.UploadFileModel fileModel = new Constants.UploadFileModel();
                fileModel.fileTitle = "certImage";
                fileModel.fileName = Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_PERSON_CERT + Constants.FILE_EXTENTION_AUTH_IMAGE;
                fileModel.filePath = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_PERSON_CERT + Constants.FILE_EXTENTION_AUTH_IMAGE);
                fileModelList.add(fileModel);
            }
        } else { // Enterprise
            sendParams.put("enterKind", btnEnter1.isChecked() ? "1" : "2");
            sendParams.put("enterCertNum", edtCertNumEnter.getText().toString().trim());
            sendParams.put("enterName", edtNameEnter.getText().toString().trim());
            sendParams.put("xyleixingId", "" + mHangyeIdEnter);
            sendParams.put("cityId", "" + mCityIdEnter);
            sendParams.put("addr", edtAddrEnter.getText().toString().trim());
            sendParams.put("webUrl", edtWeburlEnter.getText().toString().trim());
            sendParams.put("weixin", edtWeixinEnter.getText().toString().trim());
            sendParams.put("mainJob", edtMainJobEnter.getText().toString().trim());
            sendParams.put("comment", edtCommentEnter.getText().toString().trim());
            sendParams.put("recommend", edtRecommendEnter.getText().toString().trim());
            sendParams.put("bossName", edtBossNameEnter.getText().toString().trim());
            sendParams.put("bossJob", edtBossJobEnter.getText().toString().trim());
            sendParams.put("bossMobile", edtBossMobileEnter.getText().toString().trim());
            sendParams.put("bossWeixin", edtBossWeixinEnter.getText().toString().trim());
            sendParams.put("xyWatch", xyWatchStr);
            sendParams.put("xyWatched", xyWatchedStr);

            String tmpPath = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_ENTER_LOGO + Constants.FILE_EXTENTION_AUTH_IMAGE);
            File tmpFile = new File(tmpPath);
            if (tmpFile.exists()) {
                Constants.UploadFileModel fileModel = new Constants.UploadFileModel();
                fileModel.fileTitle = "logo";
                fileModel.fileName = Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_ENTER_LOGO + Constants.FILE_EXTENTION_AUTH_IMAGE;
                fileModel.filePath = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_ENTER_LOGO + Constants.FILE_EXTENTION_AUTH_IMAGE);
                fileModelList.add(fileModel);
            }
            tmpPath = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_ENTER_CERT + Constants.FILE_EXTENTION_AUTH_IMAGE);
            tmpFile = new File(tmpPath);
            if (tmpFile.exists()) {
                Constants.UploadFileModel fileModel = new Constants.UploadFileModel();
                fileModel.fileTitle = "enterCertImage";
                fileModel.fileName = Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_ENTER_CERT + Constants.FILE_EXTENTION_AUTH_IMAGE;
                fileModel.filePath = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_ENTER_CERT + Constants.FILE_EXTENTION_AUTH_IMAGE);
                fileModelList.add(fileModel);
            }
        }
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                Utils.displayProgressDialog(getActivity());
            }
            @Override
            protected Object doInBackground(Object... params) {
                SyncInfo info = new SyncInfo(getContext());
                boolean flag = (boolean) params[0];
                Map<String, String> sendParams = (Map<String, String>) params[1];
                ArrayList<Constants.UploadFileModel> fileModelList = (ArrayList<Constants.UploadFileModel>) params[2];
                LoginModel result;
                if (flag) {
                    result = info.syncSubmitAuthPerson(sendParams, fileModelList);
                } else {
                    result = info.syncSubmitAuthEnterprise(sendParams, fileModelList);
                }

                return result;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                LoginModel result = (LoginModel) o;
                if (result .isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        SessionInstance.initialize(getActivity(), result);
                        BaseFragmentActivity parent = (BaseFragmentActivity)getActivity();
                        if (parent instanceof LoginActivity) {
                            ((LoginActivity) parent).toMainActivity();
                        } else {
                            Intent intent = new Intent(Constants.NOTIFY_USERMODEL_CHANGED);
                            getActivity().getBaseContext().sendBroadcast(intent);
                            parent.onBackActivity();
                        }
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, R.string.error_real_cert_failed, Toast.LENGTH_SHORT).show();
                }
                Utils.disappearProgressDialog();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute(btnPerson.isChecked(), sendParams, fileModelList);
    }

    private boolean isCheckValidation() {
        boolean flag = false;
        if (btnPerson.isChecked()) {
            if (edtRealname.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_realname, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (edtCertNum.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_certnum, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (edtCertNum.getText().toString().trim().length() != 18) {
                Toast.makeText(getContext(), R.string.pls_incorrect_certnum, Toast.LENGTH_SHORT).show();
                return flag;
            }
            // check cert_photo_file
            if (mUser.getCertImage() == null || mUser.getCertImage().length() == 0) {
                String filePath = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_PERSON_CERT + Constants.FILE_EXTENTION_AUTH_IMAGE);
                File tmpFile = new File(filePath);
                if (!tmpFile.exists()) {
                    Toast.makeText(getContext(), R.string.pls_insert_certphoto, Toast.LENGTH_SHORT).show();
                    return flag;
                }
            }
            if (mEnterpriseId == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_enterprise, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (mHangyeId == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_hangye, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (mCityId == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_city, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (edtJob.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_job, Toast.LENGTH_SHORT).show();
                return flag;
            }
        } else {
            if (btnEnter1.isChecked() == false && btnEnter2.isChecked() == false) {
                Toast.makeText(getContext(), R.string.pls_insert_enterleixing, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (edtNameEnter.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_name_enter, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (mHangyeIdEnter == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_hangye_enter, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (mCityIdEnter == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_city_enter, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (edtMainJobEnter.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_mainjob_enter, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (edtCertNumEnter.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_certnum_enter, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (edtCertNumEnter.getText().toString().trim().length() > 25) {
                Toast.makeText(getContext(), R.string.pls_incorrect_certnum_enter, Toast.LENGTH_SHORT).show();
                return flag;
            }
            // check cert_photo_file
            if (mUser.getEnterCertImage() == null || mUser.getEnterCertImage().length() == 0) {
                String filePath = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + PICK_FROM_GALLERY_ENTER_CERT + Constants.FILE_EXTENTION_AUTH_IMAGE);
                File tmpFile = new File(filePath);
                if (!tmpFile.exists()) {
                    Toast.makeText(getContext(), R.string.pls_insert_certphoto, Toast.LENGTH_SHORT).show();
                    return flag;
                }
            }
            if (edtCommentEnter.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_comment_enter, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (edtRecommendEnter.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_recommend_enter, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (edtBossNameEnter.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_bossname_enter, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (edtBossJobEnter.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_bossjob_enter, Toast.LENGTH_SHORT).show();
                return flag;
            }
            if (edtBossMobileEnter.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), R.string.pls_insert_bossmobile_enter, Toast.LENGTH_SHORT).show();
                return flag;
            }
        }
        return true;
    }

    //add dd -- 2017.12.07
    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri, int aspectRatioX, int aspectRatioY) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(aspectRatioX, aspectRatioY)
                .start(getActivity());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // add dd -- 2017.12.07
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
            // no permissions required or already grunted, can start crop image activity

            int aspectRatioX = 1;
            int aspectRatioY = 1;
            if (mIndex == PICK_FROM_GALLERY_PERSON_CERT || mIndex == PICK_FROM_GALLERY_ENTER_CERT) {
                aspectRatioX = 8;
                aspectRatioY = 5;
            }

            startCropImageActivity(imageUri, aspectRatioX, aspectRatioY);
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap photo = ResUtils.decodeUri(getContext(), result.getUri());
                    if(photo != null) {
                        try {
                            FileOutputStream fos = new FileOutputStream(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + mIndex + Constants.FILE_EXTENTION_AUTH_IMAGE));
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                            switch(mIndex) {
                                case PICK_FROM_GALLERY_PERSON_LOGO:
                                    imgLogo.setImageBitmap(photo);
                                    break;
                                case PICK_FROM_GALLERY_PERSON_CERT:
                                    imgCertPhoto.setImageBitmap(photo);
                                    break;
                                case PICK_FROM_GALLERY_ENTER_LOGO:
                                    imgLogoEnter.setImageBitmap(photo);
                                    break;
                                case PICK_FROM_GALLERY_ENTER_CERT:
                                    imgCertPhotoEnter.setImageBitmap(photo);
                                    break;
                            }

                            fos.flush();
                            fos.close();
                        } catch(Exception e) {
                            Log.e(TAG, "" + requestCode + " : " + e.toString());
                        }
                    } else {
                        Log.i(TAG, "Bitmap is null");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.i(TAG, "Crop Fail");
            }
        }


//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != Activity.RESULT_OK)
//            return;
//
//        switch(requestCode) {
//            case PICK_FROM_GALLERY_PERSON_LOGO:
//            case PICK_FROM_GALLERY_PERSON_CERT:
//            case PICK_FROM_GALLERY_ENTER_LOGO:
//            case PICK_FROM_GALLERY_ENTER_CERT:
//            {
//                Bitmap photo = data.getParcelableExtra("data");
//                if(photo != null) {
//                    try {
//                        FileOutputStream fos = new FileOutputStream(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + requestCode + Constants.FILE_EXTENTION_AUTH_IMAGE));
//                        photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//
//                        switch(requestCode) {
//                            case PICK_FROM_GALLERY_PERSON_LOGO:
//                                imgLogo.setImageBitmap(photo);
//                                break;
//                            case PICK_FROM_GALLERY_PERSON_CERT:
//                                imgCertPhoto.setImageBitmap(photo);
//                                break;
//                            case PICK_FROM_GALLERY_ENTER_LOGO:
//                                imgLogoEnter.setImageBitmap(photo);
//                                break;
//                            case PICK_FROM_GALLERY_ENTER_CERT:
//                                imgCertPhotoEnter.setImageBitmap(photo);
//                                break;
//                        }
//
//                        fos.flush();
//                        fos.close();
//                    } catch(Exception e) {
//                        Log.e(TAG, "" + requestCode + " : " + e.toString());
//                    }
//                } else {
//                    Log.i(TAG, "Bitmap is null");
//                }
//                break;
//            }
//            case PICK_FROM_CAMERA_PERSON_LOGO :
//            case PICK_FROM_CAMERA_PERSON_CERT :
//            case PICK_FROM_CAMERA_ENTER_LOGO :
//            case PICK_FROM_CAMERA_ENTER_CERT :
//            {
//                Uri imageCaptureUri = Uri.fromFile(new File(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + requestCode + Constants.FILE_EXTENTION_AUTH_IMAGE)));
//
//                Intent intent = new Intent("com.android.camera.action.CROP");
//                intent.setDataAndType(imageCaptureUri, "image/*");
//                if (requestCode == PICK_FROM_CAMERA_PERSON_LOGO || requestCode == PICK_FROM_CAMERA_ENTER_LOGO) {
//                    intent.putExtra("aspectX", (int) getContext().getResources().getDimension(R.dimen.image_avatar_width));
//                    intent.putExtra("aspectY", (int) getContext().getResources().getDimension(R.dimen.image_avatar_height));
//                    intent.putExtra("outputX", (int) getContext().getResources().getDimension(R.dimen.image_avatar_width));
//                    intent.putExtra("outputY", (int) getContext().getResources().getDimension(R.dimen.image_avatar_height));
//                }
//                if (requestCode == PICK_FROM_CAMERA_PERSON_CERT || requestCode == PICK_FROM_CAMERA_ENTER_CERT) {
//                    intent.putExtra("aspectX", (int) getContext().getResources().getDimension(R.dimen.image_cert_width));
//                    intent.putExtra("aspectY", (int) getContext().getResources().getDimension(R.dimen.image_cert_height));
//                    intent.putExtra("outputX", (int) getContext().getResources().getDimension(R.dimen.image_cert_width));
//                    intent.putExtra("outputY", (int) getContext().getResources().getDimension(R.dimen.image_cert_height));
//                }
//                intent.putExtra("scale", true);
//                intent.putExtra("return-data", true);
//
//                int reReqCode = PICK_FROM_GALLERY_PERSON_LOGO;
//                switch(requestCode) {
//                    case PICK_FROM_CAMERA_PERSON_LOGO:
//                        reReqCode = PICK_FROM_GALLERY_PERSON_LOGO;
//                        break;
//                    case PICK_FROM_CAMERA_PERSON_CERT:
//                        reReqCode = PICK_FROM_GALLERY_PERSON_CERT;
//                        break;
//                    case PICK_FROM_CAMERA_ENTER_LOGO:
//                        reReqCode = PICK_FROM_GALLERY_ENTER_LOGO;
//                        break;
//                    case PICK_FROM_CAMERA_ENTER_CERT:
//                        reReqCode = PICK_FROM_GALLERY_ENTER_CERT;
//                        break;
//                }
//                startActivityForResult(intent, reReqCode);
//            }
//        }
    }

    private void doTakeCameraAction(int selIndex) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + selIndex + Constants.FILE_EXTENTION_AUTH_IMAGE));
        file.deleteOnExit();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, selIndex);
    }

    private void doTakeGalleryAction(int selIndex) {
        //声明意图
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        if (selIndex == PICK_FROM_GALLERY_PERSON_LOGO || selIndex == PICK_FROM_GALLERY_ENTER_LOGO) {
            intent.putExtra("aspectX", (int) getContext().getResources().getDimension(R.dimen.image_avatar_width));
            intent.putExtra("aspectY", (int) getContext().getResources().getDimension(R.dimen.image_avatar_height));
            intent.putExtra("outputX", (int) getContext().getResources().getDimension(R.dimen.image_avatar_width));
            intent.putExtra("outputY", (int) getContext().getResources().getDimension(R.dimen.image_avatar_height));
        }
        if (selIndex == PICK_FROM_GALLERY_PERSON_CERT || selIndex == PICK_FROM_GALLERY_ENTER_CERT) {
            intent.putExtra("aspectX", (int) getContext().getResources().getDimension(R.dimen.image_cert_width));
            intent.putExtra("aspectY", (int) getContext().getResources().getDimension(R.dimen.image_cert_height));
            intent.putExtra("outputX", (int) getContext().getResources().getDimension(R.dimen.image_cert_width));
            intent.putExtra("outputY", (int) getContext().getResources().getDimension(R.dimen.image_cert_height));
        }
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, selIndex);
    }

    private void onSelectCompany() {
        int visibility = viewCondition.getVisibility();
        if (visibility == View.GONE) {
            if (mCompanyListView == null) {
                mCompanyListView = new CompanyListView(getContext(), "公司目录", new CompanyListView.OnCompanySelectListener() {
                    @Override
                    public void onCompanySelected(int id, String name, int xyleixingId, String xyName) {
                        if (id != -1 && name != null) {
                            mEnterpriseId = id;
                            txtSelectEnterprise.setText(name);
                            mHangyeId = xyleixingId;
                            txtSelectHangye.setText(xyName);
                        }
                        mCompanyListView.setVisibility(View.GONE);
                        viewCondition.setVisibility(View.GONE);
                        viewConditionBody.setVisibility(View.GONE);
                    }
                }, new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        mCompanyListView.setVisibility(View.GONE);
                        viewCondition.setVisibility(View.GONE);
                        viewConditionBody.setVisibility(View.GONE);
                    }
                });
                mCompanyListView.setCurrentCompanyName(mUser.getEnterName());
                viewConditionBody.addView(mCompanyListView);
            }
            mCompanyListView.setVisibility(View.VISIBLE);

            viewCondition.setVisibility(View.VISIBLE);
            viewConditionBody.setVisibility(View.VISIBLE);
            CommonUtils.animationShowFromRight(viewConditionBody);
        } else {
            viewCondition.setVisibility(View.GONE);
            viewConditionBody.setVisibility(View.GONE);
            mCompanyListView.setVisibility(View.GONE);
        }
    }

    private void onSelectCity() {
        if (mCityDialog != null) {
            mCityDialog.cancel();
            mCityDialog = null;
        }
        int cityId = mCityId;
        if (btnEnterprise.isChecked()) {
            cityId = mCityIdEnter;
        }
        mCityDialog = new SelectCityDialog(mContext, cityId, new SelectCityDialog.OnCitySelectListener() {
            @Override
            public void OnCitySelected(CityModel city) {
                if (city != null) {
                    if (btnPerson.isChecked()) {
                        mCityId = city.getId();
                        txtSelectCity.setText(city.getProvinceName() + ", " + city.getName());
                    } else {
                        mCityIdEnter = city.getId();
                        txtSelectCityEnter.setText(city.getProvinceName() + ", " + city.getName());
                    }
                }
            }
        });

        mCityDialog.setCancelable(true);
        mCityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCityDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mCityDialog.show();
    }

    private void onSelectHangye(int resId) {
        int visibility = viewCondition.getVisibility();
        if (visibility == View.GONE) {
            if (resId == R.id.txt_select_hangye) {
                if (mHangyeListViewPerson == null) {
                    mHangyeListViewPerson = new MeHangyeListView(getContext(), new MeHangyeListView.OnHangyeSelectListener() {
                        @Override
                        public void OnHangyeSelected(int curId, String curTitle) {
                            if (curId != -1 && curTitle != null) {
                                mHangyeId = curId;
                                txtSelectHangye.setText(curTitle);
                            }
                            mHangyeListViewPerson.setVisibility(View.GONE);
                            viewCondition.setVisibility(View.GONE);
                            viewConditionBody.setVisibility(View.GONE);
                        }
                    }, new OnCancelListener() {
                        @Override
                        public void onCancel() {
                            mHangyeListViewPerson.setVisibility(View.GONE);
                            viewCondition.setVisibility(View.GONE);
                            viewConditionBody.setVisibility(View.GONE);
                        }
                    });
                    mHangyeListViewPerson.setData(getString(R.string.hangye), mXyleixingList, mUser.getXyleixingId());
                    viewConditionBody.addView(mHangyeListViewPerson);
                }
                mHangyeListViewPerson.setVisibility(View.VISIBLE);
            }
            if (resId == R.id.txt_select_hangye_enter) {
                if (mHangyeListViewEnter == null) {
                    mHangyeListViewEnter = new MeHangyeListView(getContext(), new MeHangyeListView.OnHangyeSelectListener() {
                        @Override
                        public void OnHangyeSelected(int curId, String curTitle) {
                            if (curId != -1 && curTitle != null) {
                                mHangyeIdEnter = curId;
                                txtSelectHangyeEnter.setText(curTitle);
                            }
                            mHangyeListViewEnter.setVisibility(View.GONE);
                            viewCondition.setVisibility(View.GONE);
                            viewConditionBody.setVisibility(View.GONE);
                        }
                    }, new OnCancelListener() {
                        @Override
                        public void onCancel() {
                            mHangyeListViewEnter.setVisibility(View.GONE);
                            viewCondition.setVisibility(View.GONE);
                            viewConditionBody.setVisibility(View.GONE);
                        }
                    });
                    mHangyeListViewEnter.setData(getString(R.string.hangye), mXyleixingList, mUser.getXyleixingId());
                    viewConditionBody.addView(mHangyeListViewEnter);
                }
                mHangyeListViewEnter.setVisibility(View.VISIBLE);
            }
            viewCondition.setVisibility(View.VISIBLE);
            viewConditionBody.setVisibility(View.VISIBLE);
            CommonUtils.animationShowFromRight(viewConditionBody);
        } else {
            viewCondition.setVisibility(View.GONE);
            viewConditionBody.setVisibility(View.GONE);
            if (resId == R.id.txt_select_hangye) {
                mHangyeListViewPerson.setVisibility(View.GONE);
            }
            if (resId == R.id.txt_select_hangye_enter) {
                mHangyeListViewEnter.setVisibility(View.GONE);
            }
        }
    }

    public class CustomWatchListAdapter extends BaseAdapter {
        private ArrayList<XyleixingModel> mDatas;
        private ArrayList<Integer> mCurrentIds;
        private boolean mIsWatched = true;

        public void setDatas(boolean isWatched, ArrayList<XyleixingModel> datas, ArrayList<Integer> curIds) {
            mIsWatched = isWatched;
            this.mDatas = datas;
            if (mDatas == null) {
                mDatas = new ArrayList<XyleixingModel>();
            }
            this.mCurrentIds = curIds;
            if (mCurrentIds ==  null) {
                mCurrentIds = new ArrayList<Integer>();
            }
            notifyDataSetChanged();
        }

        public ArrayList<Integer> getCurrentIds() {
            return mCurrentIds;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas == null ? null : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewWatchHolder holder = new ViewWatchHolder();

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_me_hangye, null);

                holder.txtTitle = (Button) convertView.findViewById(R.id.txt_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewWatchHolder) convertView.getTag();
            }

            convertView.setId(position);

            final XyleixingModel item = (XyleixingModel) getItem(position);

            holder.id = item.getId();
            holder.txtTitle.setText(item.getTitle());

            /*if (mCurrentIds.contains(item.getId()))
                holder.txtTitle.setActivated(true);
            else
                holder.txtTitle.setActivated(false);*/

            boolean isChecked = false;
            for (int i = 0; i < item.getList().size(); i++) {
                XyleixingModel tmp = item.getList().get(i);
                if (mIsWatched) {
                    if (tmp.getIsMyWatched() == 1 && mCurrentIds.contains(tmp.getId())) {
                        isChecked = true;
                        break;
                    }
                } else {
                    if (tmp.getIsMyWatch() == 1 && mCurrentIds.contains(tmp.getId())) {
                        isChecked = true;
                        break;
                    }
                }
            }
            holder.txtTitle.setActivated(isChecked);

            final ViewWatchHolder finalHolder = holder;
            if (mUser.getTestStatus() != Constants.TEST_STATUS_READY) {
                holder.txtTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ArrayList<XyleixingModel> itemList = (ArrayList<XyleixingModel>) item.getList();
                        final MeHangyeListDialog dialog = new MeHangyeListDialog(getContext(), mIsWatched, item.getTitle(), itemList, new MeHangyeListDialog.OnOkClickListener() {
                            @Override
                            public void onOk() {
                                boolean flag = false;
                                for (int i = 0; i < itemList.size(); i++) {
                                    if (mIsWatched == true) {
                                        if (itemList.get(i).getIsMyWatched() == 1) {
                                            flag = true;
                                            break;
                                        }
                                    } else {
                                        if (itemList.get(i).getIsMyWatch() == 1) {
                                            flag = true;
                                            break;
                                        }
                                    }
                                }
                                finalHolder.txtTitle.setActivated(flag);
                            }
                        });
                        dialog.setCancelable(true);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });
            }

            return convertView;
        }
    }

    private class ViewWatchHolder {
        public int id;
        public Button txtTitle;
    }

    private void getDataTask() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.displayProgressDialog(mContext);
            }
            @Override
            protected Object doInBackground(Object... params) {
                for (int i = PICK_FROM_CAMERA_PERSON_LOGO; i <= PICK_FROM_GALLERY_ENTER_CERT; i++) {
                    String filePath = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + i + Constants.FILE_EXTENTION_AUTH_IMAGE);
                    File tmpFile = new File(filePath);
                    if (tmpFile.exists()) {
                        tmpFile.delete();
                    }
                }

                AccountModel result1 = new SyncInfo(mContext).syncAccountInfo(0);
                SessionInstance.getInstance().getLoginData().setUser(result1.getAccount());
                mUser = result1.getAccount();

                XyleixingListModel data = new SyncInfo(mContext).syncXyleixingList("");
                publishProgress(data);

                return null;
            }
            @Override
            protected void onProgressUpdate(Object... values) {
                super.onProgressUpdate(values);
                XyleixingListModel result = (XyleixingListModel) values[0];
                if (result.isValid()) {
                    if (result.getRetCode() == Constants.ERROR_OK) {
                        mXyleixingList = (ArrayList<XyleixingModel>) result.getList();
                        initUI();
                    } else {
                        Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, R.string.error_real_cert_failed, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                //mAdapter.notifyDataSetChanged();
                Utils.disappearProgressDialog();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                Utils.disappearProgressDialog();
            }
        }.execute();
    }

}
