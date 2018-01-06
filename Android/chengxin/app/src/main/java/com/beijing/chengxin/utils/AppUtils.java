package com.beijing.chengxin.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Sunxp on 2017/10/24.
 * 用于App相关的工具类，如重启应用，获取应用信息等
 */

public class AppUtils {
    //检测应用是否存在
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void openQQChat(Context context, String qqNum) {
        if (AppUtils.checkApkExist(context, "com.tencent.mobileqq")) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1")));
        } else {
            Toast.makeText(context, "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
        }
    }

    public static void openCall(Activity aty, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));//立即拨打电话
        //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));//打开拨号盘

        if (ActivityCompat.checkSelfPermission(aty, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(aty, new String[]{Manifest.permission.CALL_PHONE}, 0x2000);
            return;
        }
        aty.startActivity(intent);
    }
}
