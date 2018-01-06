package com.beijing.chengxin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.beijing.chengxin.ui.widget.GridView;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.ResUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class MakeEvaluationActivity extends ParentFragmentActivity {
    public final String TAG = MakeEvaluationActivity.class.getName();

    private  static int MAX_LETTER_COUNT = 100;
    private  static int MAX_PICTURE_COUNT = 5;

    private  ToggleButton btnPerson , btnEnterprise, btnTypeFront , btnTypeBack , btnModeDetail , btnModeQuick;
    private EditText editReason, editContent;
    private  TextView txtReason , txtContent, txtName;
    private LinearLayout layoutReason, layoutEvalAccountType;

    private GridView gridView;
    private GridViewAdapter mAdapter;
    private List<Bitmap> imageList;
    private List<String> imagePathList;
    int photoIndex;

    int userId;
    String userName;
    String userCode;

    SyncInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_evalution);

        // set title
        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.make_eval));
        findViewById(R.id.btn_back).setOnClickListener(mClickListener);
        findViewById(R.id.btn_publish).setOnClickListener(mClickListener);

        btnPerson = (ToggleButton)findViewById(R.id.btn_person);
        btnEnterprise = (ToggleButton)findViewById(R.id.btn_enterprise);

        btnTypeFront = (ToggleButton)findViewById(R.id.btn_type_front);
        btnTypeBack = (ToggleButton)findViewById(R.id.btn_type_back);
        btnModeDetail = (ToggleButton)findViewById(R.id.btn_mode_detail);
        btnModeQuick = (ToggleButton)findViewById(R.id.btn_mode_quick);

        btnPerson.setOnClickListener(mClickListener);
        btnEnterprise.setOnClickListener(mClickListener);
        btnTypeFront.setOnClickListener(mClickListener);
        btnTypeBack.setOnClickListener(mClickListener);
        btnModeDetail.setOnClickListener(mClickListener);
        btnModeQuick.setOnClickListener(mClickListener);

        txtReason= (TextView)findViewById(R.id.txt_eval_reason_cnt);
        txtContent= (TextView)findViewById(R.id.txt_eval_content_cnt);
        txtName = (TextView)findViewById(R.id.txt_name);

        txtReason.setText(String.format("0/%d", MAX_LETTER_COUNT));
        txtContent.setText(String.format("0/%d", MAX_LETTER_COUNT));

        editContent = (EditText)findViewById(R.id.edit_eval_content);
        editReason= (EditText)findViewById(R.id.edit_eval_reason);

        editReason.setOnKeyListener(keyListener);
        editContent.setOnKeyListener(keyListener);

        layoutReason = (LinearLayout)findViewById(R.id.layout_eval_reason);
        layoutEvalAccountType = (LinearLayout)findViewById(R.id.layout_eval_account_type);

        imageList = new ArrayList<Bitmap>();
        imagePathList= new ArrayList<String >();
        photoIndex = 0;
        gridView = (GridView)findViewById(R.id.grid_view);
        mAdapter = new GridViewAdapter(this);
        gridView.setAdapter(mAdapter);

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            layoutEvalAccountType.setVisibility(View.VISIBLE);
            txtName.setOnClickListener(mClickListener);
        } else {
            layoutEvalAccountType.setVisibility(View.GONE);
            userName = getIntent().getStringExtra("userName");
            userCode = getIntent().getStringExtra("userCode");
            txtName.setText(String.format("%s(%s)", userName, userCode));
        }

        info = new SyncInfo(this);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    onBackPressed();
                    break;
                case R.id.txt_name:
                    Intent intent = new Intent(MakeEvaluationActivity.this, DetailActivity.class);
                    intent.putExtra("type", Constants.INDEX_SELECT_ACCOUNT);
                    intent.putExtra("akind", (btnPerson.isChecked() ? Constants.ACCOUNT_TYPE_PERSON : Constants.ACCOUNT_TYPE_ENTERPRISE));
                    intent.putExtra("userid", userId);
                    startActivityForResult(intent, Constants.ACTIVITY_ACCOUNT_SELECT);
                    break;
                case R.id.btn_person:
                    btnEnterprise.setChecked(false);
                    btnPerson.setChecked(true);
                    break;
                case R.id.btn_enterprise:
                    btnEnterprise.setChecked(true);
                    btnPerson.setChecked(false);
                    break;
                case R.id.btn_type_front:
                    btnTypeFront.setChecked(true);
                    btnTypeBack.setChecked(false);
                    break;
                case R.id.btn_type_back:
                    btnTypeFront.setChecked(false);
                    btnTypeBack.setChecked(true);
                    break;
                case R.id.btn_mode_detail:
                    btnModeDetail.setChecked(true);
                    btnModeQuick.setChecked(false);
                    layoutReason.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn_mode_quick:
                    btnModeDetail.setChecked(false);
                    btnModeQuick.setChecked(true);
                    layoutReason.setVisibility(View.GONE);
                    break;
                case R.id.btn_publish:
                    if (!ChengxinApplication.instance.mIsVisibleFlag)
                        return;
                    String errMsg = "";
                    String reason = editReason.getText().toString().trim();
                    String content = editContent.getText().toString().trim();
                    if (userId < 0)
                        errMsg = "平台个人不能为空";
                    else if (btnModeDetail.isChecked() && reason.equals(""))
                        errMsg = "评价原因不能为空";
                    else if (content.equals(""))
                        errMsg = "评价内容不能为空";
                    else if (imageList.size() == 0)
                        errMsg = "应该上传1张照片";

                    if (!errMsg.equals("")) {
                        Toast.makeText(getBaseContext(), errMsg, Toast.LENGTH_SHORT).show();
                    } else {
                        int kind = (btnTypeFront.isChecked() ? Constants.ESTIMATE_KIND_FORWORD : Constants.ESTIMATE_KIND_BACKWORD);
                        int method = (btnModeDetail.isChecked() ? Constants.ESTIMATE_METHOD_DETAIl : Constants.ESTIMATE_METHOD_QUICK);
                        new AddProductAsync().execute(String.valueOf(userId), "", String.valueOf(kind), String.valueOf(method), reason, content);
                    }
                    break;
            }
        }
    };

    View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            String content;
            int length;
            switch (v.getId()) {
                case R.id.edit_eval_content:
                    content = editContent.getText().toString();
                    length = content.length();
                    txtContent.setText(String.format("%d/%d", length, MAX_LETTER_COUNT));

                    if (length >= MAX_LETTER_COUNT) {
                        editContent.setText(content.substring(0, MAX_LETTER_COUNT));
                        return true;
                    }
                    return false;
                case R.id.edit_eval_reason:
                default:
                    content = editReason.getText().toString();
                    length = content.length();
                    txtReason.setText(String.format("%d/%d", length, MAX_LETTER_COUNT));

                    if (length >= MAX_LETTER_COUNT) {
                        editReason.setText(content.substring(0, MAX_LETTER_COUNT));
                        return true;
                    }
                    return false;
            }
        }
    };

    public class GridViewAdapter extends BaseAdapter {
        private Context mContext;
        private View mRootView;

        public GridViewAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            int count = imageList.size();
            if (count < MAX_PICTURE_COUNT)
                count ++;
            count = Math.min(count, MAX_PICTURE_COUNT);
            return count;
        }

        @Override
        public Object getItem(int position) {
            if (position < imageList.size())
                return imageList.get(position);
            else return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            Bitmap item = (Bitmap)getItem(position);
            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_take_picture, parent, false);

            Button btnTake = (Button)convertView.findViewById(R.id.txt_take_photo);
            ImageButton btnDelete = (ImageButton)convertView.findViewById(R.id.btn_delete);
            ImageView imgPhoto = (ImageView)convertView.findViewById(R.id.imgPhoto);

            if (item == null) {
                btnTake.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.GONE);
            }
            else {
                btnTake.setVisibility(View.GONE);
                btnDelete.setVisibility(View.VISIBLE);
                imgPhoto.setImageBitmap(item);
            }
            btnTake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // add dd -- 2017.12.14
                    CropImage.startPickImageActivity(MakeEvaluationActivity.this);

//                    SelectGalleryDialog dlg = new SelectGalleryDialog(MakeEvaluationActivity.this, new SelectGalleryDialog.OnSelectListener() {
//                        @Override
//                        public void onSelectCamera() {
//                            doTakeCameraAction();
//                        }
//                        @Override
//                        public void onSelectGallery() {
//                            doTakeGallaryAction();
//                        }
//                    });
//                    dlg.setCancelable(true);
//                    dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    dlg.show();
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageList.remove(position);
                    imagePathList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    private void doTakeCameraAction() {
        photoIndex ++;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file =  new File(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_EVAL_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE));
        file.deleteOnExit();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, Constants.PICK_FROM_CAMERA);
    }

    private void doTakeGallaryAction() {
        photoIndex ++;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX",  (int) getResources().getDimension(R.dimen.comedity_image_height));
        intent.putExtra("outputY",  (int) getResources().getDimension(R.dimen.comedity_image_height));
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, Constants.PICK_FROM_GALLARY);
    }

    // add dd 2017.12.14
    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1, 1)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            // no permissions required or already grunted, can start crop image activity
            startCropImageActivity(imageUri);
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                photoIndex ++;

                try {
                    Bitmap photo = ResUtils.decodeUri(this, result.getUri());
                    if(photo != null) {
                        try {
                            FileOutputStream fos = new FileOutputStream(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_EVAL_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE));
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            imageList.add(photo);
                            imagePathList.add(Constants.FILE_PREFIX_EVAL_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE);
                            mAdapter.notifyDataSetChanged();

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
        if (requestCode == Constants.ACTIVITY_ACCOUNT_SELECT && resultCode == RESULT_OK && data != null) {
            userId = data.getIntExtra("userId", -1);
            userCode = data.getStringExtra("code");
            userName = data.getStringExtra("name");
            txtName.setText(String.format("%s(%s)", userName, userCode));
        }

//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK)
//            return;
//        if (requestCode == Constants.PICK_FROM_GALLARY) {
//            Bitmap photo = null;
//            photo = data.getParcelableExtra("data");
//
//            if(photo != null) {
//                try {
//                    FileOutputStream fos = new FileOutputStream(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_EVAL_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE));
//                    photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    imageList.add(photo);
//                    imagePathList.add(Constants.FILE_PREFIX_EVAL_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE);
//                    mAdapter.notifyDataSetChanged();
//
//                    fos.flush();
//                    fos.close();
//                } catch(Exception e) {
//                    Log.e(TAG, "" + requestCode + " : " + e.toString());
//                }
//            } else {
//                Log.i(TAG, "Bitmap is null");
//            }
//        } else if (requestCode == Constants.PICK_FROM_CAMERA) {
//            Uri imageCaptureUri = Uri.fromFile(new File(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_EVAL_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE)));
//
//            Intent intent = new Intent("com.android.camera.action.CROP");
//            intent.setDataAndType(imageCaptureUri, "image/*");
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            intent.putExtra("outputX",  (int) getResources().getDimension(R.dimen.comedity_image_height));
//            intent.putExtra("outputY",  (int) getResources().getDimension(R.dimen.comedity_image_height));
//            intent.putExtra("scale", true);
//            intent.putExtra("return-data", true);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
//
//            startActivityForResult(intent, Constants.PICK_FROM_GALLARY);
//        } else if (requestCode == Constants.ACTIVITY_ACCOUNT_SELECT) {
//            userId = data.getIntExtra("userId", -1);
//            userCode = data.getStringExtra("code");
//            userName = data.getStringExtra("name");
//            txtName.setText(String.format("%s(%s)", userName, userCode));
//        }
    }

    class AddProductAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(MakeEvaluationActivity.this);
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncLeaveEstimate(1, strs[0], strs[1], strs[2], strs[3], strs[4], strs[5], imagePathList);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    Toast.makeText(MakeEvaluationActivity.this, "成功发布评价", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.err_server), Toast.LENGTH_LONG).show();
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
