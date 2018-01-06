package com.beijing.chengxin.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import com.beijing.chengxin.R;
import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.CityModel;
import com.beijing.chengxin.network.model.ComedityModel;
import com.beijing.chengxin.network.model.ItemModel;
import com.beijing.chengxin.network.model.ServeModel;
import com.beijing.chengxin.network.model.UserModel;
import com.beijing.chengxin.ui.activity.MyRealnameCertActivity;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.beijing.chengxin.config.Constants.ACCOUNT_TYPE_PERSON;

public class CommonUtils {

    public static void sortCityByChinese(List<CityModel> cityDatas) {
        if (cityDatas != null) {
            Comparator<CityModel> comparator = new Comparator<CityModel>() {
                @Override
                public int compare(CityModel item1, CityModel item2) {
                    String name1 = item1.getCityAlias();
                    String name2 = item2.getCityAlias();

                    if (name1 == null || name1.length() == 0) {
                        name1 = item1.getName();
                    }

                    if (name2 == null || name2.length() == 0) {
                        name2 = item2.getName();
                    }

                    return name1.compareToIgnoreCase(name2);
                }
            };
            Collections.sort(cityDatas, comparator);
        }
    }

    public static String getUserName(UserModel user) {
        String name = user.getMobile();
        if (user.getAkind() == ACCOUNT_TYPE_PERSON) {
            if (!user.getRealname().equals(""))
                name = user.getRealname();
        } else if (user.getAkind() == Constants.ACCOUNT_TYPE_ENTERPRISE) {
            if (!user.getEnterName().equals(""))
                name = user.getEnterName();
        }
        return name;
    }

    public static String getComedityListName(List<ComedityModel> list) {
        String str = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i ++) {
                ComedityModel item = list.get(i);
                str += item.getName();
                if (i < list.size() - 1)
                    str += ", ";
            }
        }
        return str;
    }

    public static String getItemListName(List<ItemModel> list) {
        String str = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i ++) {
                ItemModel item = list.get(i);
                str += item.getName();
                if (i < list.size() - 1)
                    str += ", ";
            }
        }
        return str;
    }

    public static String getServeListName(List<ServeModel> list) {
        String str = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i ++) {
                ServeModel item = list.get(i);
                str += item.getName();
                if (i < list.size() - 1)
                    str += ", ";
            }
        }
        return str;
    }

    public static void sortAccountByChinese(List<UserModel> datas) {
        if (datas != null) {
            Comparator<UserModel> comparator = new Comparator<UserModel>() {
                @Override
                public int compare(UserModel item1, UserModel item2) {
                    String name1 = item1.alias;
                    String name2 = item2.alias;

                    if (name1 == null || name1.length() == 0) {
                        String tmpName = item1.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? item1.getRealname() : item1.getEnterName();
                        tmpName = tmpName.length() == 0 ? item1.getMobile() : tmpName;
                        name1 = tmpName;
                    }

                    if (name2 == null || name2.length() == 0) {
                        String tmpName = item2.getAkind() == Constants.ACCOUNT_TYPE_PERSON ? item2.getRealname() : item2.getEnterName();
                        tmpName = tmpName.length() == 0 ? item2.getMobile() : tmpName;
                        name2 = tmpName;
                    }

                    char star = '☆';
                    char first = 0, second = 0;
                    if (name1.length() > 0) {
                        first = name1.charAt(0);
                    }
                    if (name2.length() > 0) {
                        second = name2.charAt(0);
                    }
                    if (first == star && second == star) {
                        return name1.compareToIgnoreCase(name2);
                    } else if (first == star && second != star) {
                        return -1;
                    } else if (first != star && second == star) {
                        return 1;
                    }
                    return name1.compareToIgnoreCase(name2);
                }
            };
            Collections.sort(datas, comparator);
        }
    }

    public static String getCurrentDirBySrc(String src) {
        if (src == null)
            return null;
        String currentDirPath = src.substring(0, src.lastIndexOf("/"));
        return currentDirPath;
    }

    public static void animationShowFromLeft(View view) {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(200);
        view.startAnimation(animation);
    }

    public static void animationHideToLeft(View view) {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(200);
        view.startAnimation(animation);
    }

    public static void animationShowFromRight(View view) {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(200);
        view.startAnimation(animation);
    }

    public static void animationHideToRight(View view) {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(200);
        view.startAnimation(animation);
    }

    public static void showRealnameCertAlert(final Activity mActivity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder
                .setMessage(R.string.realname_cert_confirm)
                .setCancelable(false)
                .setPositiveButton(R.string.cert_immediately, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(mActivity, MyRealnameCertActivity.class);
                        mActivity.startActivity(intent);
                        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                })
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        if (view.hasFocus()) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyboardFrom(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(), 0);
        }
    }

    /**
     * convert Form String to String
     *
     * @param dateStr
     * @param timeFormat
     *            : yyyyMMddHHmmss
     * @return
     */
    public static String getDateStrFromStrFormat(String dateStr, String originalFormat, String timeFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(originalFormat, Locale.ENGLISH);
            Date date = format.parse(dateStr);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getCompleteWebUrl(String url) {
        String urlCompletePattern = "^http(s{0,1})://[a-zA-Z0-9_/\\\\-\\\\.]+([:][0-9]+)?[a-zA-Z0-9_/\\\\&\\\\?\\\\=\\\\-\\\\.\\\\~\\\\%]*";
        String urlNonePrePattern = "^[a-zA-Z0-9_/\\\\-\\\\.]+([:][0-9]+)?[a-zA-Z0-9_/\\\\&\\\\?\\\\=\\\\-\\\\.\\\\~\\\\%]*";
        String result = url;
        if (!result.matches(urlCompletePattern)) {
            if (result.matches(urlNonePrePattern))
                result = "http://" + result;
        }

        return result;
    }

    public static SpannableString formatString(Context context, String text, int style) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context, style), 0, text.length(), 0);
        return spannableString;
    }

}
