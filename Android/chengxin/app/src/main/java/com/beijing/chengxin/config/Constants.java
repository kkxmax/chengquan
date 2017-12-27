package com.beijing.chengxin.config;

import java.security.PublicKey;

public class Constants {

//    public static final boolean DEBUG_MODE = true;
    public static final boolean DEBUG_MODE = false;
//    public static final String SERVER_ADDR = "10.70.3.61";
    public static final String SERVER_ADDR = "cqegz.com";
//    public static final String CONTEXT_PATH = "BFIP";
    public static final String CONTEXT_PATH = "ChengxinTest";

//    public static final String API_ADDR = "http://" + SERVER_ADDR + ":" + SERVER_PORT + "/BFIP/api.html";
//    public static final String FILE_ADDR = "http://" + SERVER_ADDR + ":" + SERVER_PORT + "/BFIP";
    // add dd -- 2017.12.07
    public static final String API_ADDR = "http://" + SERVER_ADDR + "/" + CONTEXT_PATH +"/api.html";
    public static final String FILE_ADDR = "http://" + SERVER_ADDR + "/" + CONTEXT_PATH;
    public static final String INVITE_URL = "http://" + SERVER_ADDR + "/" + CONTEXT_PATH +"/invite.html?reqcode=%s&shareUserId=%s";
    public static final String LOGO_URL = "http://" + SERVER_ADDR + "/bfip_logo/ic_logo.png";
    public static final String BASE_URL = "http://" + SERVER_ADDR + "/" + CONTEXT_PATH + "/";

    public static final String ITEM_SHARE_URL = "http://" + SERVER_ADDR + "/" + CONTEXT_PATH + "/xiangmu.html?itemId=";
    public static final String SERVE_SHARE_URL = "http://" + SERVER_ADDR + "/"+ CONTEXT_PATH +"/fuwu.html?serviceId=";
    public static final String PERSONAL_REPORT_SHARE_URL = "http://" + SERVER_ADDR + "/"+ CONTEXT_PATH +"/report_geren.html?accountId=%s&shareUserId=%s";
    public static final String ENTERPRISE_REPORT_SHARE_URL = "http://" + SERVER_ADDR + "/"+ CONTEXT_PATH +"/report_qiye.html?accountId=%s&shareUserId=%s";
    public static final String ENTERPRISE_SHARE_URL = "http://" + SERVER_ADDR + "/"+ CONTEXT_PATH +"/qiye.html?accountId=%s&shareUserId=%s";
    public static final String PERSONAL_SHARE_URL = "http://" + SERVER_ADDR + "/"+ CONTEXT_PATH +"/geren.html?accountId=%s&shareUserId=%s";
    public static final String HOT_SHARE_URL = "http://" + SERVER_ADDR + "/"+ CONTEXT_PATH +"/redian.html?hotId=";
    public static final String COMEDITY_SHARE_URL = "http://" + SERVER_ADDR + "/"+ CONTEXT_PATH +"/chanpin.html?productId=";

    public static final class SHARE_KIND {
        public static final int COMEDITY = 1;
        public static final int ITEM = 2;
        public static final int SERVICE = 3;
        public static final int PERSONAL_DETAIL = 4;
        public static final int ENTERPRISE_DETAIL = 5;
        public static final int REPORT = 6;
        public static final int HOT = 7;
    };
    public static final String P_ACTION = "pAct";
    public static final String P_TOKEN = "token";

    public static final String IS_AUTO_LOGIN = "auto_login";

    public static final float LEVEL_ZERO = 0.01f;

    public static final String SEARCH_ORDER = "search_order";
    public static final String SEARCH_CITYNAME= "search_cityname";
    public static final String SEARCH_AKIND = "search_akind";
    public static final String SEARCH_XYLEIXINGID = "search_xyleixingid";
    public static final String SEARCH_KEYWORD = "search_keyword";
    public static final String SEARCH_HOME_KEYWORD_VALUE = "search_home_keyword_value";
    public static final String SEARCH_EVAL_KEYWORD_VALUE = "search_eval_keyword_value";

    public final static int SEARCH_IN_HOME = 0;
    public final static int SEARCH_IN_HOT = 1;
    public final static int SEARCH_IN_EVAL= 2;

    public final static int SEARCH_HOME_FAMILIAR = 0;
    public final static int SEARCH_HOME_ENTERPRISE= 1;
    public final static int SEARCH_HOME_COMEDITY = 2;
    public final static int SEARCH_HOME_ITEM = 3;
    public final static int SEARCH_HOME_SERVE = 4;
    public final static int SEARCH_HOME_CODE = 5;

    public static final String NOTIFY_SEARCH_COND_CHANGED = "search_condition_changed";
    public static final String NOTIFY_USERMODEL_CHANGED = "usermodel_changed";
    public static final String NOTIFY_FOLLOW_INFO_CHANGED = "follow_info_changed"; // 关注 - Sub Activity
    public static final String NOTIFY_FOLLOW_INFO_CHANGED_FRAGMENT = "follow_info_changed_fragment"; // 关注
    public static final String NOTIFY_NEWS_COUNT_CHANGED = "news_count_changed";

    public final static int INDEX_FAMILIAR = 0;
    public final static int INDEX_ENTERPRISE = 1;
    public final static int INDEX_COMEDITY = 2;
    public final static int INDEX_ITEM = 3;
    public final static int INDEX_SERVE = 4;
    public final static int INDEX_HOT = 6;
    public final static int INDEX_SELECT_ACCOUNT = 7;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int PAGE_ITEM_COUNT = 10;

    public static final int ACCOUNT_TYPE_PERSON = 1;
    public static final int ACCOUNT_TYPE_ENTERPRISE = 2;

    public static final int ENTER_KIND_ENTERPRISE = 1;
    public static final int ENTER_KIND_PERSON = 2;

    public static final int INTEREST_NO = 0;
    public static final int INTEREST_OK = 1;

    // evaluation type
    public static final int ESTIMATE_KIND_FORWORD = 1;
    public static final int ESTIMATE_KIND_BACKWORD = 2;

    public static final int ESTIMATE_METHOD_DETAIl = 1;
    public static final int ESTIMATE_METHOD_QUICK = 2;

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
    public static final int COMEDITY_MAIN_YES = 1;
    public static final int COMEDITY_MAIN_NO = 0;

    // read count relation
    public static final int VIEW_CNT_KIND_PERSONAL_OR_ENTER = 1;
    public static final int VIEW_CNT_KIND_HOT = 2;

    // system notification type
    public static final int NOTICE_KIND_ESTIMATE = 1;
    public static final int NOTICE_KIND_AUTH = 2;
    public static final int NOTICE_KIND_CORRECTION = 3;
    public static final int NOTICE_KIND_INVITE = 4;

    public static final int ERROR_OK = 200;
    public static final int ERROR_DUPLICATE = 297;

    public static final String STR_STAR = "☆";

    public static int PICK_FROM_GALLARY = 1;
    public static int PICK_FROM_CAMERA = 2;
    public static int ACTIVITY_ACCOUNT_SELECT = 1001;
    public static int ACTIVITY_MAKE_EVAL = 1002;

    // Activity Request Code
    public static final int REQEUST_CODE_MY_DEPLOY = 888;
    public static final int REQEUST_CODE_TEMP = 1231;
    // Activity Result Code
    public static final int RESULT_CODE_LOGIN_DUPLICATE = 1232;

    public static final String FILE_PREFIX_AUTH_IMAGE = "auth_image_";
    public static final String FILE_PREFIX_COMEDITY_IMAGE = "commodity_image_";
    public static final String FILE_PREFIX_ITEM_IMAGE = "item_image_";
    public static final String FILE_PREFIX_SERVE_IMAGE = "serve_image_";
    public static final String FILE_PREFIX_EVAL_IMAGE = "eval_image_";
    public static final String FILE_PREFIX_ERROR_CORRECT_IMAGE = "error_correct_image_";

    public static final String FILE_EXTENTION_AUTH_IMAGE = ".jpg";

    public static class UploadFileModel{
        public String fileTitle;
        public String fileName;
        public String filePath;
    }
}
