package com.beijing.chengxin.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.beijing.chengxin.ChengxinApplication;
import com.beijing.chengxin.network.model.CityModel;
import com.beijing.chengxin.network.model.FenleiListModel;
import com.beijing.chengxin.network.model.FenleiModel;
import com.beijing.chengxin.network.model.HotModel;
import com.beijing.chengxin.network.model.ItemModel;
import com.beijing.chengxin.network.model.ProvinceModel;
import com.beijing.chengxin.network.model.ServeModel;
import com.beijing.chengxin.network.model.XyleixingModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppConfig {
    private final String CONFIG_FILE_NAME = "info_config";

    private static AppConfig instance;

    /** the preferences. */
    private SharedPreferences mPreference;
    private SharedPreferences.Editor mEditor;

    /** index string */
    public static final String TITLE_SORT_HOME = "sort_home";

    public static final String TITLE_FOLLOW_PERSON = "follow_person";
    public static final String TITLE_FOLLOW_ENTERPRISE = "follow_enterprise";
    public static final String TITLE_FOLLOW_MYHOME_PERSON = "follow_myhome_person";
    public static final String TITLE_FOLLOW_MYHOME_ENTERPRISE = "follow_myhome_enterprise";
    public static final String TITLE_FOLLOW_FRIEND_1_PERSON = "follow_friend_1_person";
    public static final String TITLE_FOLLOW_FRIEND_1_ENTERPRISE = "follow_friend_1_enterprise";
    public static final String TITLE_FOLLOW_FRIEND_2_PERSON = "follow_friend_2_person";
    public static final String TITLE_FOLLOW_FRIEND_2_ENTERPRISE = "follow_friend_2_enterprise";
    public static final String TITLE_FOLLOW_FRIEND_3_PERSON = "follow_friend_3_person";
    public static final String TITLE_FOLLOW_FRIEND_3_ENTERPRISE = "follow_friend_3_enterprise";

    /** index int */
    // home sort index int
    public static final int VAL_SORT_HOME_INTEREST = 0;
    public static final int VAL_SORT_HOME_TRUST = 1;
    public static final int VAL_SORT_HOME_LAST = 2;

    /* search condition relation */
    public List<ProvinceModel> cityList;
    public List<XyleixingModel> xyleixingList;
    public List<XyleixingModel> pleixingList;
    public List<FenleiModel> itemFenleiList;
    public List<FenleiModel> serveFenleiList;

    /* for item and service detail */
    public ItemModel currentItem;
    public ServeModel currentServe;
    public HotModel currentHot;

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig(ChengxinApplication.getContext());
        }
        return instance;
    }

    public AppConfig(Context context) {
        this.mPreference = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        this.mEditor = this.mPreference.edit();

        cityList = new ArrayList<ProvinceModel>();
        xyleixingList = new ArrayList<XyleixingModel>();
        pleixingList = new ArrayList<XyleixingModel>();
        itemFenleiList = new ArrayList<FenleiModel>();
        serveFenleiList = new ArrayList<FenleiModel>();
    }

    public void clear()  {
        mEditor = mPreference.edit();
        mEditor.clear();
        mEditor.commit();
    }

    public void setIntValue(String key, int val) {
        mEditor = mPreference.edit();
        mEditor.putInt(key, val);
        mEditor.commit();
    }

    public int getIntValue(String key, int defaultVal) {
        return mPreference.getInt(key, defaultVal);
    }

    public void setIntArray(String key, List<Integer> array) {
        mEditor = mPreference.edit();
        String str = "";
        for (int i = 0; i < array.size(); i++) {
            if (!str.equals(""))
                str += ",";
            str += String.valueOf(array.get(i));
        }
        mEditor.putString(key, str);
        mEditor.commit();
    }

    public List<Integer> getIntArray(String key) {
        String str = mPreference.getString(key, "");
        String[] strArr = str.split(",");
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i< strArr.length; i++) {
            if (!strArr[i].equals(""))
                list.add(Integer.valueOf(strArr[i]));
        }

        return list;
    }

    public void setStringValue(String key, String val) {
        mEditor = mPreference.edit();
        mEditor.putString(key, val);
        mEditor.commit();
    }

    public String getStringValue(String key, String defaultVal) {
        return mPreference.getString(key, defaultVal);
    }

    public void setStringSetValue(String key, Set<String> val) {
        mEditor = mPreference.edit();
        mEditor.putStringSet(key, val);
        mEditor.commit();
    }

    public HashSet<String> getStringSetValue(String key, Set<String> defaultVal) {
        return (HashSet<String>) mPreference.getStringSet(key, defaultVal);
    }

}
