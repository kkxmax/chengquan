package com.beijing.chengxin.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.SyncInfo;
import com.beijing.chengxin.network.model.ErrorListModel;
import com.beijing.chengxin.network.model.ErrorModel;
import com.beijing.chengxin.ui.widget.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.beijing.chengxin.config.Constants.ERROR_OK;

public class MyErrorCorrectDetailActivity extends ParentFragmentActivity {

    private ErrorModel mData;

    // 纠错 UI Controller
    ToggleButton btnOver, btnLie;
    TextView txtReason, txtWhyis;
    HorizontalScrollView hsViewError;
    LinearLayout layoutImagesError;
    // 他方 UI Controller
    ImageView imgLogo;
    TextView txtName, txtContent, txtTime;
    HorizontalScrollView hsView;
    LinearLayout layoutImages;
    ImageView imgStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_error_detail);

        mData = getIntent().getParcelableExtra("data");

        ((TextView) findViewById(R.id.txt_nav_title)).setText(getString(R.string.error_detail));
        findViewById(R.id.btn_back).setOnClickListener(mButtonClickListener);

        // 纠错 UI Controller
        btnOver = (ToggleButton) findViewById(R.id.btn_over);
        btnLie = (ToggleButton) findViewById(R.id.btn_lie);
        txtReason = (TextView) findViewById(R.id.txt_reason);
        txtWhyis = (TextView) findViewById(R.id.txt_whyis);
        hsViewError = (HorizontalScrollView) findViewById(R.id.hs_view_error);
        layoutImagesError = (LinearLayout) findViewById(R.id.layout_images_error);

        // 他方 UI Controller
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtContent = (TextView) findViewById(R.id.txt_content);
        txtTime = (TextView) findViewById(R.id.txt_time);
        hsView = (HorizontalScrollView) findViewById(R.id.hs_view);
        layoutImages = (LinearLayout) findViewById(R.id.layout_images);
        imgStatus = (ImageView) findViewById(R.id.img_status);

        initData();
    }

    private void initData() {
        if (mData != null) {
            if (mData.getKind() == Constants.ERROR_KIND_KUADA) {
                btnOver.setChecked(true);
                btnLie.setChecked(false);
            } else {
                btnOver.setChecked(false);
                btnLie.setChecked(true);
            }

            txtReason.setText(mData.getReason());
            txtWhyis.setText(mData.getWhyis());
            txtTime.setText(mData.getWriteTimeString());

            List<String> imgList = mData.getImgPaths();
            if (imgList != null && imgList.size() > 0) {
                layoutImagesError.removeAllViews();
                for (int i = 0; i < imgList.size(); i++) {
                    ImageView imgView = new ImageView(this);
                    imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    int height = (int) (getResources().getDimension(R.dimen.image_hot_item_width));
                    int width = height * 3 / 2;
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(width, height);
                    lparams.setMargins(0, 0, (int) (getResources().getDimension(R.dimen.margin_small)), 0);
                    imgView.setLayoutParams(lparams);

                    Picasso.with(this).load(Constants.FILE_ADDR + imgList.get(i)).placeholder(R.drawable.no_image).into(imgView);
                    layoutImagesError.addView(imgView);
                }
                hsViewError.setVisibility(View.VISIBLE);
            } else {
                hsViewError.setVisibility(View.GONE);
            }

            int imgStatusResId = R.drawable.label_status_ready;
            switch (mData.getStatus()) {
                case Constants.TEST_STATUS_READY:
                    imgStatusResId = R.drawable.label_status_ready;
                    break;
                case Constants.TEST_STATUS_PASSED:
                    imgStatusResId = R.drawable.label_status_passed;
                    break;
                case Constants.TEST_STATUS_REJECT:
                    imgStatusResId = R.drawable.label_status_reject;
                    break;
            }
            imgStatus.setImageResource(imgStatusResId);

            Picasso.with(this).load(Constants.FILE_ADDR + mData.getEstimaterLogo()).placeholder(R.drawable.no_image).into(imgLogo);
            txtName.setText(mData.getEstimaterAkind() == Constants.ACCOUNT_TYPE_PERSON ? mData.getEstimaterRealname() : mData.getEstimaterEnterName());
            txtContent.setText(mData.getEstimateContent());

            List<String> imgEstimateList = mData.getEstimateImgPaths();
            if (imgEstimateList != null && imgEstimateList.size() > 0) {
                layoutImages.removeAllViews();
                for (int i = 0; i < imgEstimateList.size(); i++) {
                    ImageView imgView = new ImageView(this);
                    imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    int height = (int) (getResources().getDimension(R.dimen.image_hot_item_width));
                    int width = height * 3 / 2;
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(width, height);
                    lparams.setMargins(0, 0, (int) (getResources().getDimension(R.dimen.margin_small)), 0);
                    imgView.setLayoutParams(lparams);

                    Picasso.with(this).load(Constants.FILE_ADDR + imgEstimateList.get(i)).placeholder(R.drawable.no_image).into(imgView);
                    layoutImages.addView(imgView);
                }
                hsView.setVisibility(View.VISIBLE);
            } else {
                hsView.setVisibility(View.GONE);
            }
        }
    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    onBackPressed();
                    break;
            }
        }
    };

}
