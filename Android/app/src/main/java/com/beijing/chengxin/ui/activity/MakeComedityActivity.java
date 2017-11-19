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
import com.beijing.chengxin.ui.dialog.SelectGalleryDialog;
import com.beijing.chengxin.ui.listener.OnCancelListener;
import com.beijing.chengxin.ui.view.MeHangyeListView;
import com.beijing.chengxin.ui.widget.GridView;
import com.beijing.chengxin.ui.widget.Utils;
import com.beijing.chengxin.utils.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;
import static com.beijing.chengxin.config.Constants.PICK_FROM_CAMERA;

public class MakeComedityActivity extends ParentFragmentActivity {

    public final String TAG = MakeComedityActivity.class.getName();
    private  static int MAX_LETTER_COUNT = 300;
    private  static int MAX_PICTURE_COUNT = 5;


    private ImageButton  btnBack;
    private EditText editTitle, editPrice, editComment, editNet, editPlace;
    private TextView txtLetterCount, txtFenlei;
    private ToggleButton btnYes, btnNo;
    private Button btnPublish;

    private GridView gridView;
    private GridViewAdapter mAdapter;
    private List<Bitmap> imageList;
    private List<String> imagePathList;

    LinearLayout viewCondition;
    FrameLayout viewConditionBody;
    private MeHangyeListView mHangyeListView;
    int mHangyeId;

    int productId;

    SyncInfo info;
    int photoIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_comedity);

        // set title
        ((TextView)findViewById(R.id.txt_nav_title)).setText(getString(R.string.make_comedity));

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnYes = (ToggleButton)findViewById(R.id.btn_yes);
        btnNo = (ToggleButton)findViewById(R.id.btn_no);
        btnPublish = (Button)findViewById(R.id.btn_publish);

        btnBack.setOnClickListener(mClickListener);
        btnYes.setOnClickListener(mClickListener);
        btnNo.setOnClickListener(mClickListener);
        btnPublish.setOnClickListener(mClickListener);

        txtLetterCount = (TextView)findViewById(R.id.txt_letter_count);
        txtFenlei = (TextView)findViewById(R.id.txt_fenlei);
        txtLetterCount.setText(String.format("0/%d", MAX_LETTER_COUNT));
        txtFenlei.setOnClickListener(mClickListener);

        editTitle = (EditText)findViewById(R.id.edit_title);
        editPrice = (EditText)findViewById(R.id.edit_price);
        editComment = (EditText)findViewById(R.id.edit_comment);
        editNet = (EditText)findViewById(R.id.edit_net);
        editPlace = (EditText)findViewById(R.id.edit_place);

        editComment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String content = editComment.getText().toString();
                int length = content.length();
                txtLetterCount.setText(String.format("%d/%d", length, MAX_LETTER_COUNT));

                if (length >= MAX_LETTER_COUNT) {
                    editComment.setText(content.substring(0, MAX_LETTER_COUNT));
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

        viewCondition = (LinearLayout) findViewById(R.id.view_condition);
        viewConditionBody = (FrameLayout) findViewById(R.id.view_condition_body);

        info = new SyncInfo(this);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txt_fenlei:
                    if (mHangyeListView == null) {
                        mHangyeListView = new MeHangyeListView(MakeComedityActivity.this, new MeHangyeListView.OnHangyeSelectListener() {
                            @Override
                            public void OnHangyeSelected(int curId, String curTitle) {
                                if (curId != -1 && curTitle != null) {
                                    mHangyeId = curId;
                                    txtFenlei.setText(curTitle);
                                }
                                viewCondition.setVisibility(View.GONE);
                            }
                        }, new OnCancelListener() {
                            @Override
                            public void onCancel() {
                                viewCondition.setVisibility(View.GONE);
                            }
                        });
                        viewConditionBody.addView(mHangyeListView);
                    }
                    mHangyeListView.setData(AppConfig.getInstance().pleixingList, mHangyeId);
                    mHangyeListView.setVisibility(View.VISIBLE);
                    viewCondition.setVisibility(View.VISIBLE);
                    viewConditionBody.setVisibility(View.VISIBLE);
                    CommonUtils.animationShowFromRight(viewConditionBody);
                    break;
                case R.id.btn_yes:
                    btnYes.setChecked(true);
                    btnNo.setChecked(false);
                    break;
                case R.id.btn_no:
                    btnYes.setChecked(false);
                    btnNo.setChecked(true);
                    break;
                case R.id.btn_back:
                    onBackPressed();
                    break;
                case R.id.btn_publish:
                    String productIdStr =String.valueOf(productId);
                    String name = editTitle.getText().toString().trim();
                    String priceStr = editPrice.getText().toString().trim();
                    priceStr = priceStr.equals("") ? "0" : priceStr;
                    float price = Float.valueOf(priceStr);
                    priceStr = String.format("%.02f", price);
                    String comment = editComment.getText().toString().trim();
                    String net = editNet.getText().toString().trim();
                    String place = editPlace.getText().toString().trim();
                    String isMain = btnYes.isChecked() ? "1" : "0";
                    String errMsg = "";
                    if (name.equals(""))
                        errMsg = "产品名称不能为空";
                    else if (price < 0.01f)
                        errMsg = "产品价格不能为空";
                    else if (mHangyeId == 0)
                        errMsg = "产品分类不能为空";
                    else if (comment.equals(""))
                        errMsg = "产品介绍不能为空";
                    else if (net.equals(""))
                        errMsg = "产品网址不能为空";
                    else if (place.equals(""))
                        errMsg = "实体店地址不能为空";
                    else if (imageList == null || imageList.size() == 0)
                        errMsg = "最少应该上传1张照片";

                    if (!errMsg.equals(""))
                        Toast.makeText(MakeComedityActivity.this, errMsg, Toast.LENGTH_LONG).show();
                    else
                        new AddProductAsync().execute(productIdStr, name, isMain, priceStr, String.valueOf(mHangyeId), comment, net, place);
                    break;
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
                    SelectGalleryDialog dlg = new SelectGalleryDialog(MakeComedityActivity.this, new SelectGalleryDialog.OnSelectListener() {
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
        File file =  new File(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_COMEDITY_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE));
        file.deleteOnExit();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, Constants.PICK_FROM_CAMERA);
    }

    private void doTakeGallaryAction() {
        photoIndex ++;
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.putExtra("crop", "true");
        intent.putExtra("return-data", true);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                    FileOutputStream fos = new FileOutputStream(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_COMEDITY_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE));
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    imageList.add(photo);
                    imagePathList.add(Constants.FILE_PREFIX_COMEDITY_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE);
                    mAdapter.notifyDataSetChanged();

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
                //Bundle extras = data.getExtras();
                photo = data.getParcelableExtra("data");
                //photo = extras.getParcelable("data");
                if(photo != null) {
                    try {
                        FileOutputStream fos = new FileOutputStream(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_COMEDITY_IMAGE + requestCode + Constants.FILE_EXTENTION_AUTH_IMAGE));
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        imageList.add(photo);
                        imagePathList.add(Constants.FILE_PREFIX_COMEDITY_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE);

                        fos.flush();
                        fos.close();
                    } catch(Exception e) {
                        Log.e(TAG, "" + requestCode + " : " + e.toString());
                    }
                } else {
                    Log.i(TAG, "Bitmap is null");
                }
            }

            Uri imageCaptureUri = Uri.fromFile(new File(ChengxinApplication.getTempFilePath(Constants.FILE_PREFIX_COMEDITY_IMAGE + photoIndex + Constants.FILE_EXTENTION_AUTH_IMAGE)));
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(imageCaptureUri, "image/*");
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, Constants.PICK_FROM_GALLARY);
        }
    }

    class AddProductAsync extends AsyncTask<String, String, BaseModel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.displayProgressDialog(getBaseContext());
        }
        @Override
        protected BaseModel doInBackground(String... strs) {
            return info.syncAddProduct(Integer.valueOf(strs[0]), strs[1], strs[2], strs[3], strs[4], strs[5], strs[6], strs[7], imagePathList);
        }
        @Override
        protected void onPostExecute(BaseModel result) {
            super.onPostExecute(result);
            if (result .isValid()) {
                if(result.getRetCode() == ERROR_OK) {
                    Toast.makeText(MakeComedityActivity.this, "成功发布产品", Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(MakeComedityActivity.this, MyWriteActivity.class);
                    intent.putExtra("fragmentIndex", 0);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Toast.makeText(MakeComedityActivity.this, result.getMsg(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MakeComedityActivity.this, getString(R.string.err_server), Toast.LENGTH_LONG).show();
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
