package com.beijing.chengxin.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.AppConfig;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.CityModel;
import com.beijing.chengxin.ui.dialog.SelectCityDialog;
import com.beijing.chengxin.ui.dialog.SelectGalleryDialog;
import com.beijing.chengxin.ui.listener.OnCancelListener;
import com.beijing.chengxin.ui.view.FenleiGridView;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;

import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.PICK_FROM_CAMERA;

public class MakeServeActivity extends ParentFragmentActivity {

    public final String TAG = MakeServeActivity.class.getName();
    private  static int MAX_LETTER_COUNT = 300;

    private ImageButton  btnBack;
    private ImageView img_logo;
    private EditText edit_name, edit_comment, edit_net, edit_address, edit_person_name, edit_phone_number, edit_weixin;
    private  TextView txt_fenlei, txt_letter_count;
    private Button btn_publish;
    private ToggleButton btn_first_show;

    LinearLayout viewCondition;
    FrameLayout viewConditionBody;
    private FenleiGridView mFenleiGridView;
    int mFenleiId;

    String logoUrl = "";

    SyncInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_serve);
        // set title

        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.make_serve));

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        img_logo = (ImageView) findViewById(R.id.img_logo);

        btnBack.setOnClickListener(mClickListener);
        img_logo.setOnClickListener(mClickListener);

        txt_fenlei = (TextView)findViewById(R.id.txt_fenlei);
        btn_publish  = (Button) findViewById(R.id.btn_publish);
        txt_fenlei.setOnClickListener(mClickListener);
        btn_publish.setOnClickListener(mClickListener);

        txt_letter_count = (TextView)findViewById(R.id.txt_letter_count);
        txt_letter_count.setText(String.format("0/%d", MAX_LETTER_COUNT));

        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_comment = (EditText)findViewById(R.id.edit_comment);
        edit_net = (EditText)findViewById(R.id.edit_net);
        edit_address = (EditText)findViewById(R.id.edit_address);
        edit_person_name = (EditText)findViewById(R.id.edit_person_name);
        edit_phone_number = (EditText)findViewById(R.id.edit_phone_number);
        edit_weixin = (EditText)findViewById(R.id.edit_weixin);
        btn_first_show = (ToggleButton)findViewById(R.id.btn_first_show);

        edit_comment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String content = edit_comment.getText().toString();
                int length = content.length();
                txt_letter_count.setText(String.format("%d/%d", length, MAX_LETTER_COUNT));

                if (length >= MAX_LETTER_COUNT) {
                    edit_comment.setText(content.substring(0, MAX_LETTER_COUNT));
                    return true;
                }
                return false;
            }
        });

        viewCondition = (LinearLayout) findViewById(R.id.view_condition);
        viewConditionBody = (FrameLayout) findViewById(R.id.view_condition_body);

        info = new SyncInfo(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_logo:
                    SelectGalleryDialog dlg = new SelectGalleryDialog(MakeServeActivity.this, new SelectGalleryDialog.OnSelectListener() {
                        @Override
                        public void onSelectCamera() {
                            doTakeCameraAction();
                        }
                        @Override
                        public void onSelectGallery() {
                            doTakeGallaryAction();
                        }
                    });
                    dlg.setCancelable(true);
                    dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                    break;
                case R.id.txt_fenlei:
                    if (mFenleiGridView == null) {
                        mFenleiGridView = new FenleiGridView(MakeServeActivity.this, new FenleiGridView.OnFenleiSelectListener() {
                            @Override
                            public void OnFenleiSelected(int curId, String curTitle) {
                                if (curId != -1 && curTitle != null) {
                                    mFenleiId = curId;
                                    txt_fenlei.setText(curTitle);
                                }
                                viewCondition.setVisibility(View.GONE);
                            }
                        }, new OnCancelListener() {
                            @Override
                            public void onCancel() {
                                viewCondition.setVisibility(View.GONE);
                            }
                        });
                        viewConditionBody.addView(mFenleiGridView);
                    }
                    mFenleiGridView.setData(AppConfig.getInstance().serveFenleiList, mFenleiId);
                    mFenleiGridView.setVisibility(View.VISIBLE);
                    viewCondition.setVisibility(View.VISIBLE);
                    viewConditionBody.setVisibility(View.VISIBLE);
                    CommonUtils.animationShowFromRight(viewConditionBody);
                    break;
                case R.id.btn_back:
                    onBackPressed();
                    break;
                case R.id.btn_publish:
                    String serviceId = "";
                    String name = edit_name.getText().toString();
                    String fenleiId = String.valueOf(mFenleiId);
                    String comment = edit_comment.getText().toString();
                    String weburl = edit_net.getText().toString();
                    String addr = edit_address.getText().toString();
                    String isShow = btn_first_show.isChecked() ? "1" : "0";
                    String contactName = edit_person_name.getText().toString();
                    String contactMobile = edit_phone_number.getText().toString();
                    String contactWeixin = edit_weixin.getText().toString();

                    String errMsg = "";
                    if (name.equals(""))
                        errMsg = "项目名称不能为空";
                    else if (logoUrl.equals(""))
                        errMsg = "应该上传1张照片";
                    else if (mFenleiId < 1)
                        errMsg = "项目分类不能为空";
                    else if (comment.equals(""))
                        errMsg = "项目介绍不能为空";
                    else if (weburl.equals(""))
                        errMsg = "项目网址不能为空";
                    else if (addr.equals(""))
                        errMsg = "详细地址不能为空";
                    else if (contactName.equals(""))
                        errMsg = "联系人姓名不能为空";
                    else if (contactMobile.equals(""))
                        errMsg = "联系人手机号不能为空";
                    else if (contactWeixin.equals(""))
                        errMsg = "联系人微信号不能为空";

                    if (!errMsg.equals("")) {
                        Toast.makeText(MakeServeActivity.this, errMsg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    new AddServeAsync().execute(serviceId, name, fenleiId, addr, comment, weburl, isShow, contactName, contactMobile, contactWeixin);
                    break;
            }
        }
    };

    private void doTakeCameraAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(ChengxinApplication.getTempFilePath(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_SERVE_IMAGE + PICK_FROM_CAMERA+Constants.FILE_EXTENTION_AUTH_IMAGE)));
        file.deleteOnExit();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, Constants.PICK_FROM_CAMERA);
    }

    private void doTakeGallaryAction() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constants.PICK_FROM_GALLARY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == Constants.PICK_FROM_GALLARY) {
            Bitmap photo = null;
            photo = data.getParcelableExtra("data");

            if(photo != null) {
                try {
                    FileOutputStream fos = new FileOutputStream(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_SERVE_IMAGE + requestCode+Constants.FILE_EXTENTION_AUTH_IMAGE));
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    img_logo.setImageBitmap(photo);
                    logoUrl = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_SERVE_IMAGE + requestCode+Constants.FILE_EXTENTION_AUTH_IMAGE);

                    fos.flush();
                    fos.close();
                } catch(Exception e) {
                    Log.e(TAG, "" + requestCode + " : " + e.toString());
                }
            } else {
                Log.i(TAG, "Bitmap is null");
            }
        } else if (requestCode == Constants.PICK_FROM_CAMERA) {
            if (data != null) {
                Bitmap photo = null;
                photo = data.getParcelableExtra("data");
                if(photo != null) {
                    try {
                        FileOutputStream fos = new FileOutputStream(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_SERVE_IMAGE + requestCode+Constants.FILE_EXTENTION_AUTH_IMAGE));
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        img_logo.setImageBitmap(photo);
                        logoUrl = ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_SERVE_IMAGE + requestCode+Constants.FILE_EXTENTION_AUTH_IMAGE);

                        fos.flush();
                        fos.close();
                    } catch(Exception e) {
                        Log.e(TAG, "" + requestCode + " : " + e.toString());
                    }
                } else {
                    Log.i(TAG, "Bitmap is null");
                }
            }

            Uri imageCaptureUri = Uri.fromFile(new File(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_SERVE_IMAGE + requestCode+Constants.FILE_EXTENTION_AUTH_IMAGE)));
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(imageCaptureUri, "image/*");
            intent.putExtra("scale", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, Constants.PICK_FROM_GALLARY);
        }
    }

    class AddServeAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getApplicationContext());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncAddServe(strs[0], strs[1], strs[2], strs[3], strs[4], strs[5], strs[6], strs[7], strs[8], strs[9], logoUrl);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    Toast.makeText(MakeServeActivity.this, "成功发布服务", Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(MakeServeActivity.this, MyWriteActivity.class);
                    intent.putExtra("fragmentIndex", 2);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Toast.makeText(MakeServeActivity.this, result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MakeServeActivity.this, getString(R.string.err_server), Toast.LENGTH_LONG).show();
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
