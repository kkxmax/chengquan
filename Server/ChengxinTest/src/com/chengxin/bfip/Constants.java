/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chengxin.bfip;

import com.chengxin.bfip.model.AccountDAO;
import com.chengxin.bfip.model.EstimateDAO;
import com.chengxin.bfip.model.FenleiDAO;
import com.chengxin.bfip.model.HotDAO;
import com.chengxin.bfip.model.NoticeDAO;
import com.chengxin.bfip.model.OpinionDAO;
import com.chengxin.common.KeyValueString;

/**
 *
 * @author Administrator
 */
public class Constants {
	
//	For Linux
	public static final String C_COMMON_FILE_PATH_SEP = "/";
	public static final String UPLOAD_TEMP_PATH = "/../Upload/image/temp";
	public static final String CAROUSEL_MEDIA_URL = "/Upload/image/carousel";
	public static final String CAROUSEL_MEDIA_PATH = "/../Upload/image/carousel";
	public static final String CERT_IMAGE_URL = "/Upload/image/cert";
	public static final String CERT_IMAGE_PATH = "/../Upload/image/cert";
	public static final String ESTIMATE_IMAGE_URL = "/Upload/image/estimate";
	public static final String ESTIMATE_IMAGE_PATH = "/../Upload/image/estimate";
	public static final String PRODUCT_IMAGE_URL = "/Upload/image/product";
	public static final String PRODUCT_IMAGE_PATH = "/../Upload/image/product";
	public static final String ITEM_IMAGE_URL = "/Upload/image/item";
	public static final String ITEM_IMAGE_PATH = "/../Upload/image/item";
	public static final String SERVICE_IMAGE_URL = "/Upload/image/service";
	public static final String SERVICE_IMAGE_PATH = "/../Upload/image/service";
	public static final String HOT_IMAGE_URL = "/Upload/image/hot";
	public static final String HOT_IMAGE_PATH = "/../Upload/image/hot";
	
//	For Windows
//	public static final String C_COMMON_FILE_PATH_SEP = "\\";
//	public static final String UPLOAD_TEMP_PATH = "\\..\\Upload\\image\\temp";
//	public static final String CAROUSEL_MEDIA_URL = "/Upload/image/carousel";
//	public static final String CAROUSEL_MEDIA_PATH = "\\..\\Upload\\image\\carousel";
//	public static final String CERT_IMAGE_URL = "/Upload/image/cert";
//	public static final String CERT_IMAGE_PATH = "\\..\\Upload\\image\\cert";
//	public static final String ESTIMATE_IMAGE_URL = "/Upload/image/estimate";
//	public static final String ESTIMATE_IMAGE_PATH = "\\..\\Upload\\image\\estimate";
//	public static final String PRODUCT_IMAGE_URL = "/Upload/image/product";
//	public static final String PRODUCT_IMAGE_PATH = "\\..\\Upload\\image\\product";
//	public static final String ITEM_IMAGE_URL = "/Upload/image/item";
//	public static final String ITEM_IMAGE_PATH = "\\..\\Upload\\image\\item";
//	public static final String SERVICE_IMAGE_URL = "/Upload/image/service";
//	public static final String SERVICE_IMAGE_PATH = "\\..\\Upload\\image\\service";
//	public static final String HOT_IMAGE_URL = "/Upload/image/hot";
//	public static final String HOT_IMAGE_PATH = "\\..\\Upload\\image\\hot";
	
    public static final String C_UPLOAD_PATH = "/ChengxinTest";
    public static final String C_NO_IMAGE = "/assets/custom/img/no_image.png";
    public static final String C_HOT_CROP_BG_IMAGE = "/assets/custom/img/hot_crop_bg.png";
    
	public static final int CAROUSEL_CNT_IN_APP = 8;
	public static final int MAXSIZE_MAIN_PRODUCT = 3;
	public static final int MAXSIZE_ACTIVE_CAROUSEL = 8;
	public static final int OP_ID_DEL = -1;
	
    public static final String[] C_DB_SECURE_KEY = {new String("WT2PwuSS")};

    public static final int VIEW_CNT_KIND_PERSONAL_OR_ENTER = 1;
    public static final int VIEW_CNT_KIND_HOT = 2;
    
    public static final String LOG_FILE_NAME = "log.txt";
    
    // 账号类型
    public static final KeyValueString[] C_ACCOUNT_ACCOUNT_TYPE = new KeyValueString[] {
    	new KeyValueString(String.valueOf(AccountDAO.ACCOUNT_TYPE_PERSONAL), "个人"),
    	new KeyValueString(String.valueOf(AccountDAO.ACCOUNT_TYPE_ENTERPRISE), "企业"),
    };

    // 企业类型
    public static final KeyValueString[] C_ACCOUNT_ENTER_TYPE = new KeyValueString[] {
    	new KeyValueString(String.valueOf(AccountDAO.ENTER_TYPE_PERSONAL), "企业"),
    	new KeyValueString(String.valueOf(AccountDAO.ENTER_TYPE_ENTERPRISE), "个体户"),
    };
    
    // 审核状态
    public static final KeyValueString[] C_ACCOUNT_TEST_STATUS = new KeyValueString[] {
    	new KeyValueString(String.valueOf(AccountDAO.TEST_ST_READY), "审核中"),
    	new KeyValueString(String.valueOf(AccountDAO.TEST_ST_PASSED), "审核通过"),
    	new KeyValueString(String.valueOf(AccountDAO.TEST_ST_UNPASSED), "审核未通过"),
    };

    // 禁用状态    
    public static final KeyValueString[] C_BAN_STATUS = new KeyValueString[] {
    	new KeyValueString(String.valueOf(AccountDAO.BAN_ST_UNBANED), "未禁用"),
    	new KeyValueString(String.valueOf(AccountDAO.BAN_ST_BANED), "已禁用"),
    };
    
    // 系统消息类型    
    public static final KeyValueString[] C_NOTICE_TYPE = new KeyValueString[] {
    	new KeyValueString(String.valueOf(NoticeDAO.NOTICE_KIND_ESTIMATE), "评价"),
    	new KeyValueString(String.valueOf(NoticeDAO.NOTICE_KIND_AUTH), "认证"),
    	new KeyValueString(String.valueOf(NoticeDAO.NOTICE_KIND_CORRECTION), "纠错"),
    	new KeyValueString(String.valueOf(NoticeDAO.NOTICE_KIND_INVITE), "邀请好友"),
    };
    
    // 系统消息状态
    public static final KeyValueString[] C_NOTICE_STATUS = new KeyValueString[] {
    	new KeyValueString(String.valueOf(NoticeDAO.NOTICE_ST_NEW), "未读"),
    	new KeyValueString(String.valueOf(NoticeDAO.NOTICE_ST_READ), "已读"),
    };
    
    // 分类类型
    public static final KeyValueString[] C_FENLEI_LEIXING = new KeyValueString[] {
    	new KeyValueString(String.valueOf(FenleiDAO.FENLEI_TYPE_ITEM), "项目"),
    	new KeyValueString(String.valueOf(FenleiDAO.FENLEI_TYPE_SERVICE), "服务"),
    };
    
    public static final KeyValueString[] C_HOTS_ST_NAME = new KeyValueString[] {
    	new KeyValueString(String.valueOf(HotDAO.HOTS_ST_UP), "已上架"),
    	new KeyValueString(String.valueOf(HotDAO.HOTS_ST_DOWN), "已下架")
    };
    
  //评价类型
    public static final KeyValueString[] C_ESTIMATE_KIND_NAME = new KeyValueString[] {
    	new KeyValueString(String.valueOf(EstimateDAO.ESTIMATE_KIND_FORWORD), "正面评价"),
    	new KeyValueString(String.valueOf(EstimateDAO.ESTIMATE_KIND_BACKWORD), "负面评价")
    };
    
    //评价方式
    public static final KeyValueString[] C_ESTIMATE_METHOD_NAME = new KeyValueString[] {
    	new KeyValueString(String.valueOf(EstimateDAO.ESTIMATE_METHOD_DETAIl), "详细评价"),
    	new KeyValueString(String.valueOf(EstimateDAO.ESTIMATE_METHOD_QUICK), "快捷评价")
    };

	//意见状态
	public static final Object C_OPINION_STATUS_NAME = new KeyValueString[] {
    	new KeyValueString(String.valueOf(OpinionDAO.OPINION_STATUS_NEW), "未处理"),
    	new KeyValueString(String.valueOf(OpinionDAO.OPINION_STATUS_PROCESSED), "已处理")
    };

}
