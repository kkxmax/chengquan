package com.beijing.chengxin.ui.activity;

import android.os.Bundle;

import com.beijing.chengxin.R;
import com.beijing.chengxin.ui.fragment.BaseFragmentActivity;
import com.squareup.picasso.Picasso;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

/**
 * Created by start on 2017.12.11.
 */

public class ImageViewActivity extends BaseFragmentActivity {
   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_imageview);

        ImageViewTouch img_content = (ImageViewTouch) findViewById(R.id.img_content);
        img_content.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        String imgPath = getIntent().getStringExtra("path");
       Picasso.with(this).load(imgPath).placeholder(R.drawable.no_image).into(img_content);
    }
}
