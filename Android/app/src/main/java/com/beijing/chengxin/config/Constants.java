package com.beijing.chengxin.config;

import java.security.PublicKey;

public class Constants {

    public static final boolean DEBUG_MODE = true;
    public static final String SERVER_ADDR = "192.168.12.222";
    public static final String SERVER_PORT = "8080";

    public static final String API_ADDR = "http://" + SERVER_ADDR + ":" + SERVER_PORT + "/BFIP/api.html";
    public static final String FILE_ADDR = "http://" + SERVER_ADDR + ":" + SERVER_PORT + "/BFIP";
    public static final String P_ACTION = "pAct";
    public static final String P_TOKEN = "token";

    public static final float LEVEL_ZERO = 0.01f;

    public static final String SEARCH_ORDER = "search_order";
    public static final String SEARCH_CITYNAME= "search_cityname";
    public static final String SEARCH_AKIND = "search_akind";
    public static final String SEARCH_XYLEIXINGID = "search_xyleixingid";
    public static final String SEARCH_KEYWORD = "search_keyword";
    public static final String SEARCH_HOME_KEYWORD_VALUE = "search_home_keyword_value";

    public final static int SEARCH_IN_HOME = 0;
    public final static int SEARCH_IN_FOLLOW = 1;

    public final static int SEARCH_HOME_FAMILIAR = 0;
    public final static int SEARCH_HOME_ENTERPRISE= 1;
    public final static int SEARCH_HOME_COMEDITY = 2;
    public final static int SEARCH_HOME_ITEM = 3;
    public final static int SEARCH_HOME_SERVE = 4;
    public final static int SEARCH_HOME_CODE = 5;

    public static final String NOTIFY_SEARCH_COND_CHANGED = "search_condition_changed";
    public static final String NOTIFY_USERMODEL_CHANGED = "usermodel_changed";

    public final static int INDEX_FAMILIAR = 0;
    public final static int INDEX_ENTERPRISE = 1;
    public final static int INDEX_COMEDITY = 2;
    public final static int INDEX_ITEM = 3;
    public final static int INDEX_SERVE = 4;
    public final static int INDEX_HOT = 6;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int PAGE_ITEM_COUNT = 100;

    public static final int ACCOUNT_TYPE_PERSON = 1;
    public static final int ACCOUNT_TYPE_ENTERPRISE = 2;

    public static final int ENTER_KIND_ENTERPRISE = 1;
    public static final int ENTER_KIND_PERSON = 2;

    public static final int ESTIMATE_KIND_FORWORD = 1;
    public static final int ESTIMATE_KIND_BACKWORD = 2;

    // realname cert relation
    public static final int ENTER_TYPE_ENTERPRISE = 1;
    public static final int ENTER_TYPE_PERSONAL = 2;

    // realname cert relation
    public static final int TEST_STATUS_READY = 1;
    public static final int TEST_STATUS_PASSED = 2;
    public static final int TEST_STATUS_REJECT = 3;

    // error kind
    public static final int ERROR_KIND_KUADA = 1;
    public static final int ERROR_KIND_XUJIA = 2;

    // Comedity Status
    public static final int COMEDITY_STATUS_DONWLOADED = 0;
    public static final int COMEDITY_STATUS_UPLOADED = 1;

    // read count relation
    public static final int VIEW_CNT_KIND_PERSONAL_OR_ENTER = 1;
    public static final int VIEW_CNT_KIND_HOT = 2;

    public static final int ERROR_OK = 200;

    public static final String STR_STAR = "☆";

    public  static int PICK_FROM_GALLARY = 1;
    public  static int PICK_FROM_CAMERA = 2;

    public static final String FILE_PREFIX_AUTH_IMAGE = "auth_image_";
    public static final String FILE_PREFIX_COMEDITY_IMAGE = "commodity_image_";
    public static final String FILE_PREFIX_ITEM_IMAGE = "item_image_";
    public static final String FILE_PREFIX_SERVE_IMAGE = "serve_image_";

    public static final String FILE_EXTENTION_AUTH_IMAGE = ".jpg";

    public static class UploadFileModel{
        public String fileTitle;
        public String fileName;
        public String filePath;
    }
}
