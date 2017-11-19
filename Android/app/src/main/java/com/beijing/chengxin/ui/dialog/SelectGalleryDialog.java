package com.beijing.chengxin.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.beijing.chengxin.R;

public class SelectGalleryDialog extends Dialog {
    Context mContext;
    OnSelectListener mListener;

    TextView txtCamera, txtGallery;

    public SelectGalleryDialog(Context context) {
        super(context);
        mContext = context;
    }

    public SelectGalleryDialog(Context context, OnSelectListener listener) {
        super(context);
        mContext = context;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inf = LayoutInflater.from(mContext);
        View dlg = inf.inflate(R.layout.dialog_select_gallery, null);
        setContentView(dlg);

        txtCamera = (TextView) findViewById(R.id.txt_camera);
        txtGallery = (TextView) findViewById(R.id.txt_gallery);

        txtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onSelectCamera();
                SelectGalleryDialog.this.cancel();
            }
        });
        txtGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onSelectGallery();
                SelectGalleryDialog.this.cancel();
            }
        });
    }

    public interface OnSelectListener {
        void onSelectCamera();
        void onSelectGallery();
    }

}