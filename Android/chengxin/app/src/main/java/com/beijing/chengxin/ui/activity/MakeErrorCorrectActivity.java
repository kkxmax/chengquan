package com.beijing.chengxin.ui.activity;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.BaseModel;
import com.beijing.chengxin.network.model.EvalModel;
import com.beijing.chengxin.ui.dialog.SelectGalleryDialog;
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

public class MakeErrorCorrectActivity extends ParentFragmentActivity {

    public final String TAG = MakeErrorCorrectActivity.class.getName();
    private int MAX_LETTER_COUNT = 100;
    private int MAX_PICTURE_COUNT = 6;

    ImageButton  btnBack;
    TextView txtPName, txtNName, txtNComment;
    ToggleButton btnKuada, btnXujia;
    EditText edtReason, edtWhyis;
    TextView txtReasonCnt, txtWhyisCnt;
    Button btnPublish;

    private GridView gridView;
    private GridViewAdapter mAdapter;
    private List<Bitmap> imageList;
    private List<String> imagePathList;

    int mEstimateId;
    String mPName, mNName, mNContent;
    int photoIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_error_correct);

        // set title
        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.right_mistake));

        mEstimateId = getIntent().getIntExtra("estimateId", 0);
        mPName = getIntent().getStringExtra("pname");
        mNName = getIntent().getStringExtra("nname");
        mNContent = getIntent().getStringExtra("ncontent");

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnKuada = (ToggleButton)findViewById(R.id.btn_kuada);
        btnXujia = (ToggleButton)findViewById(R.id.btn_xujia);
        btnPublish = (Button)findViewById(R.id.btn_publish);

        btnBack.setOnClickListener(mClickListener);
        btnKuada.setOnClickListener(mClickListener);
        btnXujia.setOnClickListener(mClickListener);
        btnPublish.setOnClickListener(mClickListener);

        txtPName = (TextView) findViewById(R.id.txt_pname);
        txtNName = (TextView) findViewById(R.id.txt_nname);
        txtNComment = (TextView) findViewById(R.id.txt_ncomment);
        edtReason = (EditText) findViewById(R.id.edt_reason);
        edtWhyis = (EditText) findViewById(R.id.edt_whyis);
        txtReasonCnt = (TextView)findViewById(R.id.txt_reason_cnt);
        txtWhyisCnt = (TextView)findViewById(R.id.txt_whyis_cnt);
        txtReasonCnt.setText(String.format("0/%d", MAX_LETTER_COUNT));
        txtWhyisCnt.setText(String.format("0/%d", MAX_LETTER_COUNT));

        edtReason.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String content = edtReason.getText().toString();
                int length = content.length();
                txtReasonCnt.setText(String.format("%d/%d", length, MAX_LETTER_COUNT));

                if (length >= MAX_LETTER_COUNT) {
                    edtReason.setText(content.substring(0, MAX_LETTER_COUNT));
                    return true;
                }
                return false;
            }
        });
        edtWhyis.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String content = edtWhyis.getText().toString();
                int length = content.length();
                txtWhyisCnt.setText(String.format("%d/%d", length, MAX_LETTER_COUNT));

                if (length >= MAX_LETTER_COUNT) {
                    edtWhyis.setText(content.substring(0, MAX_LETTER_COUNT));
                    return true;
                }
                return false;
            }
        });

        imageList = new ArrayList<Bitmap>();
        imagePathList= new ArrayList<String >();
        photoIndex = 0;
        gridView = (GridView)findViewById(R.id.grid_view);
        mAdapter = new GridViewAdapter(this);
        gridView.setAdapter(mAdapter);

        initData();
    }

    private void initData() {
        if (mEstimateId != 0) {
            txtPName.setText(mPName);
            txtNName.setText(mNName);
            txtNComment.setText(mNContent);
        }
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_kuada:
                    btnKuada.setChecked(true);
                    btnXujia.setChecked(false);
                    break;
                case R.id.btn_xujia:
                    btnKuada.setChecked(false);
                    btnXujia.setChecked(true);
                    break;
                case R.id.btn_back:
                    onBackPressed();
                    break;
                case R.id.btn_publish:
                    if (isCheckValidation()) {
                        onSubmit();
                    }
                    break;
            }
        }
    };

    private boolean isCheckValidation() {
        if (edtReason.getText().toString().trim().length() == 0) {
            Toast.makeText(mContext, R.string.pls_insert_error_reason, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtWhyis.getText().toString().trim().length() == 0) {
            Toast.makeText(mContext, R.string.pls_insert_error_whyis, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (imageList == null || imageList.size() == 0) {
            Toast.makeText(mContext, "最少应该上传1张照片", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

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

            btnTake.setText(R.string.make_error_correct_max_image_count);

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
                    CropImage.startPickImageActivity(MakeErrorCorrectActivity.this);

//                    SelectGalleryDialog dlg = new SelectGalleryDialog(MakeErrorCorrectActivity.this, new SelectGalleryDialog.OnSelectListener() {
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
        File file =  new File(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_ERROR_CORRECT_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE));
        file.deleteOnExit();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, Constants.PICK_FROM_CAMERA);
    }

    private void doTakeGallaryAction() {
        photoIndex ++;
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                            FileOutputStream fos = new FileOutputStream(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_ERROR_CORRECT_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE));
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            imageList.add(photo);
                            imagePathList.add(Constants.FILE_PREFIX_ERROR_CORRECT_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE);
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


//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK)
//            return;
//        if (requestCode == Constants.PICK_FROM_GALLARY) {
//            Bitmap photo = null;
//            photo = data.getParcelableExtra("data");
//
//            if(photo != null) {
//                try {
//                    FileOutputStream fos = new FileOutputStream(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_ERROR_CORRECT_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE));
//                    photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    imageList.add(photo);
//                    imagePathList.add(Constants.FILE_PREFIX_ERROR_CORRECT_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE);
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
//            Uri imageCaptureUri = Uri.fromFile(new File(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_ERROR_CORRECT_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE)));
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
//        }
    }

    private void onSubmit() {
        final int kind = btnKuada.isChecked() ? 1 : 2;
        final String reason = edtReason.getText().toString().trim();
        final String whyis = edtWhyis.getText().toString().trim();
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected void onPreExecute() {
                Utils.displayProgressDialog(mContext);
            }
            @Override
            protected Object doInBackground(Object... params) {
                ArrayList<Constants.UploadFileModel> fileModelList = new ArrayList<Constants.UploadFileModel>();
                for (int i = 0; i < imagePathList.size(); i++) {
                    Constants.UploadFileModel fileModel = new Constants.UploadFileModel();
                    fileModel.fileTitle = "logo" + i;
                    fileModel.fileName = imagePathList.get(i);
                    fileModel.filePath = ChengxinApplication.getTempFilePath(imagePathList.get(i));
                    fileModelList.add(fileModel);
                }
                return new SyncInfo(mContext).syncMakeCorrect(mEstimateId, kind, reason, whyis, fileModelList);
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                BaseModel result = (BaseModel) o;
                if (result.isValid()) {
                    if(result.getRetCode() == ERROR_OK) {
                        Toast.makeText(mContext, R.string.msg_success_submit, Toast.LENGTH_SHORT).show();
                        onBackPressed();
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
        }.execute();
    }

}
