package com.beijing.chengxin.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.beijing.chengxin.config.Constants;
import com.beijing.chengxin.network.model.CityModel;
import com.beijing.chengxin.network.model.ComedityModel;
import com.beijing.chengxin.network.model.ItemModel;
import com.beijing.chengxin.network.model.ServeModel;
import com.beijing.chengxin.network.model.UserModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
                        name1 = (item1.getAkind() == Constants.ACCOUNT_TYPE_PERSON) ? item1.getRealname() : item1.getEnterName();
                    }

                    if (name2 == null || name2.length() == 0) {
                        name2 = (item2.getAkind() == Constants.ACCOUNT_TYPE_ENTERPRISE) ? item2.getRealname() : item2.getEnterName();
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

}
