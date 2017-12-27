package com.beijing.chengxin.ui.widget;

import android.content.Context;
import android.content.SharedPreferences;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.R;

public class Utils {
	
	Context mContext;

	public static final int REQUEST_PHOTO_ALBUM = 1;
	public static final int REQUEST_CROP_IMAGE = 2;

	public static final int REQUEST_PHOTO_ALBUM1 = 3;
	public static final int REQUEST_CROP_IMAGE2 = 4;
	public static final int REQUEST_PHOTO_EDIT = 5;

	public static ProgressHUD mProgressHUD;
	
	public Utils(Context context) {
		this.mContext = context;
	}

	public static void displayProgressDialog(Context context){
		try {
			if (ChengxinApplication.instance.mIsVisibleFlag) {
				mProgressHUD = ProgressHUD.show(context, context.getResources().getString(R.string.loading_text), true, true);
				ChengxinApplication.instance.mIsVisibleFlag = false;
			}
		}catch (Exception e){
			//Toast.makeText(context, "Progress Diaglog Error", Toast.LENGTH_SHORT).show();
		}
    }

    public static void disappearProgressDialog(){
		try {
			mProgressHUD.dismiss();
			ChengxinApplication.instance.mIsVisibleFlag = true;
		}catch (Exception e){
			//Toast.makeText(mProgressHUD.getContext(), "Progress Diaglog Error", Toast.LENGTH_SHORT).show();
		}
    }
	
}
