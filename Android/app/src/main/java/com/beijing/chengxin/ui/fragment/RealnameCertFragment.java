package com.beijing.chengxin.ui.fragment;

import android.app.Activity;
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
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SessionInstance;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.LoginModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.network.model.XyleixingModel;
import com.beijing.chengxin.ui.activity.MyRealnameCertActivity;
import com.beijing.chengxin.ui.dialog.MeHangyeListDialog;
import com.beijing.chengxin.ui.dialog.SelectGalleryDialog;
import com.beijing.chengxin.ui.listener.OnCancelListener;
import com.beijing.chengxin.ui.view.CityListView;
import com.beijing.chengxin.ui.view.MeHangyeListView;
import com.beijing.chengxin.ui.widget.GridView;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class RealnameCertFragment extends Fragment {
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

    private CityListView mCityListViewPerson, mCityListViewEnter;
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
    FrameLayout viewConditionBody;

    // Person UI Controller
    ImageView imgLogo, imgCertPhoto;
    TextView txtCertPhotoAdd, txtSelectHangye, txtSelectCity;
    EditText edtRealname, edtCertNum, edtJob, edtWeixin, edtExperience, edtHistory, txtSelectEnterprise;
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
        rootView = inflater.inflate(R.layout.activity_my_realname_cert, container, false);

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

        //Person UI Controller
        imgLogo = (ImageView) rootView.findViewById(R.id.img_logo);
        imgCertPhoto = (ImageView) rootView.findViewById(R.id.img_cert_photo);
        txtCertPhotoAdd = (TextView) rootView.findViewById(R.id.txt_cert_photo_add);
        txtSelectEnterprise = (EditText) rootView.findViewById(R.id.txt_select_enterprise);
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

        txtSelectHangyeEnter.setOnClickListener(mButtonClickListener);
        txtSelectCityEnter.setOnClickListener(mButtonClickListener);

        mUser = SessionInstance.getInstance().getLoginData().getUser();

        mAdapter1 = new CustomWatchListAdapter();
        mAdapter2 = new CustomWatchListAdapter();
        mAdapter1Enter = new CustomWatchListAdapter();
        mAdapter2Enter = new CustomWatchListAdapter();

        initUI();

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
                submitStringId = R.string.edit;
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
        Picasso.with(getContext()).load(Constants.FILE_ADDR + mUser.getLogo()).placeholder(R.drawable.no_image).into(imgLogo);
        Picasso.with(getContext()).load(Constants.FILE_ADDR + mUser.getCertImage()).placeholder(R.drawable.no_image).into(imgCertPhoto);
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
        for (int i = 0; i < AppConfig.getInstance().xyleixingList.size(); i++) {
            XyleixingModel item = AppConfig.getInstance().xyleixingList.get(i);
            if (item.getIsMyWatched() == 1) {
                if (!curIds1_person.contains(item.getId()))
                    curIds1_person.add(item.getId());
            }
            if (item.getIsMyWatch() == 1) {
                if (!curIds2_person.contains(item.getId()))
                    curIds2_person.add(item.getId());
            }
        }
        mAdapter1.setDatas(true, (ArrayList<XyleixingModel>) AppConfig.getInstance().xyleixingList, curIds1_person);
        mAdapter2.setDatas(false, (ArrayList<XyleixingModel>) AppConfig.getInstance().xyleixingList, curIds2_person);

        gridView1.setAdapter(mAdapter1);
        gridView2.setAdapter(mAdapter2);

        imgLogo.setOnClickListener(mButtonClickListener);
        txtCertPhotoAdd.setOnClickListener(mButtonClickListener);
        // EnterPrise UI Controller
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imgLogoEnter.setLayoutParams(params2);
        Picasso.with(getContext()).load(Constants.FILE_ADDR + mUser.getLogo()).placeholder(R.drawable.no_image).into(imgLogoEnter);
        Picasso.with(getContext()).load(Constants.FILE_ADDR + mUser.getEnterCertImage()).placeholder(R.drawable.no_image).into(imgCertPhotoEnter);
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
        edtCertNumEnter.setText(mUser.getCertNum());
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
        for (int i = 0; i < AppConfig.getInstance().xyleixingList.size(); i++) {
            XyleixingModel item = AppConfig.getInstance().xyleixingList.get(i);
            if (item.getIsMyWatched() == 1) {
                if (!curIds1_enter.contains(item.getId()))
                    curIds1_enter.add(item.getId());
            }
            if (item.getIsMyWatch() == 1) {
                if (!curIds2_enter.contains(item.getId()))
                    curIds2_enter.add(item.getId());
            }
        }
        mAdapter1Enter.setDatas(true, (ArrayList<XyleixingModel>) AppConfig.getInstance().xyleixingList, curIds1_enter);
        mAdapter2Enter.setDatas(false, (ArrayList<XyleixingModel>) AppConfig.getInstance().xyleixingList, curIds2_enter);

        gridView1Enter.setAdapter(mAdapter1Enter);
        gridView2Enter.setAdapter(mAdapter2Enter);

        imgLogoEnter.setOnClickListener(mButtonClickListener);
        txtCertPhotoAddEnter.setOnClickListener(mButtonClickListener);

        setDisableView();
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                for (int i = PICK_FROM_CAMERA_PERSON_LOGO; i <= PICK_FROM_GALLERY_ENTER_CERT; i++) {
                    String filePath = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + i + Constants.FILE_EXTENTION_AUTH_IMAGE);
                    File tmpFile = new File(filePath);
                    if (tmpFile.exists()) {
                        tmpFile.delete();
                    }
                }
                return null;
            }
        }.execute();
    }

    private void setDisableView() {
        switch (mUser.getTestStatus()) {
            case Constants.TEST_STATUS_READY:
                // Common UI Controller
                btnPerson.setEnabled(false);
                btnEnterprise.setEnabled(false);
                btnSubmit.setEnabled(false);
                // Person UI Controller
                txtCertPhotoAdd.setEnabled(false);
                txtSelectEnterprise.setEnabled(false);
                txtSelectHangye.setEnabled(false);
                txtSelectCity.setEnabled(false);
                edtRealname.setEnabled(false);
                edtCertNum.setEnabled(false);
                edtJob.setEnabled(false);
                edtWeixin.setEnabled(false);
                edtExperience.setEnabled(false);
                edtHistory.setEnabled(false);
                // Enterprise UI Controller
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
                // Enterprise
                btnEnter1.setEnabled(false);
                btnEnter2.setEnabled(false);
                edtNameEnter.setEnabled(false);
                edtMainJobEnter.setEnabled(false);
                txtCertPhotoAddEnter.setEnabled(false);
                txtSelectHangyeEnter.setEnabled(false);
                edtAddrEnter.setEnabled(false);
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
                case R.id.txt_select_enterprise:
                    break;
                case R.id.txt_select_city:
                case R.id.txt_select_city_enter:
                    onSelectCity(v.getId());
                    break;
                case R.id.txt_select_hangye:
                case R.id.txt_select_hangye_enter:
                    onSelectHangye(v.getId());
                    break;
                case R.id.img_logo:
                case R.id.img_logo_enter:
                case R.id.txt_cert_photo_add:
                case R.id.txt_cert_photo_add_enter:
                    dlg = new SelectGalleryDialog(getContext(), new SelectGalleryDialog.OnSelectListener() {
                        @Override
                        public void onSelectCamera() {
                            int index = PICK_FROM_CAMERA_PERSON_LOGO;
                            switch (v.getId()) {
                                case R.id.img_logo:
                                    index = PICK_FROM_CAMERA_PERSON_LOGO;
                                    break;
                                case R.id.img_logo_enter:
                                    index = PICK_FROM_CAMERA_ENTER_LOGO;
                                    break;
                                case R.id.txt_cert_photo_add:
                                    index = PICK_FROM_CAMERA_PERSON_CERT;
                                    break;
                                case R.id.txt_cert_photo_add_enter:
                                    index = PICK_FROM_CAMERA_ENTER_CERT;
                                    break;
                            }
                            doTakeCameraAction(index);
                        }
                        @Override
                        public void onSelectGallery() {
                            int index = PICK_FROM_GALLERY_PERSON_LOGO;
                            switch (v.getId()) {
                                case R.id.img_logo:
                                    index = PICK_FROM_GALLERY_PERSON_LOGO;
                                    break;
                                case R.id.img_logo_enter:
                                    index = PICK_FROM_GALLERY_ENTER_LOGO;
                                    break;
                                case R.id.txt_cert_photo_add:
                                    index = PICK_FROM_GALLERY_PERSON_CERT;
                                    break;
                                case R.id.txt_cert_photo_add_enter:
                                    index = PICK_FROM_GALLERY_ENTER_CERT;
                                    break;
                            }
                            doTakeGalleryAction(index);
                        }
                    });
                    dlg.setCancelable(true);
                    dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
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
        for (int i = 0; i < AppConfig.getInstance().xyleixingList.size(); i++) {
            XyleixingModel item = AppConfig.getInstance().xyleixingList.get(i);
            if (item.getIsMyWatch() == 1) {
                xyWatchStr = "" + item.getId() + ",";
            }
            if (item.getIsMyWatched() == 1) {
                xyWatchedStr = "" + item.getId() + ",";
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
            sendParams.put("enterName", "");
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
                if (flag) {
                    return info.syncSubmitAuthPerson(sendParams, fileModelList);
                } else {
                    return info.syncSubmitAuthEnterprise(sendParams, fileModelList);
                }
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                BaseModel result = (BaseModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        reloadUserModel();
                    } else {
                        Toast.makeText(getContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.error_real_cert_failed, Toast.LENGTH_SHORT).show();
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

    private void reloadUserModel() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                Utils.displayProgressDialog(getActivity());
            }
            @Override
            protected Object doInBackground(Object... params) {
                SyncInfo info = new SyncInfo(getContext());
                return info.syncLogin(ChengxinApplication.LOGIN_MOBILE, ChengxinApplication.LOGIN_PASSWORD);
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                LoginModel result = (LoginModel) o;
                if (result .isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        SessionInstance.initialize(getActivity(), result);
                    }
                }
                Intent intent = new Intent(Constants.NOTIFY_USERMODEL_CHANGED);
                getActivity().getBaseContext().sendBroadcast(intent);
                Utils.disappearProgressDialog();
                ((BaseFragmentActivity) getActivity()).onBackActivity();
            }
        }.execute();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;

        switch(requestCode) {
            case PICK_FROM_GALLERY_PERSON_LOGO:
            case PICK_FROM_GALLERY_PERSON_CERT:
            case PICK_FROM_GALLERY_ENTER_LOGO:
            case PICK_FROM_GALLERY_ENTER_CERT:
            {
                Bitmap photo = data.getParcelableExtra("data");
                if(photo != null) {
                    try {
                        FileOutputStream fos = new FileOutputStream(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + requestCode + Constants.FILE_EXTENTION_AUTH_IMAGE));
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        switch(requestCode) {
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
                break;
            }
            case PICK_FROM_CAMERA_PERSON_LOGO :
            case PICK_FROM_CAMERA_PERSON_CERT :
            case PICK_FROM_CAMERA_ENTER_LOGO :
            case PICK_FROM_CAMERA_ENTER_CERT :
            {
                if (data != null) {
                    Bitmap photo = data.getParcelableExtra("data");
                    if(photo != null) {
                        try {
                            FileOutputStream fos = new FileOutputStream(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + requestCode + Constants.FILE_EXTENTION_AUTH_IMAGE));
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                            switch(requestCode) {
                                case PICK_FROM_CAMERA_PERSON_LOGO:
                                    imgLogo.setImageBitmap(photo);
                                    break;
                                case PICK_FROM_CAMERA_PERSON_CERT:
                                    imgCertPhoto.setImageBitmap(photo);
                                    break;
                                case PICK_FROM_CAMERA_ENTER_LOGO:
                                    imgLogoEnter.setImageBitmap(photo);
                                    break;
                                case PICK_FROM_CAMERA_ENTER_CERT:
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
                }

                Uri imageCaptureUri = Uri.fromFile(new File(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_AUTH_IMAGE + requestCode + Constants.FILE_EXTENTION_AUTH_IMAGE)));

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(imageCaptureUri, "image/*");
                if (requestCode == PICK_FROM_CAMERA_PERSON_LOGO || requestCode == PICK_FROM_CAMERA_ENTER_LOGO) {
                    intent.putExtra("aspectX", (int) getContext().getResources().getDimension(R.dimen.image_avatar_width));
                    intent.putExtra("aspectY", (int) getContext().getResources().getDimension(R.dimen.image_avatar_height));
                    intent.putExtra("outputX", (int) getContext().getResources().getDimension(R.dimen.image_avatar_width));
                    intent.putExtra("outputY", (int) getContext().getResources().getDimension(R.dimen.image_avatar_height));
                }
                if (requestCode == PICK_FROM_CAMERA_PERSON_CERT || requestCode == PICK_FROM_CAMERA_ENTER_CERT) {
                    intent.putExtra("aspectX", (int) getContext().getResources().getDimension(R.dimen.image_cert_width));
                    intent.putExtra("aspectY", (int) getContext().getResources().getDimension(R.dimen.image_cert_height));
                    intent.putExtra("outputX", (int) getContext().getResources().getDimension(R.dimen.image_cert_width));
                    intent.putExtra("outputY", (int) getContext().getResources().getDimension(R.dimen.image_cert_height));
                }
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);

                int reReqCode = PICK_FROM_GALLERY_PERSON_LOGO;
                switch(requestCode) {
                    case PICK_FROM_CAMERA_PERSON_LOGO:
                        reReqCode = PICK_FROM_GALLERY_PERSON_LOGO;
                        break;
                    case PICK_FROM_CAMERA_PERSON_CERT:
                        reReqCode = PICK_FROM_GALLERY_PERSON_CERT;
                        break;
                    case PICK_FROM_CAMERA_ENTER_LOGO:
                        reReqCode = PICK_FROM_GALLERY_ENTER_LOGO;
                        break;
                    case PICK_FROM_CAMERA_ENTER_CERT:
                        reReqCode = PICK_FROM_GALLERY_ENTER_CERT;
                        break;
                }
                startActivityForResult(intent, reReqCode);
            }
        }
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
        Intent intent = new Intent(Intent.ACTION_PICK, null);
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
        intent.putExtra("return-data", true);
        //设置类型，地址
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, selIndex);
    }

    private void onSelectCity(int resId) {
        int visibility = viewCondition.getVisibility();
        if (visibility == View.GONE) {
            if (resId == R.id.txt_select_city) {
                if (mCityListViewPerson == null) {
                    mCityListViewPerson = new CityListView(getContext(), new CityListView.OnCitySelectListener() {
                        @Override
                        public void onCitySelected(int cityId, String cityName) {
                            if (cityId != -1 && cityName != null) {
                                mCityId = cityId;
                                txtSelectCity.setText(cityName);
                            }
                            mCityListViewPerson.setVisibility(View.GONE);
                            viewCondition.setVisibility(View.GONE);
                            viewConditionBody.setVisibility(View.GONE);
                        }
                    }, new OnCancelListener() {
                        @Override
                        public void onCancel() {
                            mCityListViewPerson.setVisibility(View.GONE);
                            viewCondition.setVisibility(View.GONE);
                            viewConditionBody.setVisibility(View.GONE);
                        }
                    });
                    viewConditionBody.addView(mCityListViewPerson);
                }
                mCityListViewPerson.setVisibility(View.VISIBLE);
            }
            if (resId == R.id.txt_select_city_enter) {
                if (mCityListViewEnter == null) {
                    mCityListViewEnter = new CityListView(getContext(), new CityListView.OnCitySelectListener() {
                        @Override
                        public void onCitySelected(int cityId, String cityName) {
                            if (cityId != -1 && cityName != null) {
                                mCityIdEnter = cityId;
                                txtSelectCityEnter.setText(cityName);
                            }
                            mCityListViewEnter.setVisibility(View.GONE);
                            viewCondition.setVisibility(View.GONE);
                            viewConditionBody.setVisibility(View.GONE);
                        }
                    }, new OnCancelListener() {
                        @Override
                        public void onCancel() {
                            mCityListViewEnter.setVisibility(View.GONE);
                            viewCondition.setVisibility(View.GONE);
                            viewConditionBody.setVisibility(View.GONE);
                        }
                    });
                    viewConditionBody.addView(mCityListViewEnter);
                }
                mCityListViewEnter.setVisibility(View.VISIBLE);
            }
            viewCondition.setVisibility(View.VISIBLE);
            viewConditionBody.setVisibility(View.VISIBLE);
            CommonUtils.animationShowFromRight(viewConditionBody);
        } else {
            viewCondition.setVisibility(View.GONE);
            viewConditionBody.setVisibility(View.GONE);
            if (resId == R.id.txt_select_city) {
                mCityListViewPerson.setVisibility(View.GONE);
            }
            if (resId == R.id.txt_select_city_enter) {
                mCityListViewEnter.setVisibility(View.GONE);
            }
        }
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
                    mHangyeListViewPerson.setData(AppConfig.getInstance().xyleixingList, mUser.getXyleixingId());
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
                    mHangyeListViewEnter.setData(AppConfig.getInstance().xyleixingList, mUser.getXyleixingId());
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

            if (mCurrentIds.contains(item.getId()))
                holder.txtTitle.setActivated(true);
            else
                holder.txtTitle.setActivated(false);

            final ViewWatchHolder finalHolder = holder;
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

            return convertView;
        }
    }

    private static class ViewWatchHolder {
        public int id;
        public Button txtTitle;
    }

}