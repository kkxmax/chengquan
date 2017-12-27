package com.beijing.chengxin.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;

public class ResUtils {
    public static Bitmap decodeUri(Context context, Uri selectedImage) throws FileNotFoundException {
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage));
    }
}
