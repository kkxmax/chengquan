package com.chengxin.bfip.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.chengxin.bfip.CommonUtil;
import com.chengxin.bfip.Constants;
import com.chengxin.bfip.model.Account;
import com.chengxin.bfip.model.AccountDAO;
import com.chengxin.bfip.model.Carousel;
import com.chengxin.bfip.model.CarouselDAO;
import com.chengxin.bfip.model.City;
import com.chengxin.bfip.model.CityDAO;
import com.chengxin.bfip.model.ClickHistory;
import com.chengxin.bfip.model.ClickHistoryDAO;
import com.chengxin.bfip.model.Elect;
import com.chengxin.bfip.model.ElectDAO;
import com.chengxin.bfip.model.ErrorDAO;
import com.chengxin.bfip.model.Errors;
import com.chengxin.bfip.model.Estimate;
import com.chengxin.bfip.model.EstimateDAO;
import com.chengxin.bfip.model.Favourite;
import com.chengxin.bfip.model.FavouriteDAO;
import com.chengxin.bfip.model.Fenlei;
import com.chengxin.bfip.model.FenleiDAO;
import com.chengxin.bfip.model.Hot;
import com.chengxin.bfip.model.HotDAO;
import com.chengxin.bfip.model.Interest;
import com.chengxin.bfip.model.InterestDAO;
import com.chengxin.bfip.model.Item;
import com.chengxin.bfip.model.ItemDAO;
import com.chengxin.bfip.model.MarkLog;
import com.chengxin.bfip.model.MarkLogDAO;
import com.chengxin.bfip.model.Notice;
import com.chengxin.bfip.model.NoticeDAO;
import com.chengxin.bfip.model.Opinion;
import com.chengxin.bfip.model.OpinionDAO;
import com.chengxin.bfip.model.Pleixing;
import com.chengxin.bfip.model.PleixingDAO;
import com.chengxin.bfip.model.Product;
import com.chengxin.bfip.model.ProductDAO;
import com.chengxin.bfip.model.Province;
import com.chengxin.bfip.model.ProvinceDAO;
import com.chengxin.bfip.model.ReqCode;
import com.chengxin.bfip.model.ReqCodeDAO;
import com.chengxin.bfip.model.RoleDAO;
import com.chengxin.bfip.model.Service;
import com.chengxin.bfip.model.ServiceDAO;
import com.chengxin.bfip.model.UserDAO;
import com.chengxin.bfip.model.VerifyCode;
import com.chengxin.bfip.model.VerifyCodeDAO;
import com.chengxin.bfip.model.VideoDAO;
import com.chengxin.bfip.model.XingyeWatch;
import com.chengxin.bfip.model.XingyeWatchDAO;
import com.chengxin.bfip.model.XingyeWatched;
import com.chengxin.bfip.model.XingyeWatchedDAO;
import com.chengxin.bfip.model.Xyleixing;
import com.chengxin.bfip.model.XyleixingDAO;
import com.chengxin.common.AppSettings;
import com.chengxin.common.BaseController;
import com.chengxin.common.BinaryFormUtil;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.File;
import com.chengxin.common.SecureUtil;

/**
 *
 * @author Administrator
 */
public class ApiController extends BaseController {

	private static boolean MODE_DEVEL = false;
	private String strLoginMobile = "19135411631";
	private Account loginAccount = null;
	private JSONObject result = null;
	
    public ApiController() throws Exception {
		super();
	}

	private AccountDAO accountDao = null;
    private CarouselDAO carouselDao = null;
    private ClickHistoryDAO clickHistoryDao = null;
    private ErrorDAO errorDao = null;
    private EstimateDAO estimateDao = null;
    private FenleiDAO fenleiDao = null;
    private HotDAO hotDao = null;
    private ItemDAO itemDao = null;
    private NoticeDAO noticeDao = null;
    private OpinionDAO opinionDao = null;
    private PleixingDAO pleixingDao = null;
    private ProductDAO productDao = null;
    private RoleDAO roleDao = null;
    private ServiceDAO serviceDao = null;
    private UserDAO userDao = null;
    private VideoDAO videoDao = null;
    private XyleixingDAO xyleixingDao = null;
    private ProvinceDAO provinceDao = null;
    private CityDAO cityDao = null;
    private ReqCodeDAO reqCodeDao = null;
    private InterestDAO interestDao = null;
    private ElectDAO electDao = null;
    private VerifyCodeDAO verifyCodeDao = null;
    private XingyeWatchDAO xingyeWatchDao = null;
    private XingyeWatchedDAO xingyeWatchedDao = null;
    private FavouriteDAO favouriteDao = null;
    private MarkLogDAO markLogDao = null;
    
    public void setAccountDao(AccountDAO value) {this.accountDao = value;}
    public void setCarouselDao(CarouselDAO value) {this.carouselDao = value;}
    public void setClickHistoryDao(ClickHistoryDAO value) {this.clickHistoryDao = value;}
    public void setErrorDao(ErrorDAO value) {this.errorDao = value;}
    public void setEstimateDao(EstimateDAO value) {this.estimateDao = value;}
    public void setFenleiDao(FenleiDAO value) {this.fenleiDao = value;}
    public void setHotDao(HotDAO value) {this.hotDao = value;}
    public void setItemDao(ItemDAO value) {this.itemDao = value;}
    public void setNoticeDao(NoticeDAO value) {this.noticeDao = value;}
    public void setOpinionDao(OpinionDAO value) {this.opinionDao = value;}
    public void setPleixingDao(PleixingDAO value) {this.pleixingDao = value;}
    public void setProductDao(ProductDAO value) {this.productDao = value;}
    public void setRoleDao(RoleDAO value) {this.roleDao = value;}
    public void setServiceDao(ServiceDAO value) {this.serviceDao = value;}
    public void setUserDao(UserDAO value) {this.userDao = value;}
    public void setVideoDao(VideoDAO value) {this.videoDao = value;}
    public void setXyleixingDao(XyleixingDAO value) {this.xyleixingDao = value;}
    public void setProvinceDao(ProvinceDAO value) {this.provinceDao = value;}
    public void setCityDao(CityDAO value) {this.cityDao = value;}
    public void setReqCodeDao(ReqCodeDAO value) {this.reqCodeDao = value;}
    public void setInterestDao(InterestDAO value) {this.interestDao = value;}
    public void setElectDao(ElectDAO value) {this.electDao = value;}
    public void setVerifyCodeDao(VerifyCodeDAO value) {this.verifyCodeDao = value;}
    public void setXingyeWatchDao(XingyeWatchDAO value) {this.xingyeWatchDao = value;}
    public void setXingyeWatchedDao(XingyeWatchedDAO value) {this.xingyeWatchedDao = value;}
    public void setFavouriteDao(FavouriteDAO value) {this.favouriteDao = value;}
    public void setMarkLogDao(MarkLogDAO value) {this.markLogDao = value;}

    public ModelAndView init(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	if(MODE_DEVEL) {
    		return process(request, response, session);
    	}
    	else {
    		try {
        		return process(request, response, session);
        	}
        	catch(Exception e) {
        		result.put("retCode", 300);
        		result.put("class", e.getClass().toString());
        		result.put("msg", e.getMessage());
        		
        		return JSONResult(request, result);
        	}	
    	}
    }
    
    private ModelAndView process(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	result = new JSONObject();
    	
        String strAction = this.getBlankParameter(request, "pAct", "");
        
        if(strAction.isEmpty()) {
    		result.put("retCode", 299);
    		result.put("msg", "The request parameter named pAct is not provided");
    		
    		return JSONResult(request, result);
        }

        if(MODE_DEVEL) {
        	loginAccount = accountDao.getDetail("mobile='" + strLoginMobile + "'");
        }
        else {
        	if(!strAction.equals("getVerifyCode") && !strAction.equals("register") && !strAction.equals("login") && !strAction.equals("forgotPassword")) {
            	
            	String strToken = this.getBlankParameter(request, "token", "");
            	
            	if(strToken.isEmpty()) {
            		result.put("retCode", 298);
            		result.put("msg", "Token can't be empty");
            		
            		return JSONResult(request, result);
            	}
            	
            	loginAccount = accountDao.getDetail("token='" + strToken + "'");
            	
            	if(loginAccount == null) {
            		result.put("retCode", 297);
            		result.put("msg", "Token is invalid");
            		
            		return JSONResult(request, result);
            	}
            	
            	if(loginAccount.getBanStatus() == AccountDAO.BAN_ST_BANED) {
            		result.put("retCode", 296);
            		result.put("msg", "您是被系统管理员已禁用的。");
            		
            		return JSONResult(request, result);
            	}
            	
            	if(loginAccount.getTestStatus() != AccountDAO.TEST_ST_PASSED && (strAction.equals("makeCorrect") || strAction.equals("leaveEstimate"))) {
            		result.put("retCode", 295);
            		result.put("msg", "因为您还没有实名认证通过，无法使用该操作。");
            		
            		return JSONResult(request, result);
            	}
            }
        }
        
        if (strAction.equals("register")) {
            return this.register(request, response, session);
        } else if(strAction.equals("getVerifyCode")) {
        	return this.getVerifyCode(request, response, session);
        } else if(strAction.equals("login")) {
        	return this.login(request, response, session);
        } else if(strAction.equals("forgotPassword")) {
        	return this.forgotPassword(request, response, session);
        } else if(strAction.equals("resetPassword")) {
        	return this.resetPassword(request, response, session);
        } else if(strAction.equals("getXyleixingList")) {
        	return this.getXyleixingList(request, response, session);
        } else if(strAction.equals("getCityList")) {
        	return this.getCityList(request, response, session);
        } else if(strAction.equals("getCompanyList")) {
        	return this.getCompanyList(request, response, session);
        } else if(strAction.equals("authPersonal")) {
        	return this.authPersonal(request, response, session);
        } else if(strAction.equals("authEnter")) {
        	return this.authEnter(request, response, session);
        } else if(strAction.equals("getCarouselList")) {
        	return this.getCarouselList(request, response, session);
        } else if(strAction.equals("getFriendList")) {
        	return this.getFriendList(request, response, session);
        } else if(strAction.equals("getEnterList")) {
        	return this.getEnterList(request, response, session);
        } else if(strAction.equals("getAccountList")) {
        	return this.getAccountList(request, response, session);
        } else if(strAction.equals("setInterest")) {
        	return this.setInterest(request, response, session);
        } else if(strAction.equals("setFavourite")) {
        	return this.setFavourite(request, response, session);
        } else if(strAction.equals("getProductList")) {
        	return this.getProductList(request, response, session);
        } else if(strAction.equals("getItemList")) {
        	return this.getItemList(request, response, session);
        } else if(strAction.equals("getServiceList")) {
        	return this.getServiceList(request, response, session);
        } else if(strAction.equals("getPleixingList")) {
        	return this.getPleixingList(request, response, session);
        } else if(strAction.equals("getItemFenleiList")) {
        	return this.getItemFenleiList(request, response, session);
        } else if(strAction.equals("getServiceFenleiList")) {
        	return this.getServiceFenleiList(request, response, session);
        } else if(strAction.equals("getHotList")) {
        	return this.getHotList(request, response, session);
        } else if(strAction.equals("getAccountListForEstimate")) {
        	return this.getAccountListForEstimate(request, response, session);
        } else if(strAction.equals("getMyEstimateList")) {
        	return this.getMyEstimateList(request, response, session);
        } else if(strAction.equals("getEstimateToMeList")) {
        	return this.getEstimateToMeList(request, response, session);
        } else if(strAction.equals("getMyInterestInfo")) {
        	return this.getMyInterestInfo(request, response, session);
        } else if(strAction.equals("getMyInterestList")) {
        	return this.getMyInterestList(request, response, session);
        } else if(strAction.equals("getAccountDetail")) {
        	return this.getAccountDetail(request, response, session);
        } else if(strAction.equals("leaveEstimate")) {
        	return this.leaveEstimate(request, response, session);
        } else if(strAction.equals("leaveReply")) {
        	return this.leaveReply(request, response, session);
        } else if(strAction.equals("addProduct")) {
        	return this.addProduct(request, response, session);
        } else if(strAction.equals("addItem")) {
        	return this.addItem(request, response, session);
        } else if(strAction.equals("addService")) {
        	return this.addService(request, response, session);
        } else if(strAction.equals("incViewCount")) {
        	return this.incViewCount(request, response, session);
        } else if(strAction.equals("getInviterInfo")) {
        	return this.getInviterInfo(request, response, session);
        } else if(strAction.equals("getMarkLogList")) {
        	return this.getMarkLogList(request, response, session);
        } else if(strAction.equals("getEstimateListForHot")) {
        	return this.getEstimateListForHot(request, response, session);
        } else if(strAction.equals("getPartnerList")) {
        	return this.getPartnerList(request, response, session);
        } else if(strAction.equals("getMyFavouriteList")) {
        	return this.getMyFavouriteList(request, response, session);
        } else if(strAction.equals("getProductDetail")) {
        	return this.getProductDetail(request, response, session);
        } else if(strAction.equals("getHotDetail")) {
        	return this.getHotDetail(request, response, session);
        } else if(strAction.equals("makeCorrect")) {
        	return this.makeCorrect(request, response, session);
        } else if(strAction.equals("getErrorList")) {
        	return this.getErrorList(request, response, session);
        } else if(strAction.equals("getMyProductList")) {
        	return this.getMyProductList(request, response, session);
        } else if(strAction.equals("getMyItemList")) {
        	return this.getMyItemList(request, response, session);
        } else if(strAction.equals("getMyServiceList")) {
        	return this.getMyServiceList(request, response, session);
        } else if(strAction.equals("upProduct")) {
        	return this.upProduct(request, response, session);
        } else if(strAction.equals("downProduct")) {
        	return this.downProduct(request, response, session);
        } else if(strAction.equals("deleteProduct")) {
        	return this.deleteProduct(request, response, session);
        } else if(strAction.equals("upItem")) {
        	return this.upItem(request, response, session);
        } else if(strAction.equals("downItem")) {
        	return this.downItem(request, response, session);
        } else if(strAction.equals("deleteItem")) {
        	return this.deleteItem(request, response, session);
        } else if(strAction.equals("upService")) {
        	return this.upService(request, response, session);
        } else if(strAction.equals("downService")) {
        	return this.downService(request, response, session);
        } else if(strAction.equals("deleteService")) {
        	return this.deleteService(request, response, session);
        } else if(strAction.equals("leaveOpinion")) {
        	return this.leaveOpinion(request, response, session);
        } else if(strAction.equals("inviteFriend")) {
        	return this.inviteFriend(request, response, session);
        } else if(strAction.equals("onPurchase")) {
        	return this.onPurchase(request, response, session);
        } else if(strAction.equals("onContact")) {
        	return this.onContact(request, response, session);
        } else if(strAction.equals("onShare")) {
        	return this.onShare(request, response, session);
        } else if(strAction.equals("elect")) {
        	return this.elect(request, response, session);
        } else if(strAction.equals("getNoticeList")) {
        	return this.getNoticeList(request, response, session);
        } else if(strAction.equals("getNoticeCount")) {
        	return this.getNoticeCount(request, response, session);
        } else {
    		result.put("retCode", 296);
    		result.put("msg", "API doesn't exist");
    		
    		return JSONResult(request, result);
        }
    }
    
    private ModelAndView JSONResult(HttpServletRequest request, JSONObject object) {
    	request.setAttribute("JSON", object);
    	return new ModelAndView("json_result");
    }
    
    private ModelAndView register(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strMobile = this.getBlankParameter(request, "mobile", "");
    	String strReqCode = this.getBlankParameter(request, "reqCode", "");
    	String strVerifyCode = this.getBlankParameter(request, "verifyCode", "");
    	String strPassword = this.getBlankParameter(request, "password", "");
    	
    	if(strMobile.isEmpty() || strVerifyCode.isEmpty()) {
    		result.put("retCode", 202);
    		result.put("msg", "手机号/验证码不能为空");
    		
    		return JSONResult(request, result);
    	}
    	if(strMobile.length() != 11) {
    		result.put("retCode", 201);
    		result.put("msg", "手机号不正确");
    		
    		return JSONResult(request, result);
    	}
    	ReqCode reqCode = null;
    	if(!strReqCode.isEmpty()) {
    		reqCode = reqCodeDao.getDetail("req_code='" + strReqCode + "' and status=0");
    		if(reqCode == null) {
    			result.put("retCode", 208);
        		result.put("msg", "该邀请码是不存在或已用过的");
        		
        		return JSONResult(request, result);
    		}
    	}
    	
    	VerifyCode verifyCode = verifyCodeDao.getDetail("mobile=" + strMobile);
    	
    	if(verifyCode == null || !strVerifyCode.equals(verifyCode.getVerifyCode()) 
    			|| DateTimeUtil.getDifferenceOfMillisecond(verifyCode.getWriteTime(), new Date()) > 10 * 60 * 1000) {
    		result.put("retCode", 203);
    		result.put("msg", "验证码不正确");
    		
    		return JSONResult(request, result);
    	}
    	if(strPassword.length() < 6 || strPassword.length() > 20) {
    		result.put("retCode", 206);
    		result.put("msg", "密码由6-20数字和字母组成");
    		
    		return JSONResult(request, result);
    	}
    	
    	if(accountDao.count(null, "mobile=" + strMobile) > 0) {
    		result.put("retCode", 207);
    		result.put("msg", "该手机号已注册");
    		
    		return JSONResult(request, result);
    	}
    	
    	Account account = new Account();
    	account.setMobile(strMobile);
    	account.setPassword(SecureUtil.getMD5(strPassword));
    	account.setAkind(AccountDAO.ACCOUNT_TYPE_PERSONAL);
    	
    	if(reqCode != null) {
    		account.setReqCodeId(reqCode.getId());
    	}
    	
    	accountDao.insert(account);
    	
    	account = accountDao.getDetail("mobile=" + strMobile);
    	
    	String strToken = SecureUtil.getSHA1(CommonUtil.genRand6String());
    	
    	account.setToken(strToken);
    	
    	accountDao.update(account);
    	
    	if(reqCode != null) {

    		reqCode.setStatus(ReqCodeDAO.STATUS_USED);
    		reqCodeDao.update(reqCode);
    		
    		CommonUtil.insertMarkLog(markLogDao, reqCode.getSender(), MarkLogDAO.LOG_KIND_INVITE, null, null, null, MarkLogDAO.PMARK_INVITE, null, account.getId());
    		
    		CommonUtil.insertNotice(noticeDao, NoticeDAO.NOTICE_TYPE_USER, reqCode.getSender(), NoticeDAO.NOTICE_KIND_INVITE
    				, null
    				, CommonUtil.getNoticeMsgTitle(NoticeDAO.NOTICE_KIND_INVITE)
    				, CommonUtil.getNoticeMsgContent(NoticeDAO.NOTICE_KIND_INVITE, null, MarkLogDAO.PMARK_INVITE , null)
    				, account.getId(), null, null);
    	}
    	
    	result.put("retCode", 200);
    	result.put("token", strToken);
    	result.put("userInfo", account);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getVerifyCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strMobile = this.getBlankParameter(request, "mobile", "");
    	
    	if(strMobile.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("verifyCode", "手机不能为空");
        	
        	return JSONResult(request, result);
    	}
    	
    	String strVerifyCode = CommonUtil.genVerifyCode();
    	
    	VerifyCode verifyCode = verifyCodeDao.getDetail("mobile=" + strMobile);
    	
    	if(verifyCode == null) {
    		verifyCode = new VerifyCode();
    		
    		verifyCode.setMobile(strMobile);
        	verifyCode.setVerifyCode(strVerifyCode);
        	
        	verifyCodeDao.insert(verifyCode);
    	}
    	else {
        	verifyCode.setVerifyCode(strVerifyCode);
        	verifyCode.setWriteTime(new Date());
        	
        	verifyCodeDao.update(verifyCode);
    	}
    	
    	result.put("retCode", 200);
    	result.put("verifyCode", strVerifyCode);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String mobile = this.getBlankParameter(request, "mobile", "");
    	String password = this.getBlankParameter(request, "password", "");
    	
    	if(mobile.isEmpty() || password.isEmpty()) {
    		result.put("retCode", 202);
    		result.put("msg", "手机号/密码不能为空");
    		
    		return JSONResult(request, result);
    	}
    	if(mobile.length() != 11) {
    		result.put("retCode", 201);
    		result.put("msg", "手机号不正确");
    		
    		return JSONResult(request, result);
    	}
    	
    	Account record = accountDao.getDetail("mobile = '" + mobile + "'");
    	
    	if(record == null) {
    		result.put("retCode", 204);
	    	result.put("msg", "该账号不存在");
	    	
	    	return JSONResult(request, result);
    	}
    	if(!record.getPassword().equals(SecureUtil.getMD5(password))) {
			result.put("retCode", 203);
	    	result.put("msg", "密码不正确");
	    	
	    	return JSONResult(request, result);
		}
    	if(record.getBanStatus() == AccountDAO.BAN_ST_BANED) {
    		result.put("retCode", 204);
	    	result.put("msg", "您是被系统管理员已禁用的");
	    	
	    	return JSONResult(request, result);
    	}
		
    	String strToken = SecureUtil.getSHA1(String.valueOf((new Random().nextInt(899999999) + 100000000)));
    	
    	record.setToken(strToken);
    	accountDao.update(record);
    	
    	result.put("retCode", 200);
    	result.put("token", strToken);
    	result.put("userInfo", record);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView forgotPassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strMobile = this.getBlankParameter(request, "mobile", "");
    	String strVerifyCode = this.getBlankParameter(request, "verifyCode", "");
    	String strPassword = this.getBlankParameter(request, "password", "");
    	
    	if(strMobile.isEmpty() || strVerifyCode.isEmpty()) {
    		result.put("retCode", 202);
    		result.put("msg", "手机号/验证码不能为空");
    		
    		return JSONResult(request, result);
    	}
    	if(strMobile.length() != 11) {
    		result.put("retCode", 201);
    		result.put("msg", "手机号不正确");
    		
    		return JSONResult(request, result);
    	}
    	
    	VerifyCode verifyCode = verifyCodeDao.getDetail("mobile=" + strMobile);
    	
    	if(verifyCode == null || !strVerifyCode.equals(verifyCode.getVerifyCode()) 
    			|| DateTimeUtil.getDifferenceOfMillisecond(verifyCode.getWriteTime(), new Date()) > 10 * 60 * 1000) {
    		result.put("retCode", 203);
    		result.put("msg", "验证码不正确");
    		
    		return JSONResult(request, result);
    	}
    	if(strPassword.length() < 6 || strPassword.length() > 20) {
    		result.put("retCode", 206);
    		result.put("msg", "密码由6-20数字和字母组成");
    		
    		return JSONResult(request, result);
    	}
    	
    	Account account = accountDao.getDetail("mobile=" + strMobile);
    	
    	if(account == null) {
    		result.put("retCode", 207);
    		result.put("msg", "该手机号已注册");
    		
    		return JSONResult(request, result);
    	}
    	
    	account.setPassword(SecureUtil.getMD5(strPassword));
    	
    	accountDao.update(account);
    	    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strOldPass = this.getBlankParameter(request, "oldPass", "");
    	String strNewPass = this.getBlankParameter(request, "newPass", "");
    	
    	if(strOldPass.isEmpty() || strNewPass.isEmpty()) {
    		result.put("retCode", 201);
    		result.put("msg", "旧密码/新密码不能为空");
    		
    		return JSONResult(request, result);
    	}
    	
    	if(strNewPass.length() < 6 || strNewPass.length() > 20) {
    		result.put("retCode", 202);
    		result.put("msg", "密码由6-20数字和字母组成");
    		
    		return JSONResult(request, result);
    	}
    	
    	if(!SecureUtil.getMD5(strOldPass).equals(loginAccount.getPassword())) {
    		result.put("retCode", 203);
    		result.put("msg", "密码错误");
    		
    		return JSONResult(request, result);
    	}
    	
    	loginAccount.setPassword(SecureUtil.getMD5(strNewPass));
    	
    	accountDao.update(loginAccount);
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getXyleixingList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strXyleixingId = this.getBlankParameter(request, "xyleixingId", "0");
    	
    	List<Xyleixing> resultArray = new ArrayList<Xyleixing>();
    	
    	List<Integer> watchXyArray = CommonUtil.getXingyeWatchArray(xingyeWatchDao, loginAccount.getId());
    	List<Integer> watchedXyArray = CommonUtil.getXingyeWatchedArray(xingyeWatchedDao, loginAccount.getId());
    	
    	List<Xyleixing> xylist1 = xyleixingDao.search(null, "upper_id=" + strXyleixingId, "write_time asc");
    	for(Xyleixing item : xylist1) {
    		List<Xyleixing> xylist2 = xyleixingDao.search(null, "upper_id=" + item.getId(), "write_time asc");
    		item.setChildren(xylist2);
    		if(!xylist2.isEmpty()) {
    			
    			for(Xyleixing xy : xylist2) {
    				if(watchXyArray.contains(xy.getId())) {
    					xy.setIsMyWatch(1);
    				}
    				if(watchedXyArray.contains(xy.getId())) {
    					xy.setIsMyWatched(1);
    				}
    			}
    			resultArray.add(item);
    		}
    	}
    	
    	result.put("retCode", 200);
    	result.put("data", resultArray);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView authPersonal(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	String strImageFileLogo = "";
    	
    	BinaryFormUtil formUtil = new BinaryFormUtil(
				CommonUtil.getRepositoryRealPath(request) + Constants.CERT_IMAGE_PATH,
				CommonUtil.getRepositoryRealPath(request) + Constants.UPLOAD_TEMP_PATH,
				this.getSetting(AppSettings.C_COMMON_FILE_PATH_SEP));

		formUtil.initForm(request, response, this.getSetting(AppSettings.C_DEFAULT_ENCODING));
	
    	String strRealname = formUtil.getString("realname", "");
    	String strCertNum = formUtil.getString("certNum", "");
    	String strEnterName = formUtil.getString("enterName", "");
    	String strXyleixingId = formUtil.getString("xyleixingId", "");
    	String strCityId = formUtil.getString("cityId", "");
    	String strJob = formUtil.getString("job", "");
    	String strWeixin = formUtil.getString("weixin", "");
    	String strExperience = formUtil.getString("experience", "");
    	String strHistory = formUtil.getString("history", "");
    	String strXyWatch = formUtil.getString("xyWatch", "");
    	String strXyWatched = formUtil.getString("xyWatched", "");
    	
    	if(loginAccount.getTestStatus() == AccountDAO.TEST_ST_READY) {
    		result.put("retCode", 210);
    		result.put("msg", "全部不可编辑");
    		
    		return JSONResult(request, result);
    	}
    	
    	if(loginAccount.getTestStatus() == AccountDAO.TEST_ST_NEW) {
    		if(strRealname.isEmpty()) {
        		result.put("retCode", 201);
        		result.put("msg", "真实姓名不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strCertNum.isEmpty()) {
        		result.put("retCode", 202);
        		result.put("msg", "身份证号码不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strEnterName.isEmpty()) {
        		result.put("retCode", 203);
        		result.put("msg", "公司不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strXyleixingId.isEmpty()) {
        		result.put("retCode", 204);
        		result.put("msg", "行业不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strCityId.isEmpty()) {
        		result.put("retCode", 205);
        		result.put("msg", "所在地不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strJob.isEmpty()) {
        		result.put("retCode", 206);
        		result.put("msg", "职位不能为空");
        		
        		return JSONResult(request, result);
        	}
        	
        	File imgFileLogo = formUtil.getFile("logo");

    		if(imgFileLogo == null) {
    			result.put("retCode", 209);
        		result.put("msg", "logo照片不能为空");
        		
        		return JSONResult(request, result);
    		}
    		
    		strImageFileLogo = Constants.CERT_IMAGE_URL + "/" + imgFileLogo.getPhysicalPath() + "/" + imgFileLogo.getPhysicalName();
    		
        	File imgFileCert = formUtil.getFile("certImage");
    		
    		if(imgFileCert == null) {
    			result.put("retCode", 208);
        		result.put("msg", "身份证照片不能为空");
        		
        		return JSONResult(request, result);
    		}
    		
    		String strImageFileCert = Constants.CERT_IMAGE_URL + "/" + imgFileCert.getPhysicalPath() + "/" + imgFileCert.getPhysicalName();
    		
    		if(MODE_DEVEL) {
    			Logger.getLogger("bfip").log(Level.INFO, strImageFileCert);	
    		}
        	
    		loginAccount.setLogo(strImageFileLogo);
        	loginAccount.setAkind(AccountDAO.ACCOUNT_TYPE_PERSONAL);
        	if(loginAccount.getCode().isEmpty()) {
        		String strCode = CommonUtil.genAccountCode(cityDao, accountDao, Integer.valueOf(strCityId));
        		loginAccount.setCode(strCode);
        	}
        	loginAccount.setRealname(strRealname);
        	loginAccount.setCertNum(strCertNum);
        	loginAccount.setCertImage(strImageFileCert);
        	loginAccount.setEnterName(strEnterName);
        	loginAccount.setXyleixingId(Integer.valueOf(strXyleixingId));
        	loginAccount.setCityId(Integer.valueOf(strCityId));
        	loginAccount.setJob(strJob);
        	loginAccount.setWeixin(strWeixin);
        	loginAccount.setExperience(strExperience);
        	loginAccount.setHistory(strHistory);
        	loginAccount.setTestStatus(AccountDAO.TEST_ST_READY);
        	
        	accountDao.update(loginAccount);
        	
        	List<XingyeWatch> xyWatchs = xingyeWatchDao.search(null, "account_id=" + loginAccount.getId());
        	for(XingyeWatch item : xyWatchs) {
        		xingyeWatchDao.delete(item);
        	}
        	for(String item : strXyWatch.split(",")) {
        		if(item.isEmpty()) {
        			continue;
        		}
        		XingyeWatch xingyeWatch = new XingyeWatch();
        		
        		xingyeWatch.setAccountId(loginAccount.getId());
        		xingyeWatch.setXyleixingId(Integer.valueOf(item));
        		
        		xingyeWatchDao.insert(xingyeWatch);
        	}
        	
        	List<XingyeWatched> xyWatcheds = xingyeWatchedDao.search(null, "account_id=" + loginAccount.getId());
        	for(XingyeWatched item : xyWatcheds) {
        		xingyeWatchedDao.delete(item);
        	}
        	for(String item : strXyWatched.split(",")) {
        		if(item.isEmpty()) {
        			continue;
        		}
        		XingyeWatched xingyeWatched = new XingyeWatched();
        		
        		xingyeWatched.setAccountId(loginAccount.getId());
        		xingyeWatched.setXyleixingId(Integer.valueOf(item));
        		
        		xingyeWatchedDao.insert(xingyeWatched);
        	}
        	
        	CommonUtil.insertNotice(noticeDao, NoticeDAO.NOTICE_TYPE_ADMIN, null, NoticeDAO.NOTICE_KIND_AUTH, null, null
        			, strRealname + "提交了认证申请，请及时处理", loginAccount.getId(), null, null);
        	    	    	
        	result.put("retCode", 200);
        	
        	return JSONResult(request, result);
    	}
    	else if(loginAccount.getTestStatus() == AccountDAO.TEST_ST_UNPASSED) {
    		if(strRealname.isEmpty()) {
        		result.put("retCode", 201);
        		result.put("msg", "真实姓名不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strCertNum.isEmpty()) {
        		result.put("retCode", 202);
        		result.put("msg", "身份证号码不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strEnterName.isEmpty()) {
        		result.put("retCode", 203);
        		result.put("msg", "公司不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strXyleixingId.isEmpty()) {
        		result.put("retCode", 204);
        		result.put("msg", "行业不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strCityId.isEmpty()) {
        		result.put("retCode", 205);
        		result.put("msg", "所在地不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strJob.isEmpty()) {
        		result.put("retCode", 206);
        		result.put("msg", "职位不能为空");
        		
        		return JSONResult(request, result);
        	}
        	
        	File imgFileLogo = formUtil.getFile("logo");
    		if(imgFileLogo != null) {
    			strImageFileLogo = Constants.CERT_IMAGE_URL + "/" + imgFileLogo.getPhysicalPath() + "/" + imgFileLogo.getPhysicalName();
    			loginAccount.setLogo(strImageFileLogo);
    		}
    		
        	File imgFileCert = formUtil.getFile("certImage");
    		if(imgFileCert != null) {
    			String strImageFileCert = Constants.CERT_IMAGE_URL + "/" + imgFileCert.getPhysicalPath() + "/" + imgFileCert.getPhysicalName();
    			loginAccount.setCertImage(strImageFileCert);
    		}
        	loginAccount.setAkind(AccountDAO.ACCOUNT_TYPE_PERSONAL);
        	if(loginAccount.getCode().isEmpty()) {
        		String strCode = CommonUtil.genAccountCode(cityDao, accountDao, Integer.valueOf(strCityId));
        		loginAccount.setCode(strCode);
        	}
        	loginAccount.setRealname(strRealname);
        	loginAccount.setCertNum(strCertNum);
        	loginAccount.setEnterName(strEnterName);
        	loginAccount.setXyleixingId(Integer.valueOf(strXyleixingId));
        	loginAccount.setCityId(Integer.valueOf(strCityId));
        	loginAccount.setJob(strJob);
        	loginAccount.setWeixin(strWeixin);
        	loginAccount.setExperience(strExperience);
        	loginAccount.setHistory(strHistory);
        	loginAccount.setTestStatus(AccountDAO.TEST_ST_READY);
        	
        	accountDao.update(loginAccount);
        	
        	List<XingyeWatch> xyWatchs = xingyeWatchDao.search(null, "account_id=" + loginAccount.getId());
        	for(XingyeWatch item : xyWatchs) {
        		xingyeWatchDao.delete(item);
        	}
        	for(String item : strXyWatch.split(",")) {
        		if(item.isEmpty()) {
        			continue;
        		}
        		XingyeWatch xingyeWatch = new XingyeWatch();
        		
        		xingyeWatch.setAccountId(loginAccount.getId());
        		xingyeWatch.setXyleixingId(Integer.valueOf(item));
        		
        		xingyeWatchDao.insert(xingyeWatch);
        	}
        	
        	List<XingyeWatched> xyWatcheds = xingyeWatchedDao.search(null, "account_id=" + loginAccount.getId());
        	for(XingyeWatched item : xyWatcheds) {
        		xingyeWatchedDao.delete(item);
        	}
        	for(String item : strXyWatched.split(",")) {
        		if(item.isEmpty()) {
        			continue;
        		}
        		XingyeWatched xingyeWatched = new XingyeWatched();
        		
        		xingyeWatched.setAccountId(loginAccount.getId());
        		xingyeWatched.setXyleixingId(Integer.valueOf(item));
        		
        		xingyeWatchedDao.insert(xingyeWatched);
        	}
        	
        	CommonUtil.insertNotice(noticeDao, NoticeDAO.NOTICE_TYPE_ADMIN, null, NoticeDAO.NOTICE_KIND_AUTH, null, null
        			, strRealname + "提交了认证申请，请及时处理", loginAccount.getId(), null, null);
        	    	    	
        	result.put("retCode", 200);
        	
        	return JSONResult(request, result);
    	}
    	else if(loginAccount.getTestStatus() == AccountDAO.TEST_ST_PASSED) {
    		
        	if(strEnterName.isEmpty()) {
        		result.put("retCode", 203);
        		result.put("msg", "公司不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strXyleixingId.isEmpty()) {
        		result.put("retCode", 204);
        		result.put("msg", "行业不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strCityId.isEmpty()) {
        		result.put("retCode", 205);
        		result.put("msg", "所在地不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strJob.isEmpty()) {
        		result.put("retCode", 206);
        		result.put("msg", "职位不能为空");
        		
        		return JSONResult(request, result);
        	}
        	
        	File imgFileLogo = formUtil.getFile("logo");

    		if(imgFileLogo != null) {
    			strImageFileLogo = Constants.CERT_IMAGE_URL + "/" + imgFileLogo.getPhysicalPath() + "/" + imgFileLogo.getPhysicalName();
        		
        		loginAccount.setLogo(strImageFileLogo);
    		}
        	if(loginAccount.getCode().isEmpty()) {
        		String strCode = CommonUtil.genAccountCode(cityDao, accountDao, Integer.valueOf(strCityId));
        		loginAccount.setCode(strCode);
        	}
        	loginAccount.setEnterName(strEnterName);
        	loginAccount.setXyleixingId(Integer.valueOf(strXyleixingId));
        	loginAccount.setCityId(Integer.valueOf(strCityId));
        	loginAccount.setJob(strJob);
        	loginAccount.setWeixin(strWeixin);
        	loginAccount.setExperience(strExperience);
        	loginAccount.setHistory(strHistory);
        	
        	accountDao.update(loginAccount);
        	
        	List<XingyeWatch> xyWatchs = xingyeWatchDao.search(null, "account_id=" + loginAccount.getId());
        	for(XingyeWatch item : xyWatchs) {
        		xingyeWatchDao.delete(item);
        	}
        	for(String item : strXyWatch.split(",")) {
        		if(item.isEmpty()) {
        			continue;
        		}
        		XingyeWatch xingyeWatch = new XingyeWatch();
        		
        		xingyeWatch.setAccountId(loginAccount.getId());
        		xingyeWatch.setXyleixingId(Integer.valueOf(item));
        		
        		xingyeWatchDao.insert(xingyeWatch);
        	}
        	
        	List<XingyeWatched> xyWatcheds = xingyeWatchedDao.search(null, "account_id=" + loginAccount.getId());
        	for(XingyeWatched item : xyWatcheds) {
        		xingyeWatchedDao.delete(item);
        	}
        	for(String item : strXyWatched.split(",")) {
        		if(item.isEmpty()) {
        			continue;
        		}
        		XingyeWatched xingyeWatched = new XingyeWatched();
        		
        		xingyeWatched.setAccountId(loginAccount.getId());
        		xingyeWatched.setXyleixingId(Integer.valueOf(item));
        		
        		xingyeWatchedDao.insert(xingyeWatched);
        	}
        	
        	result.put("retCode", 200);
        	
        	return JSONResult(request, result);
    	}
    	else {
    		result.put("retCode", 220);
    		result.put("msg", "错误，请联系管理员");
        	
        	return JSONResult(request, result);
    	}
    }
    
    private ModelAndView authEnter(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strImageFileLogo = "";
    	
    	BinaryFormUtil formUtil = new BinaryFormUtil(
				CommonUtil.getRepositoryRealPath(request) + Constants.CERT_IMAGE_PATH,
				CommonUtil.getRepositoryRealPath(request) + Constants.UPLOAD_TEMP_PATH,
				this.getSetting(AppSettings.C_COMMON_FILE_PATH_SEP));

		formUtil.initForm(request, response, this.getSetting(AppSettings.C_DEFAULT_ENCODING));
		
    	String strEnterKind = formUtil.getString("enterKind", "");
    	String strEnterName = formUtil.getString("enterName", "");
    	String strXyleixingId = formUtil.getString("xyleixingId", "");
    	String strCityId = formUtil.getString("cityId", "");
    	String strAddr = formUtil.getString("addr", "");
    	String strWebUrl = formUtil.getString("webUrl", "");
    	String strWeixin = formUtil.getString("weixin", "");
    	String strMainJob = formUtil.getString("mainJob", "");
    	String strEnterCertNum = formUtil.getString("enterCertNum", "");
    	String strComment = formUtil.getString("comment", "");
    	String strRecommend = formUtil.getString("recommend", "");
    	String strBossName = formUtil.getString("bossName", "");
    	String strBossJob = formUtil.getString("bossJob", "");
    	String strBossMobile = formUtil.getString("bossMobile", "");
    	String strBossWeixin = formUtil.getString("bossWeixin", "");
    	String strXyWatch = formUtil.getString("xyWatch", "");
    	String strXyWatched = formUtil.getString("xyWatched", "");
    	
    	if(loginAccount.getTestStatus() == AccountDAO.TEST_ST_READY) {
    		result.put("retCode", 210);
    		result.put("msg", "全部不可编辑");
    		
    		return JSONResult(request, result);
    	}
    	
    	if(loginAccount.getTestStatus() == AccountDAO.TEST_ST_NEW) {
    		if(strEnterKind.isEmpty()) {
        		result.put("retCode", 201);
        		result.put("msg", "企业类型不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strEnterCertNum.isEmpty()) {
        		result.put("retCode", 202);
        		result.put("msg", "营业执照编号不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strEnterName.isEmpty()) {
        		result.put("retCode", 203);
        		result.put("msg", "公司全称不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strXyleixingId.isEmpty()) {
        		result.put("retCode", 204);
        		result.put("msg", "行业不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strCityId.isEmpty()) {
        		result.put("retCode", 205);
        		result.put("msg", "办公地址不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strMainJob.isEmpty()) {
        		result.put("retCode", 206);
        		result.put("msg", "主营业务不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strComment.isEmpty()) {
        		result.put("retCode", 207);
        		result.put("msg", "公司介绍不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strRecommend.isEmpty()) {
        		result.put("retCode", 208);
        		result.put("msg", "我们承诺不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strBossName.isEmpty()) {
        		result.put("retCode", 209);
        		result.put("msg", "负责人姓名不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strBossJob.isEmpty()) {
        		result.put("retCode", 210);
        		result.put("msg", "负责人职位不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strBossMobile.isEmpty()) {
        		result.put("retCode", 211);
        		result.put("msg", "负责人手机号不能为空");
        		
        		return JSONResult(request, result);
        	}
    		
        	File imgFileLogo = formUtil.getFile("logo");

    		if(imgFileLogo == null) {
    			result.put("retCode", 213);
        		result.put("msg", "logo照片不能为空");
        		
        		return JSONResult(request, result);
    		}
    		
    		strImageFileLogo = Constants.CERT_IMAGE_URL + "/" + imgFileLogo.getPhysicalPath() + "/" + imgFileLogo.getPhysicalName();
    		
        	File imgFileCert = formUtil.getFile("enterCertImage");
    		
    		if(imgFileCert == null) {
    			result.put("retCode", 212);
        		result.put("msg", "营业执照照片不能为空");
        		
        		return JSONResult(request, result);
    		}
    		
    		String strImageFileCert = Constants.CERT_IMAGE_URL + "/" + imgFileCert.getPhysicalPath() + "/" + imgFileCert.getPhysicalName();
    		
    		if(MODE_DEVEL) {
    			Logger.getLogger("bfip").log(Level.INFO, strImageFileCert);	
    		}
    		
    		loginAccount.setLogo(strImageFileLogo);
        	loginAccount.setAkind(AccountDAO.ACCOUNT_TYPE_ENTERPRISE);
        	if(loginAccount.getCode().isEmpty()) {
        		String strCode = CommonUtil.genAccountCode(cityDao, accountDao, Integer.valueOf(strCityId));
        		loginAccount.setCode(strCode);
        	}
        	loginAccount.setEnterKind(Integer.valueOf(strEnterKind));
        	loginAccount.setEnterName(strEnterName);
        	loginAccount.setEnterCertNum(strEnterCertNum);
        	loginAccount.setEnterCertImage(strImageFileCert);
        	loginAccount.setXyleixingId(Integer.valueOf(strXyleixingId));
        	loginAccount.setCityId(Integer.valueOf(strCityId));
        	loginAccount.setAddr(strAddr);
        	loginAccount.setWeburl(strWebUrl);
        	loginAccount.setWeixin(strWeixin);
        	loginAccount.setMainJob(strMainJob);
        	loginAccount.setComment(strComment);
        	loginAccount.setRecommend(strRecommend);
        	loginAccount.setBossName(strBossName);
        	loginAccount.setBossJob(strBossJob);
        	loginAccount.setBossMobile(strBossMobile);
        	loginAccount.setBossWeixin(strBossWeixin);
        	loginAccount.setTestStatus(AccountDAO.TEST_ST_READY);
        	
        	accountDao.update(loginAccount);
        	    	  
        	List<XingyeWatch> xyWatchs = xingyeWatchDao.search(null, "account_id=" + loginAccount.getId());
        	for(XingyeWatch item : xyWatchs) {
        		xingyeWatchDao.delete(item);
        	}
        	for(String item : strXyWatch.split(",")) {
        		if(item.isEmpty()) {
        			continue;
        		}
        		XingyeWatch xingyeWatch = new XingyeWatch();
        		
        		xingyeWatch.setAccountId(loginAccount.getId());
        		xingyeWatch.setXyleixingId(Integer.valueOf(item));
        		
        		xingyeWatchDao.insert(xingyeWatch);
        	}
        	
        	List<XingyeWatched> xyWatcheds = xingyeWatchedDao.search(null, "account_id=" + loginAccount.getId());
        	for(XingyeWatched item : xyWatcheds) {
        		xingyeWatchedDao.delete(item);
        	}
        	for(String item : strXyWatched.split(",")) {
        		if(item.isEmpty()) {
        			continue;
        		}
        		XingyeWatched xingyeWatched = new XingyeWatched();
        		
        		xingyeWatched.setAccountId(loginAccount.getId());
        		xingyeWatched.setXyleixingId(Integer.valueOf(item));
        		
        		xingyeWatchedDao.insert(xingyeWatched);
        	}
        	
        	CommonUtil.insertNotice(noticeDao, NoticeDAO.NOTICE_TYPE_ADMIN, null, NoticeDAO.NOTICE_KIND_AUTH, null, null
        			, strEnterName + "提交了认证申请，请及时处理", loginAccount.getId(), null, null);
        	
        	result.put("retCode", 200);
        	
        	return JSONResult(request, result);
    	}
    	else if(loginAccount.getTestStatus() == AccountDAO.TEST_ST_UNPASSED) {
    		if(strEnterKind.isEmpty()) {
        		result.put("retCode", 201);
        		result.put("msg", "企业类型不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strEnterCertNum.isEmpty()) {
        		result.put("retCode", 202);
        		result.put("msg", "营业执照编号不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strEnterName.isEmpty()) {
        		result.put("retCode", 203);
        		result.put("msg", "公司全称不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strXyleixingId.isEmpty()) {
        		result.put("retCode", 204);
        		result.put("msg", "行业不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strCityId.isEmpty()) {
        		result.put("retCode", 205);
        		result.put("msg", "办公地址不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strMainJob.isEmpty()) {
        		result.put("retCode", 206);
        		result.put("msg", "主营业务不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strComment.isEmpty()) {
        		result.put("retCode", 207);
        		result.put("msg", "公司介绍不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strRecommend.isEmpty()) {
        		result.put("retCode", 208);
        		result.put("msg", "我们承诺不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strBossName.isEmpty()) {
        		result.put("retCode", 209);
        		result.put("msg", "负责人姓名不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strBossJob.isEmpty()) {
        		result.put("retCode", 210);
        		result.put("msg", "负责人职位不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strBossMobile.isEmpty()) {
        		result.put("retCode", 211);
        		result.put("msg", "负责人手机号不能为空");
        		
        		return JSONResult(request, result);
        	}
    		
        	File imgFileLogo = formUtil.getFile("logo");
    		if(imgFileLogo != null) {
    			strImageFileLogo = Constants.CERT_IMAGE_URL + "/" + imgFileLogo.getPhysicalPath() + "/" + imgFileLogo.getPhysicalName();
    			loginAccount.setLogo(strImageFileLogo);
    		}
    		
        	File imgFileCert = formUtil.getFile("enterCertImage");
    		if(imgFileCert != null) {
    			String strImageFileCert = Constants.CERT_IMAGE_URL + "/" + imgFileCert.getPhysicalPath() + "/" + imgFileCert.getPhysicalName();
    			loginAccount.setEnterCertImage(strImageFileCert);
    		}
        	loginAccount.setAkind(AccountDAO.ACCOUNT_TYPE_ENTERPRISE);
        	if(loginAccount.getCode().isEmpty()) {
        		String strCode = CommonUtil.genAccountCode(cityDao, accountDao, Integer.valueOf(strCityId));
        		loginAccount.setCode(strCode);
        	}
        	loginAccount.setEnterKind(Integer.valueOf(strEnterKind));
        	loginAccount.setEnterName(strEnterName);
        	loginAccount.setEnterCertNum(strEnterCertNum);
        	
        	loginAccount.setXyleixingId(Integer.valueOf(strXyleixingId));
        	loginAccount.setCityId(Integer.valueOf(strCityId));
        	loginAccount.setAddr(strAddr);
        	loginAccount.setWeburl(strWebUrl);
        	loginAccount.setWeixin(strWeixin);
        	loginAccount.setMainJob(strMainJob);
        	loginAccount.setComment(strComment);
        	loginAccount.setRecommend(strRecommend);
        	loginAccount.setBossName(strBossName);
        	loginAccount.setBossJob(strBossJob);
        	loginAccount.setBossMobile(strBossMobile);
        	loginAccount.setBossWeixin(strBossWeixin);
        	loginAccount.setTestStatus(AccountDAO.TEST_ST_READY);
        	
        	accountDao.update(loginAccount);
        	    	  
        	List<XingyeWatch> xyWatchs = xingyeWatchDao.search(null, "account_id=" + loginAccount.getId());
        	for(XingyeWatch item : xyWatchs) {
        		xingyeWatchDao.delete(item);
        	}
        	for(String item : strXyWatch.split(",")) {
        		if(item.isEmpty()) {
        			continue;
        		}
        		XingyeWatch xingyeWatch = new XingyeWatch();
        		
        		xingyeWatch.setAccountId(loginAccount.getId());
        		xingyeWatch.setXyleixingId(Integer.valueOf(item));
        		
        		xingyeWatchDao.insert(xingyeWatch);
        	}
        	
        	List<XingyeWatched> xyWatcheds = xingyeWatchedDao.search(null, "account_id=" + loginAccount.getId());
        	for(XingyeWatched item : xyWatcheds) {
        		xingyeWatchedDao.delete(item);
        	}
        	for(String item : strXyWatched.split(",")) {
        		if(item.isEmpty()) {
        			continue;
        		}
        		XingyeWatched xingyeWatched = new XingyeWatched();
        		
        		xingyeWatched.setAccountId(loginAccount.getId());
        		xingyeWatched.setXyleixingId(Integer.valueOf(item));
        		
        		xingyeWatchedDao.insert(xingyeWatched);
        	}
        	
        	CommonUtil.insertNotice(noticeDao, NoticeDAO.NOTICE_TYPE_ADMIN, null, NoticeDAO.NOTICE_KIND_AUTH, null, null
        			, strEnterName + "提交了认证申请，请及时处理", loginAccount.getId(), null, null);
        	
        	result.put("retCode", 200);
        	
        	return JSONResult(request, result);
    	}
    	else if(loginAccount.getTestStatus() == AccountDAO.TEST_ST_PASSED) {

    		if(strComment.isEmpty()) {
        		result.put("retCode", 207);
        		result.put("msg", "公司介绍不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strRecommend.isEmpty()) {
        		result.put("retCode", 208);
        		result.put("msg", "我们承诺不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strBossName.isEmpty()) {
        		result.put("retCode", 209);
        		result.put("msg", "负责人姓名不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strBossJob.isEmpty()) {
        		result.put("retCode", 210);
        		result.put("msg", "负责人职位不能为空");
        		
        		return JSONResult(request, result);
        	}
        	if(strBossMobile.isEmpty()) {
        		result.put("retCode", 211);
        		result.put("msg", "负责人手机号不能为空");
        		
        		return JSONResult(request, result);
        	}
    		
        	File imgFileLogo = formUtil.getFile("logo");
    		if(imgFileLogo != null) {
    			strImageFileLogo = Constants.CERT_IMAGE_URL + "/" + imgFileLogo.getPhysicalPath() + "/" + imgFileLogo.getPhysicalName();
    			loginAccount.setLogo(strImageFileLogo);
    		}
        	if(loginAccount.getCode().isEmpty()) {
        		String strCode = CommonUtil.genAccountCode(cityDao, accountDao, Integer.valueOf(strCityId));
        		loginAccount.setCode(strCode);
        	}
        	loginAccount.setWeburl(strWebUrl);
        	loginAccount.setWeixin(strWeixin);
        	loginAccount.setComment(strComment);
        	loginAccount.setRecommend(strRecommend);
        	loginAccount.setBossName(strBossName);
        	loginAccount.setBossJob(strBossJob);
        	loginAccount.setBossMobile(strBossMobile);
        	loginAccount.setBossWeixin(strBossWeixin);
        	
        	accountDao.update(loginAccount);
        	    	  
        	List<XingyeWatch> xyWatchs = xingyeWatchDao.search(null, "account_id=" + loginAccount.getId());
        	for(XingyeWatch item : xyWatchs) {
        		xingyeWatchDao.delete(item);
        	}
        	for(String item : strXyWatch.split(",")) {
        		if(item.isEmpty()) {
        			continue;
        		}
        		XingyeWatch xingyeWatch = new XingyeWatch();
        		
        		xingyeWatch.setAccountId(loginAccount.getId());
        		xingyeWatch.setXyleixingId(Integer.valueOf(item));
        		
        		xingyeWatchDao.insert(xingyeWatch);
        	}
        	
        	List<XingyeWatched> xyWatcheds = xingyeWatchedDao.search(null, "account_id=" + loginAccount.getId());
        	for(XingyeWatched item : xyWatcheds) {
        		xingyeWatchedDao.delete(item);
        	}
        	for(String item : strXyWatched.split(",")) {
        		if(item.isEmpty()) {
        			continue;
        		}
        		XingyeWatched xingyeWatched = new XingyeWatched();
        		
        		xingyeWatched.setAccountId(loginAccount.getId());
        		xingyeWatched.setXyleixingId(Integer.valueOf(item));
        		
        		xingyeWatchedDao.insert(xingyeWatched);
        	}
        	
        	result.put("retCode", 200);
        	
        	return JSONResult(request, result);
    	}
    	else {
    		result.put("retCode", 220);
    		result.put("msg", "错误，请联系管理员");
        	
        	return JSONResult(request, result);
    	}
    }
    
    private ModelAndView getFriendList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strWhere = "ban_status !=" + AccountDAO.BAN_ST_BANED;
    	List<Account> accountList = new ArrayList<Account>();
    	List<Product> products = null;
    	List<Item> items = null;
    	List<Service> services = null;
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	String strOrder = this.getBlankParameter(request, "order", "1");
    	String strCityName = this.getBlankParameter(request, "cityName", "");
    	String strAkind = this.getBlankParameter(request, "akind", "0");
    	String strXyleixingIds = this.getBlankParameter(request, "xyleixingIds", "0");
    	String strKeyword = this.getBlankParameter(request, "keyword", "");
    	String strKeywordCode = this.getBlankParameter(request, "keywordCode", "");
    	
    	String strOrderby = "view_cnt desc";
    	
    	if(strOrder.equals("1")) {
    		strOrderby = "view_cnt desc, write_time desc";
    	} else if(strOrder.equals("2")) {
    		strOrderby = "credit desc, write_time desc";
    	} else if(strOrder.equals("3")) {
    		strOrderby = "write_time desc";
    	} else {
    		result.put("retCode", 201);
    		result.put("msg", "order is incorrect");
    		
    		return JSONResult(request, result);
    	}
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	String strFriendIds = loginAccount.getFriend1();
    	
    	if(!loginAccount.getFriend2().isEmpty()) {
    		strFriendIds += "," + loginAccount.getFriend2();
    	}
    	if(!loginAccount.getFriend3().isEmpty()) {
    		strFriendIds += "," + loginAccount.getFriend3();
    	}
    	
    	if(strFriendIds.isEmpty()) {
    		result.put("retCode", 200);
        	result.put("data", accountList);
        	
        	return JSONResult(request, result);
    	}
    	
    	strWhere += " and id in (" + strFriendIds + ")";
    	
    	if(!strKeywordCode.isEmpty()) {
    		strWhere += " and code like '%" + strKeywordCode + "%'";
    	}
    	else if(!strKeyword.isEmpty()) {
    		strWhere += " and account_name like '%" + strKeyword + "%'";
    	}
    	if(!strAkind.equals("0")) {
    		strWhere += " and akind=" + strAkind;
    	}
    	
    	if(!strCityName.isEmpty()) {
    		strWhere += " and (city_name like '%" + strCityName + "%' or province_name like '%" + strCityName + "%')";
    	}
    	if(!strXyleixingIds.equals("0")) {
    		strWhere += " and xyleixing_id in (" + CommonUtil.getSubXyIds(xyleixingDao, strXyleixingIds.split(",")) + ")";
    	}
    	
		accountList = accountDao.search(filter, strWhere, strOrderby);
    	
    	List<Integer> myInterests = CommonUtil.getInterestIds(interestDao, loginAccount.getId());
    	
    	for(Account item : accountList) {
    		if(myInterests.contains(item.getId())) {
    			item.setInterested(1);
    		}
    		
    		products = productDao.search(null, "is_main=1 and account_id=" + item.getId());
    		items = itemDao.search(null, "account_id=" + item.getId());
    		services = serviceDao.search(null, "account_id=" + item.getId());
    		
    		item.setProducts(products);
    		item.setItems(items);
    		item.setServices(services);
    	}
    	
    	result.put("retCode", 200);
    	result.put("data", accountList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getEnterList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	String strOrder = this.getBlankParameter(request, "order", "1");
    	String strCityName = this.getBlankParameter(request, "cityName", "");
    	String strEnterKind = this.getBlankParameter(request, "enterKind", "0");
    	String strXyleixingIds = this.getBlankParameter(request, "xyleixingIds", "0");
    	String strKeyword = this.getBlankParameter(request, "keyword", "");
    	
    	String strWhere = "ban_status !=" + AccountDAO.BAN_ST_BANED;
    	strWhere += " and akind=" + AccountDAO.ACCOUNT_TYPE_ENTERPRISE;
    	List<Product> products = null;
    	List<Item> items = null;
    	List<Service> services = null;
    	
    	String strOrderby = "view_cnt desc";
    	
    	if(strOrder.equals("1")) {
    		strOrderby = "view_cnt desc, write_time desc";
    	} else if(strOrder.equals("2")) {
    		strOrderby = "credit desc, write_time desc";
    	} else if(strOrder.equals("3")) {
    		strOrderby = "write_time desc";
    	} else {
    		result.put("retCode", 201);
    		result.put("msg", "order is incorrect");
    		
    		return JSONResult(request, result);
    	}
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	if(!strKeyword.isEmpty()) {
    		strWhere += " and account_name like '%" + strKeyword + "%'";
    	}
    	if(!strEnterKind.equals("0")) {
    		strWhere += " and enter_kind=" + strEnterKind;
    	}
    	if(!strCityName.isEmpty()) {
    		strWhere += " and (city_name like '%" + strCityName + "%' or province_name like '%" + strCityName + "%')";
    	}
    	if(!strXyleixingIds.equals("0")) {
    		strWhere += " and xyleixing_id in (" + CommonUtil.getSubXyIds(xyleixingDao, strXyleixingIds.split(",")) + ")";
    	}
    	
		List<Account> accountList = accountDao.search(filter, strWhere, strOrderby);
    	
    	List<Integer> myInterests = CommonUtil.getInterestIds(interestDao, loginAccount.getId());
    	
    	for(Account item : accountList) {
    		if(myInterests.contains(item.getId())) {
    			item.setInterested(1);
    		}
    		
    		products = productDao.search(null, "is_main=1 and account_id=" + item.getId());
    		items = itemDao.search(null, "account_id=" + item.getId());
    		services = serviceDao.search(null, "account_id=" + item.getId());
    		
    		item.setProducts(products);
    		item.setItems(items);
    		item.setServices(services);
    	}
    	
    	result.put("retCode", 200);
    	result.put("data", accountList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getAccountList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strWhere = "1";
    	List<Product> products = null;
    	List<Item> items = null;
    	List<Service> services = null;
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	String strOrder = this.getBlankParameter(request, "order", "1");
    	String strCityName = this.getBlankParameter(request, "cityName", "");
    	String strXyleixingId = this.getBlankParameter(request, "xyleixingId", "0");
    	String strKeyword = this.getBlankParameter(request, "keyword", "");
    	
    	String strOrderby = "view_cnt desc";
    	
    	if(strOrder.equals("1")) {
    		strOrderby = "view_cnt desc, write_time desc";
    	} else if(strOrder.equals("2")) {
    		strOrderby = "credit desc, write_time desc";
    	} else if(strOrder.equals("3")) {
    		strOrderby = "write_time desc";
    	} else {
    		result.put("retCode", 201);
    		result.put("msg", "order is incorrect");
    		
    		return JSONResult(request, result);
    	}
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	if(!strKeyword.isEmpty()) {
    		strWhere += " and code like '% " + strKeyword + "%'";
    	}
    	if(!strCityName.isEmpty()) {
    		strWhere += " and (city_name like '%" + strCityName + "%' or province_name like '%" + strCityName + "%')";
    	}
    	if(!strXyleixingId.equals("0")) {
    		strWhere += " and xyleixing_id in (" + CommonUtil.getSubXyIds(xyleixingDao, Integer.parseInt(strXyleixingId)) + ")";
    	}
    	
		List<Account> accountList = accountDao.search(filter, strWhere, strOrderby);
    	
    	List<Integer> myInterests = CommonUtil.getInterestIds(interestDao, loginAccount.getId());
    	
    	for(Account item : accountList) {
    		if(myInterests.contains(item.getId())) {
    			item.setInterested(1);
    		}
    		
    		products = productDao.search(null, "is_main=1");
    		items = itemDao.search(null);
    		services = serviceDao.search(null);
    		
    		item.setProducts(products);
    		item.setItems(items);
    		item.setServices(services);
    	}
    	
    	result.put("retCode", 200);
    	result.put("data", accountList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView setInterest(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strAccountId = this.getBlankParameter(request, "accountId", "");
    	String strVal = this.getBlankParameter(request, "val", "1");
    	
    	if(strAccountId.isEmpty()) {
    		result.put("retCode", 201);
    		result.put("msg", "accountId is empty");
    		
    		return JSONResult(request, result);
    	}
    	
    	Interest interest = interestDao.getDetail("owner=" + loginAccount.getId() + " and target=" + Integer.valueOf(strAccountId));
    	
    	if(strVal.equals("1")) {
    		if(interest == null) {
        		interest = new Interest();
        		
        		interest.setOwner(loginAccount.getId());
        		interest.setTarget(Integer.valueOf(strAccountId));
        		
        		interestDao.insert(interest);
        	}	
    	}
    	else if(strVal.equals("0")) {
    		if(interest != null) {
    			interestDao.delete(interest);	
    		}
    	}
    	else {
    		result.put("retCode", 202);
    		result.put("msg", "val is empty!");
    		
    		return JSONResult(request, result);
    	}
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getCarouselList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", 0);
    	filter.put("length", Constants.CAROUSEL_CNT_IN_APP);
    	
    	List<Carousel> carouselList = carouselDao.search(filter, "status=1", "ord asc");
    	
    	result.put("retCode", 200);
    	result.put("data", carouselList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getCityList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	List<Province> list = provinceDao.search();
    	
    	for(Province item : list) {
    		List<City> cityList = cityDao.search(null, "province_id=" + item.getId());
    		item.setCities(cityList);
    	}
    	
    	result.put("retCode", 200);
    	result.put("data", list);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getCompanyList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strKeyword = this.getBlankParameter(request, "keyword", "");
    	
    	List<String> clist = new ArrayList<String>();
    	List<Account> alist = accountDao.search(null, "enter_name != '' and enter_name like '%" + strKeyword + "%'", "enter_name asc", "enter_name");
    	
    	for(Account item : alist) {
    		clist.add(item.getEnterName());
    	}
    	
    	result.put("retCode", 200);
    	result.put("data", clist);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getProductList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	String strWhere = "status=" + HotDAO.HOTS_ST_UP;
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	String strOrder = this.getBlankParameter(request, "order", "1");
    	String strCityName = this.getBlankParameter(request, "cityName", "");
    	String strPleixingIds = this.getBlankParameter(request, "pleixingIds", "0");
    	String strKeyword = this.getBlankParameter(request, "keyword", "");
    	
    	String strOrderCol = "account_view_cnt";
    	String strOrderDir = "desc";
    	
    	if(strOrder.equals("1")) {
    		strOrderCol = "account_view_cnt";
    	} else if(strOrder.equals("2")) {
    		strOrderCol = "account_credit";
    	} else if(strOrder.equals("3")) {
    		strOrderCol = "write_time";
    	} else {
    		result.put("retCode", 201);
    		result.put("msg", "order is incorrect");
    		
    		return JSONResult(request, result);
    	}
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	if(!strCityName.isEmpty()) {
    		strWhere += " and (city_name like '%" + strCityName + "%' or province_name like '%" + strCityName + "%')";
    	}
    	if(!strPleixingIds.equals("0")) {
    		strWhere += " and pleixing_id in (" + CommonUtil.getSubPIds(pleixingDao, strPleixingIds.split(",")) + ")";
    	}
    	if(!strKeyword.isEmpty()) {
    		strWhere += " and name like '%" + strKeyword + "%'";
    	}
    	
		List<Product> productList = productDao.search(filter, strWhere, strOrderCol + " " + strOrderDir);
		
		CommonUtil.setProductFavouriteStatus(favouriteDao, loginAccount.getId(), productList);
    	
    	result.put("retCode", 200);
    	result.put("data", productList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getItemList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	String strWhere = "status=" + HotDAO.HOTS_ST_UP;
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	String strOrder = this.getBlankParameter(request, "order", "1");
    	String strCityName = this.getBlankParameter(request, "cityName", "");
    	String strAkind = this.getBlankParameter(request, "akind", "0");
    	String strFenleiIds = this.getBlankParameter(request, "fenleiIds", "0");
    	String strKeyword = this.getBlankParameter(request, "keyword", "");
    	
    	String strOrderCol = "account_view_cnt";
    	String strOrderDir = "desc";
    	
    	if(strOrder.equals("1")) {
    		strOrderCol = "account_view_cnt";
    	} else if(strOrder.equals("2")) {
    		strOrderCol = "account_credit";
    	} else if(strOrder.equals("3")) {
    		strOrderCol = "write_time";
    	} else {
    		result.put("retCode", 201);
    		result.put("msg", "order is incorrect");
    		
    		return JSONResult(request, result);
    	}
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	if(!strAkind.equals("0")) {
    		strWhere += " and akind=" + Integer.valueOf(strAkind);	
    	}
    	if(!strCityName.isEmpty()) {
    		strWhere += " (city_name like '%" + strCityName + "%' or province_name like '%" + strCityName + "%')";
    	}
    	if(!strFenleiIds.equals("0")) {
    		strWhere += " and fenlei_id in (" + strFenleiIds + ")";
    	}
    	if(!strKeyword.isEmpty()) {
    		strWhere += " and name like '%" + strKeyword + "%'";
    	}
    	
		List<Item> itemList = itemDao.search(filter, strWhere, strOrderCol + " " + strOrderDir);
    	
    	result.put("retCode", 200);
    	result.put("data", itemList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getServiceList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	String strWhere = "status=" + HotDAO.HOTS_ST_UP;
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	String strOrder = this.getBlankParameter(request, "order", "1");
    	String strCityName = this.getBlankParameter(request, "cityName", "");
    	String strAkind = this.getBlankParameter(request, "akind", "0");
    	String strFenleiIds = this.getBlankParameter(request, "fenleiIds", "0");
    	String strKeyword = this.getBlankParameter(request, "keyword", "");
    	
    	String strOrderCol = "account_view_cnt";
    	String strOrderDir = "desc";
    	
    	if(strOrder.equals("1")) {
    		strOrderCol = "account_view_cnt";
    	} else if(strOrder.equals("2")) {
    		strOrderCol = "account_credit";
    	} else if(strOrder.equals("3")) {
    		strOrderCol = "write_time";
    	} else {
    		result.put("retCode", 201);
    		result.put("msg", "order is incorrect");
    		
    		return JSONResult(request, result);
    	}
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	if(!strAkind.equals("0")) {
    		strWhere += " and akind=" + Integer.valueOf(strAkind);	
    	}
    	if(!strCityName.isEmpty()) {
    		strWhere += " (city_name like '%" + strCityName + "%' or province_name like '%" + strCityName + "%')";
    	}
    	if(!strFenleiIds.equals("0")) {
    		strWhere += " and fenlei_id in (" + strFenleiIds + ")";
    	}
    	if(!strKeyword.isEmpty()) {
    		strWhere += " and name like '%" + strKeyword + "%'";
    	}
    	
		List<Service> serviceList = serviceDao.search(filter, strWhere, strOrderCol + " " + strOrderDir);
    	
    	result.put("retCode", 200);
    	result.put("data", serviceList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getPleixingList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strPleixingId = this.getBlankParameter(request, "pleixingId", "0");
    	
    	List<Pleixing> resultArray = new ArrayList<Pleixing>();
    	
    	List<Pleixing> plist1 = pleixingDao.search(null, "upper_id=" + strPleixingId, "write_time asc");
    	for(Pleixing item : plist1) {
    		List<Pleixing> plist2 = pleixingDao.search(null, "upper_id=" + item.getId(), "write_time asc");
			item.setChildren(plist2);
    		if(!plist2.isEmpty()) {
    			resultArray.add(item);
    		}
    	}
    	
    	result.put("retCode", 200);
    	result.put("data", resultArray);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getItemFenleiList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	List<Fenlei> list = fenleiDao.search(null, "leixing=" + FenleiDAO.FENLEI_TYPE_ITEM, "write_time asc");
    	
    	result.put("retCode", 200);
    	result.put("data", list);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getServiceFenleiList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	List<Fenlei> list = fenleiDao.search(null, "leixing=" + FenleiDAO.FENLEI_TYPE_SERVICE, "write_time asc");
    	
    	result.put("retCode", 200);
    	result.put("data", list);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getHotList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	String strWhere = "status=" + HotDAO.HOTS_ST_UP;
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	
    	String strOrderCol = "write_time";
    	String strOrderDir = "desc";
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
		List<Hot> hotList = hotDao.search(filter, strWhere, strOrderCol + " " + strOrderDir);
		
		CommonUtil.setHotElectStatus(electDao, loginAccount.getId(), hotList);
		CommonUtil.setHotFavouriteStatus(favouriteDao, loginAccount.getId(), hotList);
    	
    	result.put("retCode", 200);
    	result.put("data", hotList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getAccountListForEstimate(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	String strWhere = "1";
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	String strAkind = this.getBlankParameter(request, "akind", "0");
    	String strXyleixingIds = this.getBlankParameter(request, "xyleixingIds", "0");
    	String strKeyword = this.getBlankParameter(request, "keyword", "");
    	
    	String strOrderCol = "feedback_cnt";
    	String strOrderDir = "desc";
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	if(!strAkind.equals("0")) {
    		strWhere += " and akind=" + strAkind;
    	}
    	if(!strXyleixingIds.equals("0")) {
    		strWhere += " and xyleixing_id in (" + CommonUtil.getSubXyIds(xyleixingDao, strXyleixingIds.split(",")) + ")";
    	}
    	if(!strKeyword.isEmpty()) {
    		strWhere += " and (code like '%" + strKeyword + "%' or account_name like '%" + strKeyword + "%')";
    	}
    	
		List<Account> accountList = accountDao.search(filter, strWhere, strOrderCol + " " + strOrderDir + ",write_time desc");
    	
    	result.put("retCode", 200);
    	result.put("data", accountList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getMyEstimateList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	return getEstimateMyAndToMeList(request, response, session, 1);
    }
    
    private ModelAndView getEstimateToMeList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	return getEstimateMyAndToMeList(request, response, session, 2);
    }
    
    private ModelAndView getEstimateMyAndToMeList(HttpServletRequest request, HttpServletResponse response, HttpSession session, int type) throws Exception {
    	String strWhere = "1";
    	
    	if(type == 1) {
    		strWhere = "(upper_id is null or upper_id=0) and type=" + EstimateDAO.ESTIMATE_TYPE_PERSONAL_OR_ENTER + " and owner=" + loginAccount.getId();
    	}
    	else if(type == 2) {
    		strWhere = "(upper_id is null or upper_id=0) and type=" + EstimateDAO.ESTIMATE_TYPE_PERSONAL_OR_ENTER + " and account_id=" + loginAccount.getId();	
    	}
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	String strAkind = this.getBlankParameter(request, "akind", "0");
    	String strKind = this.getBlankParameter(request, "kind", "0");
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	if(!strAkind.equals("0")) {
    		strWhere += " and target_account_akind=" + strAkind;
    	}
    	if(!strKind.equals("0")) {
    		strWhere += " and kind=" + strKind;
    	}
    	
		List<Estimate> estimateList = estimateDao.search(filter, strWhere, "write_time desc");
    	
    	result.put("retCode", 200);
    	result.put("data", estimateList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getMyInterestInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	
    	String strOrderCol = "credit";
    	String strOrderDir = "desc";
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	int personalCnt = interestDao.count(null, "owner=" + loginAccount.getId() + " and akind=" + AccountDAO.ACCOUNT_TYPE_PERSONAL);
    	int enterCnt = interestDao.count(null, "owner=" + loginAccount.getId() + " and akind=" + AccountDAO.ACCOUNT_TYPE_ENTERPRISE);
    	int myAncestorCnt = CommonUtil.getInterestIds(interestDao, loginAccount.getId()).contains(loginAccount.getReqCodeSenderId()) ? 1 : 0;
    	int friend1Cnt = loginAccount.getFriend1().split(",").length;
    	int friend2Cnt = loginAccount.getFriend2().split(",").length;
    	int friend3Cnt = loginAccount.getFriend3().split(",").length;
    	
    	String strMyXingyeWatchs = CommonUtil.getXingyeWatchs(xingyeWatchDao, loginAccount.getId());
    	
    	String strAccountIds = CommonUtil.getXingyeWatcheds(xingyeWatchedDao, strMyXingyeWatchs);
    	
//    	String strInterestIds = CommonUtil.getInterestIdsStr(interestDao, loginAccount.getId());
    	
    	List<Account> accountList = new ArrayList<Account>();
    	
    	String strWhere = "1";

//    	if(!strInterestIds.isEmpty()) {
//    		strWhere += " and id not in(" + strInterestIds + ")";
//    	}
    	if(!strAccountIds.isEmpty()) {
    		strWhere += " and id in(" + strAccountIds + ")";
        	
    		accountList = accountDao.search(filter, strWhere, strOrderCol + " " + strOrderDir + ", write_time desc");
    	}
    	
    	CommonUtil.setInterestStatus(interestDao, loginAccount.getId(), accountList);
    	
    	result.put("retCode", 200);
    	result.put("personalCnt", personalCnt);
    	result.put("enterCnt", enterCnt);
    	result.put("myAncestorCnt", myAncestorCnt);
    	result.put("friend1Cnt", friend1Cnt);
    	result.put("friend2Cnt", friend2Cnt);
    	result.put("friend3Cnt", friend3Cnt);
    	result.put("accountsRecommended", accountList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getMyInterestList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	String strWhere = "1";
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	String strAkind = this.getBlankParameter(request, "akind", "0");
    	String strFriendLevel = this.getBlankParameter(request, "friendLevel", "");
    	
    	List<Account> accountList = new ArrayList<Account>();
    	
    	String strOrderCol = "realname";
    	if(!strAkind.equals("0")) {
    		strWhere += " and akind=" + strAkind;
    		
    		if(strAkind.equals(String.valueOf(AccountDAO.ACCOUNT_TYPE_PERSONAL))) {
    			strOrderCol = "realname";
        	}
    		else if(strAkind.equals(String.valueOf(AccountDAO.ACCOUNT_TYPE_ENTERPRISE))) {
    			strOrderCol = "enter_name";
    		}
    	}
    	if(strFriendLevel.equals("1") || strFriendLevel.equals("2") || strFriendLevel.equals("3")) {
    		String strFriends = "";
    		
    		if(strFriendLevel.equals("1")) {
    			strFriends = loginAccount.getFriend1();	
    		}
    		else if(strFriendLevel.equals("2")) {
    			strFriends = loginAccount.getFriend2();
    		}
    		else if(strFriendLevel.equals("3")) {
    			strFriends = loginAccount.getFriend3();
    		}
    		
    		if(!strFriends.isEmpty()) {
    			strWhere += " and id in (" + strFriends + ")";	
    		}
    		else {
    			result.put("retCode", 200);
    	    	result.put("data", accountList);
    	    	
    	    	return JSONResult(request, result);
    		}
    	}
    	else if(strFriendLevel.equals("0")) {
    		accountList = accountDao.search(null, "id=" + loginAccount.getReqCodeSenderId());
    		
    		CommonUtil.setInterestStatus(interestDao, loginAccount.getId(), accountList);
    		
    		result.put("retCode", 200);
        	result.put("data", accountList);
        	
        	return JSONResult(request, result);
    	}
    	
    	String strOrderDir = "asc";
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	strWhere += " and owner=" + loginAccount.getId();
    	List<Interest> myInterests = interestDao.search(null, strWhere);
    	
    	String strMyInterestIds = "";
    	for(Interest item : myInterests) {
    		if(!strMyInterestIds.isEmpty()) {
    			strMyInterestIds += ",";
			}
    		strMyInterestIds += item.getTarget();
    	}
    	
    	if(!strMyInterestIds.isEmpty()) {
    		accountList = accountDao.search(filter, "id in(" + strMyInterestIds + ")", strOrderCol + " " + strOrderDir);	
    	}
    	
    	CommonUtil.setInterestStatus(interestDao, loginAccount.getId(), accountList);
    	
    	result.put("retCode", 200);
    	result.put("data", accountList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getAccountDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strAccountId = this.getBlankParameter(request, "accountId", String.valueOf(loginAccount.getId()));
    	
    	Account account = accountDao.getDetail(Integer.valueOf(strAccountId));
    	
    	if(account == null) {
    		result.put("retCode", 201);
        	result.put("msg", "That account doesn't exist");
        	
        	return JSONResult(request, result);
    	}
    	
    	List<Estimate> estimates = estimateDao.search(null, "type=" + EstimateDAO.ESTIMATE_TYPE_PERSONAL_OR_ENTER + " and (upper_id is null or upper_id=0) and account_id=" + strAccountId, "elect_cnt desc, write_time desc");
    	List<Product> products = productDao.search(null, "account_id=" + account.getId(), "account_view_cnt desc");
    	List<Item> items = itemDao.search(null, "account_id=" + account.getId(), "account_view_cnt desc");
    	List<Service> services = serviceDao.search(null, "account_id=" + account.getId(), "account_view_cnt desc");
    	
    	for(Estimate item : estimates) {
    		List<Estimate> replys = estimateDao.search(null, "upper_id=" + item.getId(), "write_time desc");
    		item.setReplys(replys);
    	}
    	
    	CommonUtil.setProductFavouriteStatus(favouriteDao, loginAccount.getId(), products);
    	CommonUtil.setEstimateElectStatus(electDao, loginAccount.getId(), estimates);
    	CommonUtil.setInterestStatus(interestDao, loginAccount.getId(), account);
		
    	account.setEstimates(estimates);
		account.setProducts(products);
		account.setItems(items);
		account.setServices(services);
    	
    	result.put("retCode", 200);
    	result.put("account", account);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView leaveEstimate(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	BinaryFormUtil formUtil = new BinaryFormUtil(
				CommonUtil.getRepositoryRealPath(request) + Constants.ESTIMATE_IMAGE_PATH,
				CommonUtil.getRepositoryRealPath(request) + Constants.UPLOAD_TEMP_PATH,
				this.getSetting(AppSettings.C_COMMON_FILE_PATH_SEP));

		formUtil.initForm(request, response, this.getSetting(AppSettings.C_DEFAULT_ENCODING));

		String strType = formUtil.getString("type", String.valueOf(EstimateDAO.ESTIMATE_TYPE_PERSONAL_OR_ENTER));
    	String strKind = formUtil.getString("kind", String.valueOf(EstimateDAO.ESTIMATE_KIND_FORWORD));
    	String strMethod = formUtil.getString("method", String.valueOf(EstimateDAO.ESTIMATE_METHOD_DETAIl));
    	String strReason = formUtil.getString("reason", "");
    	String strContent = formUtil.getString("content", "");
    	String strAccountId = formUtil.getString("accountId", "");
    	String strHotId = formUtil.getString("hotId", "");
    	
    	if(strType.isEmpty()) {
    		result.put("retCode", 208);
    		result.put("msg", "type is empty");
    		
    		return JSONResult(request, result);
    	}
    	if(!strType.equals(String.valueOf(EstimateDAO.ESTIMATE_TYPE_PERSONAL_OR_ENTER)) && !strType.equals(String.valueOf(EstimateDAO.ESTIMATE_TYPE_HOT))) {
    		result.put("retCode", 209);
    		result.put("msg", "type is invalid");
    		
    		return JSONResult(request, result);
    	}
    	if(strKind.isEmpty()) {
    		result.put("retCode", 201);
    		result.put("msg", "请选择评价类型");
    		
    		return JSONResult(request, result);
    	}
    	if(!strKind.equals(String.valueOf(EstimateDAO.ESTIMATE_KIND_FORWORD)) && !strKind.equals(String.valueOf(EstimateDAO.ESTIMATE_KIND_BACKWORD))) {
    		result.put("retCode", 206);
    		result.put("msg", "kind is invalid");
    		
    		return JSONResult(request, result);
    	}
    	if(strMethod.isEmpty()) {
    		result.put("retCode", 202);
    		result.put("msg", "请选择评价方式");
    		
    		return JSONResult(request, result);
    	}
    	if(!strMethod.equals(String.valueOf(EstimateDAO.ESTIMATE_METHOD_DETAIl)) && !strMethod.equals(String.valueOf(EstimateDAO.ESTIMATE_METHOD_QUICK))) {
    		result.put("retCode", 207);
    		result.put("msg", "method is invalid");
    		
    		return JSONResult(request, result);
    	}
    	if(strReason.isEmpty()) {
    		result.put("retCode", 203);
    		result.put("msg", "请输入评价原因");
    		
    		return JSONResult(request, result);
    	}
    	if(strContent.isEmpty()) {
    		result.put("retCode", 204);
    		result.put("msg", "请输入评价内容");
    		
    		return JSONResult(request, result);
    	}
    	if(strType.equals(String.valueOf(EstimateDAO.ESTIMATE_TYPE_PERSONAL_OR_ENTER))) {
    		if(strAccountId.isEmpty()) {
        		result.put("retCode", 205);
        		result.put("msg", "请选择评价人");
        		
        		return JSONResult(request, result);
        	}	
    	}
    	else if(strType.equals(String.valueOf(EstimateDAO.ESTIMATE_TYPE_HOT))) {
    		if(strHotId.isEmpty()) {
        		result.put("retCode", 210);
        		result.put("msg", "请选择热点");
        		
        		return JSONResult(request, result);
        	}
    	}
    	

    	Estimate estimate = new Estimate();
    	
    	estimate.setType(Integer.valueOf(strType));
    	if(strType.equals(String.valueOf(EstimateDAO.ESTIMATE_TYPE_PERSONAL_OR_ENTER))) {
    		estimate.setAccountId(Integer.valueOf(strAccountId));	
    	}
    	else if(strType.equals(String.valueOf(EstimateDAO.ESTIMATE_TYPE_HOT))) {
    		estimate.setHotId(Integer.valueOf(strHotId));
    	}
    	estimate.setKind(Integer.valueOf(strKind));
    	estimate.setMethod(Integer.valueOf(strMethod));
    	estimate.setReason(strReason);
    	estimate.setContent(strContent);
    	estimate.setOwner(loginAccount.getId());
    	
    	List<File> imgFileList = formUtil.getFileList();
		
    	int i = 0;
		for(File imgFile : imgFileList) {

			String strFileName = Constants.ESTIMATE_IMAGE_URL + "/" + imgFile.getPhysicalPath() + "/" + imgFile.getPhysicalName();
			
			if(i == 0) {
				estimate.setImgPath1(strFileName);
			} else if(i == 1) {
				estimate.setImgPath2(strFileName);
			} else if(i == 2) {
				estimate.setImgPath3(strFileName);
			} else if(i == 3) {
				estimate.setImgPath4(strFileName);
			} else if(i == 4) {
				estimate.setImgPath5(strFileName);
			}
			i ++;
		}
		
    	estimateDao.insert(estimate);
    	
    	if(strType.equals(String.valueOf(EstimateDAO.ESTIMATE_TYPE_PERSONAL_OR_ENTER))) {
    		if(strKind.equals(String.valueOf(EstimateDAO.ESTIMATE_KIND_FORWORD))) {
    			
    			CommonUtil.insertMarkLog(markLogDao, Integer.valueOf(strAccountId), MarkLogDAO.LOG_KIND_ESTIMATE_RECEIVE, 
    					Integer.valueOf(strKind), Integer.valueOf(strMethod), null,
						MarkLogDAO.PMARK_ESTIMATE_P_RECEIVE, null, loginAccount.getId());
    			
    			if(strMethod.equals(String.valueOf(EstimateDAO.ESTIMATE_METHOD_DETAIl))) {
    				
    				CommonUtil.insertMarkLog(markLogDao, loginAccount.getId(), MarkLogDAO.LOG_KIND_ESTIMATE_GIVE,
    						Integer.valueOf(strKind), Integer.valueOf(strMethod), null,
    						MarkLogDAO.PMARK_ESTIMATE_P_GIVE_DETAIL, null, Integer.valueOf(strAccountId));
    			}
    			else if(strMethod.equals(String.valueOf(EstimateDAO.ESTIMATE_METHOD_QUICK))) {
    				
    				CommonUtil.insertMarkLog(markLogDao, loginAccount.getId(), MarkLogDAO.LOG_KIND_ESTIMATE_GIVE, 
    						Integer.valueOf(strKind), Integer.valueOf(strMethod), null,
    						MarkLogDAO.PMARK_ESTIMATE_P_GIVE_QUICK, null, Integer.valueOf(strAccountId));
    			}
    		}
    		else if(strKind.equals(String.valueOf(EstimateDAO.ESTIMATE_KIND_BACKWORD))) {
    			
    			CommonUtil.insertMarkLog(markLogDao, Integer.valueOf(strAccountId), MarkLogDAO.LOG_KIND_ESTIMATE_RECEIVE, 
    					Integer.valueOf(strKind), Integer.valueOf(strMethod), null,
						null, MarkLogDAO.NMARK_ESTIMATE_N_RECEIVE, loginAccount.getId());

    			if(strMethod.equals(String.valueOf(EstimateDAO.ESTIMATE_METHOD_DETAIl))) {
    			
    				CommonUtil.insertMarkLog(markLogDao, loginAccount.getId(), MarkLogDAO.LOG_KIND_ESTIMATE_GIVE,
    						Integer.valueOf(strKind), Integer.valueOf(strMethod), null,
    						MarkLogDAO.PMARK_ESTIMATE_N_GIVE, null, Integer.valueOf(strAccountId));	
    			}
    		}
    		
    		CommonUtil.insertNotice(noticeDao, NoticeDAO.NOTICE_TYPE_USER, Integer.valueOf(strAccountId), NoticeDAO.NOTICE_KIND_ESTIMATE
    				, Integer.valueOf(strKind)
    				, CommonUtil.getNoticeMsgTitle(NoticeDAO.NOTICE_KIND_ESTIMATE)
    				, CommonUtil.getNoticeMsgContent(NoticeDAO.NOTICE_KIND_ESTIMATE, Integer.valueOf(strKind)
    						, (strKind.equals(String.valueOf(EstimateDAO.ESTIMATE_KIND_FORWORD)) ? MarkLogDAO.PMARK_ESTIMATE_P_RECEIVE : MarkLogDAO.NMARK_ESTIMATE_N_RECEIVE)
    						, null
    				)
    				, null, estimate.getId(), null);
    	}
    	
    	
    	if(strType.equals(String.valueOf(EstimateDAO.ESTIMATE_TYPE_PERSONAL_OR_ENTER))) {
    		Account targetAccount = accountDao.getDetail(Integer.valueOf(strAccountId));
    		
    		CommonUtil.insertNotice(noticeDao, NoticeDAO.NOTICE_TYPE_ADMIN, null, NoticeDAO.NOTICE_KIND_ESTIMATE, null, null
        			, (loginAccount.getAkind() == AccountDAO.ACCOUNT_TYPE_PERSONAL ? loginAccount.getRealname() : loginAccount.getEnterName()) 
        			+ "对" + (targetAccount.getAkind() == AccountDAO.ACCOUNT_TYPE_PERSONAL ? targetAccount.getRealname() : targetAccount.getEnterName()) + "做出了评价"
        			, null, estimate.getId(), null);	
    	}
    	
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView leaveReply(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strEstimateId = this.getBlankParameter(request, "estimateId", "");
    	String strContent = HtmlUtils.htmlUnescape(this.getBlankParameter(request, "content", ""));
    	
    	if(strEstimateId.isEmpty()) {
    		result.put("retCode", 201);
    		result.put("msg", "estimateId is empty");
    		
    		return JSONResult(request, result);
    	}
    	if(strContent.isEmpty()) {
    		result.put("retCode", 203);
    		result.put("msg", "请输入回复内容");
    		
    		return JSONResult(request, result);
    	}
    	
    	Estimate upperEstimate = estimateDao.get(Integer.valueOf(strEstimateId));
    	
    	if(upperEstimate == null) {
    		result.put("retCode", 202);
    		result.put("msg", "That estimate doesn't exist");
    		
    		return JSONResult(request, result);
    	}
    	
    	Estimate estimate = new Estimate();

    	estimate.setType(upperEstimate.getType());
    	estimate.setUpperId(upperEstimate.getId());
    	estimate.setContent(strContent);
    	estimate.setOwner(loginAccount.getId());

    	estimateDao.insert(estimate);
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView addProduct(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	if(loginAccount.getAkind() == AccountDAO.ACCOUNT_TYPE_PERSONAL) {
    		result.put("retCode", 207);
    		result.put("msg", "Personal user can't add product");
    		
    		return JSONResult(request, result);
    	}
    	BinaryFormUtil formUtil = new BinaryFormUtil(
				CommonUtil.getRepositoryRealPath(request) + Constants.PRODUCT_IMAGE_PATH,
				CommonUtil.getRepositoryRealPath(request) + Constants.UPLOAD_TEMP_PATH,
				this.getSetting(AppSettings.C_COMMON_FILE_PATH_SEP));

		formUtil.initForm(request, response, this.getSetting(AppSettings.C_DEFAULT_ENCODING));

		String strProductId = formUtil.getString("productId", "");
		String strName = formUtil.getString("name", "");
    	String strIsMain = formUtil.getString("isMain", "0");
    	String strPrice = formUtil.getString("price", "");
    	String strPleixingId = formUtil.getString("pleixingId", "");
    	String strComment = formUtil.getString("comment", "");
    	String strWeburl = formUtil.getString("weburl", "");
    	String strSaleAddr = formUtil.getString("saleAddr", "");
    	
    	if(strName.isEmpty()) {
    		result.put("retCode", 201);
    		result.put("msg", "请输入产品名称");
    		
    		return JSONResult(request, result);
    	}
    	if(strPrice.isEmpty()) {
    		result.put("retCode", 202);
    		result.put("msg", "请输入产品价格");
    		
    		return JSONResult(request, result);
    	}
    	if(strPleixingId.isEmpty()) {
    		result.put("retCode", 203);
    		result.put("msg", "请选择产品分类");
    		
    		return JSONResult(request, result);
    	}
    	if(strComment.isEmpty()) {
    		result.put("retCode", 204);
    		result.put("msg", "请输入产品介绍");
    		
    		return JSONResult(request, result);
    	}
    	
    	List<File> imgFileList = formUtil.getFileList();
    	
    	if(imgFileList.size() == 0) {
    		result.put("retCode", 205);
    		result.put("msg", "请选择产品图片");
    		
    		return JSONResult(request, result);
    	}
    	
    	Product product;
    	
    	if(strProductId.isEmpty()) {
    		product = new Product();
    		product.setAccountId(loginAccount.getId());
    	}
    	else {
    		product = productDao.get(Integer.valueOf(strProductId));
    		
    		if(product == null) {
    			result.put("retCode", 206);
        		result.put("msg", "That product doesn't exist");
        		
        		return JSONResult(request, result);
    		}
    	}
    	
    	product.setName(strName);
    	product.setCode(CommonUtil.genProductCode(productDao));
    	product.setIsMain(Integer.valueOf(strIsMain));
    	product.setPrice(Double.valueOf(strPrice));
    	product.setPleixingId(Integer.valueOf(strPleixingId));
    	product.setComment(strComment);
    	product.setWeburl(strWeburl);
    	product.setSaleAddr(strSaleAddr);
		
    	int i = 0;
		for(File imgFile : imgFileList) {

			String strFileName = Constants.PRODUCT_IMAGE_URL + "/" + imgFile.getPhysicalPath() + "/" + imgFile.getPhysicalName();
			
			if(i == 0) {
				product.setImgPath1(strFileName);
			} else if(i == 1) {
				product.setImgPath2(strFileName);
			} else if(i == 2) {
				product.setImgPath3(strFileName);
			} else if(i == 3) {
				product.setImgPath4(strFileName);
			} else if(i == 4) {
				product.setImgPath5(strFileName);
			}
			i ++;
		}
		
		if(strProductId.isEmpty()) {
			productDao.insert(product);	
		}
		else {
			productDao.update(product);
		}
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView addItem(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	BinaryFormUtil formUtil = new BinaryFormUtil(
				CommonUtil.getRepositoryRealPath(request) + Constants.ITEM_IMAGE_PATH,
				CommonUtil.getRepositoryRealPath(request) + Constants.UPLOAD_TEMP_PATH,
				this.getSetting(AppSettings.C_COMMON_FILE_PATH_SEP));

		formUtil.initForm(request, response, this.getSetting(AppSettings.C_DEFAULT_ENCODING));

		String strItemId = formUtil.getString("itemId", "");
		String strName = formUtil.getString("name", "");
    	String strFenleiId = formUtil.getString("fenleiId", "");
    	String strCityId = formUtil.getString("cityId", "");
    	String strAddr = formUtil.getString("addr", "");
    	String strComment = formUtil.getString("comment", "");
    	String strNeed = formUtil.getString("need", "");
    	String strWeburl = formUtil.getString("weburl", "");
    	String strIsShow = formUtil.getString("isShow", "0");
    	String strContactName = formUtil.getString("contactName", "");
    	String strContactMobile = formUtil.getString("contactMobile", "");
    	String strContactWeixin = formUtil.getString("contactWeixin", "");
    	
    	if(strName.isEmpty()) {
    		result.put("retCode", 201);
    		result.put("msg", "请输入项目名称");
    		
    		return JSONResult(request, result);
    	}
    	if(strFenleiId.isEmpty()) {
    		result.put("retCode", 202);
    		result.put("msg", "请选择项目分类");
    		
    		return JSONResult(request, result);
    	}
    	if(strCityId.isEmpty()) {
    		result.put("retCode", 203);
    		result.put("msg", "请选择所在城市");
    		
    		return JSONResult(request, result);
    	}
    	if(strAddr.isEmpty()) {
    		result.put("retCode", 211);
    		result.put("msg", "请输入详细地址");
    		
    		return JSONResult(request, result);
    	}
    	if(strComment.isEmpty()) {
    		result.put("retCode", 204);
    		result.put("msg", "请输入项目介绍");
    		
    		return JSONResult(request, result);
    	}
    	if(strNeed.isEmpty()) {
    		result.put("retCode", 205);
    		result.put("msg", "请输入所需资源");
    		
    		return JSONResult(request, result);
    	}
    	if(!strIsShow.equals("0") && !strIsShow.equals("1")) {
    		result.put("retCode", 210);
    		result.put("msg", "isShow is invalid");
    		
    		return JSONResult(request, result);
    	}
    	if(strContactName.isEmpty()) {
    		result.put("retCode", 206);
    		result.put("msg", "请输入联系人姓名");
    		
    		return JSONResult(request, result);
    	}
    	if(strContactMobile.isEmpty()) {
    		result.put("retCode", 207);
    		result.put("msg", "请输入联系人手机号");
    		
    		return JSONResult(request, result);
    	}
    	if(strContactWeixin.isEmpty()) {
    		result.put("retCode", 208);
    		result.put("msg", "请输入联系人微信号");
    		
    		return JSONResult(request, result);
    	}
    	
    	File imgFileLogo = formUtil.getFile("logo");
		
		if(imgFileLogo == null) {
			result.put("retCode", 209);
    		result.put("msg", "LOGO不能为空");
    		
    		return JSONResult(request, result);
		}
		
		String strImageFileLogo = Constants.ITEM_IMAGE_URL + "/" + imgFileLogo.getPhysicalPath() + "/" + imgFileLogo.getPhysicalName();
    	
		Item item;
		
		if(strItemId.isEmpty()) {
			item = new Item();
			item.setAccountId(loginAccount.getId());
    	}
    	else {
    		item = itemDao.get(Integer.valueOf(strItemId));
    		
    		if(item == null) {
    			result.put("retCode", 210);
        		result.put("msg", "That item doesn't exist");
        		
        		return JSONResult(request, result);
    		}
    	}
    	
    	item.setLogo(strImageFileLogo);
    	item.setName(strName);
    	item.setCode(CommonUtil.genItemCode(itemDao));
    	item.setFenleiId(Integer.valueOf(strFenleiId));
    	item.setCityId(Integer.valueOf(strCityId));
    	item.setAddr(strAddr);
    	item.setComment(strComment);
    	item.setNeed(strNeed);
    	item.setWeburl(strWeburl);
    	item.setIsShow(Integer.valueOf(strIsShow));
    	item.setContactName(strContactName);
    	item.setContactMobile(strContactMobile);
    	item.setContactWeixin(strContactWeixin);
		
    	if(strItemId.isEmpty()) {
    		itemDao.insert(item);
    	}
    	else {
    		itemDao.update(item);
    	}
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView addService(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	BinaryFormUtil formUtil = new BinaryFormUtil(
				CommonUtil.getRepositoryRealPath(request) + Constants.SERVICE_IMAGE_PATH,
				CommonUtil.getRepositoryRealPath(request) + Constants.UPLOAD_TEMP_PATH,
				this.getSetting(AppSettings.C_COMMON_FILE_PATH_SEP));

		formUtil.initForm(request, response, this.getSetting(AppSettings.C_DEFAULT_ENCODING));

		String strServiceId = formUtil.getString("serviceId", "");
		String strName = formUtil.getString("name", "");
    	String strFenleiId = formUtil.getString("fenleiId", "");
    	String strComment = formUtil.getString("comment", "");
    	String strAddr = formUtil.getString("addr", "");
    	String strWeburl = formUtil.getString("weburl", "");
    	String strIsShow = formUtil.getString("isShow", "0");
    	String strContactName = formUtil.getString("contactName", "");
    	String strContactMobile = formUtil.getString("contactMobile", "");
    	String strContactWeixin = formUtil.getString("contactWeixin", "");
    	
    	if(strName.isEmpty()) {
    		result.put("retCode", 201);
    		result.put("msg", "请输入服务名称");
    		
    		return JSONResult(request, result);
    	}
    	if(strFenleiId.isEmpty()) {
    		result.put("retCode", 202);
    		result.put("msg", "请选择服务分类");
    		
    		return JSONResult(request, result);
    	}
    	if(strComment.isEmpty()) {
    		result.put("retCode", 204);
    		result.put("msg", "请输入服务介绍");
    		
    		return JSONResult(request, result);
    	}
    	if(!strIsShow.equals("0") && !strIsShow.equals("1")) {
    		result.put("retCode", 210);
    		result.put("msg", "isShow is invalid");
    		
    		return JSONResult(request, result);
    	}
    	if(strContactName.isEmpty()) {
    		result.put("retCode", 206);
    		result.put("msg", "请输入联系人姓名");
    		
    		return JSONResult(request, result);
    	}
    	if(strContactMobile.isEmpty()) {
    		result.put("retCode", 207);
    		result.put("msg", "请输入联系人手机号");
    		
    		return JSONResult(request, result);
    	}
    	if(strContactWeixin.isEmpty()) {
    		result.put("retCode", 208);
    		result.put("msg", "请输入联系人微信号");
    		
    		return JSONResult(request, result);
    	}
    	
    	File imgFileLogo = formUtil.getFile("logo");
		
		if(imgFileLogo == null) {
			result.put("retCode", 209);
    		result.put("msg", "LOGO不能为空");
    		
    		return JSONResult(request, result);
		}
		
		String strImageFileLogo = Constants.SERVICE_IMAGE_URL + "/" + imgFileLogo.getPhysicalPath() + "/" + imgFileLogo.getPhysicalName();
    	
    	Service service;
    	
    	if(strServiceId.isEmpty()) {
    		service = new Service();
    	}
    	else {
    		service = serviceDao.get(Integer.valueOf(strServiceId));
    		
    		if(service == null) {
    			result.put("retCode", 210);
        		result.put("msg", "That item doesn't exist");
        		
        		return JSONResult(request, result);
    		}
    	}
    	
    	service.setLogo(strImageFileLogo);
    	service.setName(strName);
    	service.setCode(CommonUtil.genServiceCode(serviceDao));
    	service.setFenleiId(Integer.valueOf(strFenleiId));
    	service.setComment(strComment);
    	service.setAddr(strAddr);
    	service.setWeburl(strWeburl);
    	service.setIsShow(Integer.valueOf(strIsShow));
    	service.setContactName(strContactName);
    	service.setContactMobile(strContactMobile);
    	service.setContactWeixin(strContactWeixin);
		
    	if(strServiceId.isEmpty()) {
    		serviceDao.insert(service);
    	}
    	else {
    		serviceDao.update(service);
    	}
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView incViewCount(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strKind = this.getBlankParameter(request, "kind", "");
    	String strAccountId = this.getBlankParameter(request, "accountId", "");
    	String strHotId = this.getBlankParameter(request, "hotId", "");
    	
    	if(strKind.isEmpty()) {
    		result.put("retCode", 201);
    		result.put("msg", "kind is empty");
    		
    		return JSONResult(request, result);
    	}
    	
    	if(strKind.equals(String.valueOf(Constants.VIEW_CNT_KIND_PERSONAL_OR_ENTER))) {
    		if(strAccountId.isEmpty()) {
    			result.put("retCode", 203);
        		result.put("msg", "accountId is empty");
        		
        		return JSONResult(request, result);
    		}
    		
    		Account account = accountDao.get(Integer.valueOf(strAccountId));
    		
    		if(account == null) {
    			result.put("retCode", 205);
        		result.put("msg", "That account doesn't exist");
        		
        		return JSONResult(request, result);
    		}
    		
    		account.setViewCnt(account.getViewCnt() + 1);
    		
    		accountDao.update(account);
    	}
    	else if(strKind.equals(String.valueOf(Constants.VIEW_CNT_KIND_HOT))) {
    		if(strHotId.isEmpty()) {
    			result.put("retCode", 204);
        		result.put("msg", "hotId is empty");
        		
        		return JSONResult(request, result);
    		}
    		
    		Hot hot = hotDao.get(Integer.valueOf(strHotId));
    		
    		if(hot == null) {
    			result.put("retCode", 206);
        		result.put("msg", "That hot doesn't exist");
        		
        		return JSONResult(request, result);
    		}
    		
    		hot.setVisitCnt(hot.getVisitCnt() + 1);
    		
    		hotDao.update(hot);
    	}
    	else {
    		result.put("retCode", 202);
    		result.put("msg", "kind is invalid");
    		
    		return JSONResult(request, result);
    	}
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getInviterInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	Account account = accountDao.getDetail(loginAccount.getReqCodeSenderId());
    	
    	result.put("retCode", 200);
    	result.put("inviterInfo", account);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getMarkLogList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	List<MarkLog> markLogList = markLogDao.search(null, "account_id=" + loginAccount.getId(), "write_time desc");
    	
    	result.put("retCode", 200);
    	result.put("data", markLogList);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getEstimateListForHot(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strHotId = this.getBlankParameter(request, "hotId", "");
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	
    	if(strHotId.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("msg", "hotId is empty");
        	
        	return JSONResult(request, result);
    	}
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	String strWhere = "type=" + EstimateDAO.ESTIMATE_TYPE_HOT + " and (upper_id is null or upper_id=0) and hot_id=" + strHotId;
    	List<Estimate> estimates = estimateDao.search(filter, strWhere, "elect_cnt desc, write_time desc");
    	int moreCnt = estimateDao.count(null, strWhere) - (Integer.valueOf(strStart) + estimates.size());
    	
    	for(Estimate item : estimates) {
    		List<Estimate> replys = estimateDao.search(null, "upper_id=" + item.getId(), "write_time desc");
    		item.setReplys(replys);
    	}
    	
    	CommonUtil.setEstimateElectStatus(electDao, loginAccount.getId(), estimates);
    	
    	result.put("retCode", 200);
    	result.put("data", estimates);
    	result.put("moreCnt", moreCnt);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getPartnerList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strHotId = this.getBlankParameter(request, "hotId", "");
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	
    	List<Product> products = null;
    	List<Item> items = null;
    	List<Service> services = null;
    	
    	if(strHotId.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("msg", "hotId is empty");
        	
        	return JSONResult(request, result);
    	}
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	Hot hot = hotDao.get(Integer.valueOf(strHotId));
    	
    	if(hot == null) {
    		result.put("retCode", 202);
        	result.put("msg", "That hot doesn't exist");
        	
        	return JSONResult(request, result);
    	}
    	
    	String strWhere = "xyleixing_id=" + hot.getXyleixingId() + " and akind=" + AccountDAO.ACCOUNT_TYPE_ENTERPRISE;
    	List<Account> accountList = accountDao.search(filter, strWhere, "credit desc, write_time desc");
    	
    	List<Integer> myInterests = CommonUtil.getInterestIds(interestDao, loginAccount.getId());
    	
    	for(Account item : accountList) {
    		if(myInterests.contains(item.getId())) {
    			item.setInterested(1);
    		}
    		
    		products = productDao.search(null, "is_main=1 and account_id=" + item.getId());
    		items = itemDao.search(null, "account_id=" + item.getId());
    		services = serviceDao.search(null, "account_id=" + item.getId());
    		
    		item.setProducts(products);
    		item.setItems(items);
    		item.setServices(services);
    	}
    	
    	int moreCnt = accountDao.count(null, strWhere) - (Integer.valueOf(strStart) + accountList.size());
    	
    	result.put("retCode", 200);
    	result.put("data", accountList);
    	result.put("moreCnt", moreCnt);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getMyFavouriteList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strKind = this.getBlankParameter(request, "kind", "");
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	
    	if(!strKind.equals(String.valueOf(FavouriteDAO.FAVOURITE_KIND_PRODUCT)) && !strKind.equals(String.valueOf(FavouriteDAO.FAVOURITE_KIND_HOT))) {
    		result.put("retCode", 201);
        	result.put("msg", "kind is invalid");
        	
        	return JSONResult(request, result);
    	}
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	String strWhere = "kind=" + strKind;
    	List<Favourite> favouriteList = favouriteDao.search(filter, strWhere, "write_time desc");
    	int moreCnt = favouriteDao.count(null, strWhere) - (Integer.valueOf(strStart) + favouriteList.size());
    	
    	result.put("retCode", 200);
    	result.put("data", favouriteList);
    	result.put("moreCnt", moreCnt);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getProductDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strProductId = this.getBlankParameter(request, "productId", "");
    	
    	if(strProductId.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("msg", "productId is empty");
        	
        	return JSONResult(request, result);
    	}
    	
    	Product product = productDao.getDetail(Integer.valueOf(strProductId));
    	
    	if(product == null) {
    		result.put("retCode", 202);
        	result.put("msg", "That product doesn't exist");
        	
        	return JSONResult(request, result);
    	}
    	
    	CommonUtil.setProductFavouriteStatus(favouriteDao, loginAccount.getId(), product);
    	
    	result.put("retCode", 200);
    	result.put("product", product);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getHotDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strHotId = this.getBlankParameter(request, "hotId", "");
    	
    	if(strHotId.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("msg", "hotId is empty");
        	
        	return JSONResult(request, result);
    	}
    	
    	Hot hot = hotDao.getDetail(Integer.valueOf(strHotId));
    	
    	if(hot == null) {
    		result.put("retCode", 202);
        	result.put("msg", "That hot doesn't exist");
        	
        	return JSONResult(request, result);
    	}
    	
    	result.put("retCode", 200);
    	result.put("hot", hot);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView makeCorrect(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	BinaryFormUtil formUtil = new BinaryFormUtil(
				CommonUtil.getRepositoryRealPath(request) + Constants.ESTIMATE_IMAGE_PATH,
				CommonUtil.getRepositoryRealPath(request) + Constants.UPLOAD_TEMP_PATH,
				this.getSetting(AppSettings.C_COMMON_FILE_PATH_SEP));

		formUtil.initForm(request, response, this.getSetting(AppSettings.C_DEFAULT_ENCODING));

		String strEstimateId = formUtil.getString("estimateId", "");
		String strKind = formUtil.getString("kind", "");
    	String strReason = formUtil.getString("reason", "");
    	String strWhyis = formUtil.getString("whyis", "");
    	
    	if(strEstimateId.isEmpty()) {
    		result.put("retCode", 201);
    		result.put("msg", "estimateId is empty");
    		
    		return JSONResult(request, result);
    	}
    	if(!strKind.equals(String.valueOf(ErrorDAO.ERROR_KIND_OVER)) && !strKind.equals(String.valueOf(ErrorDAO.ERROR_KIND_FALSE))) {
    		result.put("retCode", 202);
    		result.put("msg", "kind is invalid");
    		
    		return JSONResult(request, result);
    	}
    	if(strReason.isEmpty()) {
    		result.put("retCode", 203);
    		result.put("msg", "请输入纠错原因");
    		
    		return JSONResult(request, result);
    	}
    	if(strWhyis.isEmpty()) {
    		result.put("retCode", 204);
    		result.put("msg", "请输入纠错依据");
    		
    		return JSONResult(request, result);
    	}
    	
    	Errors error = new Errors();
    	
    	error.setEstimateId(Integer.valueOf(strEstimateId));
    	error.setOwnerId(loginAccount.getId());
    	error.setKind(Integer.valueOf(strKind));
    	error.setReason(strReason);
    	error.setWhyis(strWhyis);
    	error.setStatus(ErrorDAO.ERROR_ST_PROCESSING);
    	
    	List<File> imgFileList = formUtil.getFileList();
		
    	int i = 0;
		for(File imgFile : imgFileList) {

			String strFileName = Constants.ESTIMATE_IMAGE_URL + "/" + imgFile.getPhysicalPath() + "/" + imgFile.getPhysicalName();
			
			if(i == 0) {
				error.setImgPath1(strFileName);
			} else if(i == 1) {
				error.setImgPath2(strFileName);
			} else if(i == 2) {
				error.setImgPath3(strFileName);
			} else if(i == 3) {
				error.setImgPath4(strFileName);
			} else if(i == 4) {
				error.setImgPath5(strFileName);
			} else if(i == 5) {
				error.setImgPath5(strFileName);
			} else if(i == 6) {
				error.setImgPath5(strFileName);
			}
			i ++;
		}
		
    	errorDao.insert(error);
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getErrorList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strStatus = this.getBlankParameter(request, "status", "0");
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	
    	if(!strStatus.equals("0") 
    			&& !strStatus.equals(String.valueOf(ErrorDAO.ERROR_ST_PROCESSING)) 
    			&& !strStatus.equals(String.valueOf(ErrorDAO.ERROR_ST_PASS))
    			&& !strStatus.equals(String.valueOf(ErrorDAO.ERROR_ST_NOPASS))) {
    		result.put("retCode", 201);
        	result.put("msg", "status is invalid");
        	
        	return JSONResult(request, result);
    	}
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	String strWhere = "1";
    	if(!strStatus.equals("0")) {
    		strWhere += " and status=" + strStatus;	
    	}
    	
    	List<Errors> errorList = errorDao.search(filter, strWhere, "write_time desc");
    	int moreCnt = errorDao.count(null, strWhere) - (Integer.valueOf(strStart) + errorList.size());
    	
    	result.put("retCode", 200);
    	result.put("data", errorList);
    	result.put("moreCnt", moreCnt);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getMyProductList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	String strKeyword = this.getBlankParameter(request, "keyword", "");
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	String strWhere = "account_id=" + loginAccount.getId();
    	if(!strKeyword.isEmpty()) {
    		strWhere += " and (code like '%" + strKeyword + "%' or name like '%" + strKeyword + "%' or pleixing_name like '%" + strKeyword 
    				+ "%' or comment like '%" + strKeyword + "%' or weburl like '%" + strKeyword + "%' or sale_addr like '%" + strKeyword + "%')";
    	}
		List<Product> productList = productDao.search(filter, strWhere, "write_time desc");
		int moreCnt = productDao.count(null, strWhere) - (Integer.valueOf(strStart) + productList.size());
    	
    	result.put("retCode", 200);
    	result.put("data", productList);
    	result.put("moreCnt", moreCnt);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getMyItemList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	String strKeyword = this.getBlankParameter(request, "keyword", "");
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	String strWhere = "account_id=" + loginAccount.getId();
    	if(!strKeyword.isEmpty()) {
    		strWhere += " and (code like '%" + strKeyword + "%' or name like '%" + strKeyword + "%' or fenlei_name like '%" + strKeyword 
    				+ "%' or comment like '%" + strKeyword + "%' or weburl like '%" + strKeyword + "%' or need like '%" + strKeyword + "%')";
    	}
		List<Item> itemList = itemDao.search(filter, strWhere, "write_time desc");
		int moreCnt = itemDao.count(null, strWhere) - (Integer.valueOf(strStart) + itemList.size());
    	
    	result.put("retCode", 200);
    	result.put("data", itemList);
    	result.put("moreCnt", moreCnt);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getMyServiceList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	String strKeyword = this.getBlankParameter(request, "keyword", "");
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	String strWhere = "account_id=" + loginAccount.getId();
    	if(!strKeyword.isEmpty()) {
    		strWhere += " and (code like '%" + strKeyword + "%' or name like '%" + strKeyword + "%' or fenlei_name like '%" + strKeyword 
    				+ "%' or comment like '%" + strKeyword + "%' or weburl like '%" + strKeyword + "%' or addr like '%" + strKeyword + "%')";
    	}
		List<Service> serviceList = serviceDao.search(filter, strWhere, "write_time desc");
		int moreCnt = serviceDao.count(null, strWhere) - (Integer.valueOf(strStart) + serviceList.size());
    	
    	result.put("retCode", 200);
    	result.put("data", serviceList);
    	result.put("moreCnt", moreCnt);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView updowndelProduct(HttpServletRequest request, HttpServletResponse response, HttpSession session, int type) throws Exception {
    	
    	String strProductId = this.getBlankParameter(request, "productId", "");
    	
    	if(strProductId.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("msg", "productId is empty");
        	
        	return JSONResult(request, result);
    	}
    	
    	Product product = productDao.get(Integer.valueOf(strProductId));
    	
    	if(product == null) {
    		result.put("retCode", 202);
        	result.put("msg", "That product doesn't exist");
        	
        	return JSONResult(request, result);
    	}
    	else if(product.getAccountId() != loginAccount.getId()) {
    		result.put("retCode", 203);
        	result.put("msg", "That product isn't yours");
        	
        	return JSONResult(request, result);
    	}
    	
    	if(type == HotDAO.HOTS_ST_UP) {
    		product.setStatus(HotDAO.HOTS_ST_UP);	

    		productDao.update(product);
    	}
    	else if(type == HotDAO.HOTS_ST_DOWN) {
    		product.setStatus(HotDAO.HOTS_ST_DOWN);
    		
    		productDao.update(product);
    	}
    	else if(type == Constants.OP_ID_DEL) {
    		
    		productDao.delete(Integer.valueOf(strProductId));
    	}
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView upProduct(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	return updowndelProduct(request, response, session, HotDAO.HOTS_ST_UP);
    }
    
    private ModelAndView downProduct(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	return updowndelProduct(request, response, session, HotDAO.HOTS_ST_DOWN);
    }

    private ModelAndView deleteProduct(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	return updowndelProduct(request, response, session, Constants.OP_ID_DEL);
    }
    
    private ModelAndView updowndelItem(HttpServletRequest request, HttpServletResponse response, HttpSession session, int type) throws Exception {
    	
    	String strItemId = this.getBlankParameter(request, "itemId", "");
    	
    	if(strItemId.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("msg", "itemId is empty");
        	
        	return JSONResult(request, result);
    	}
    	
    	Item item = itemDao.get(Integer.valueOf(strItemId));
    	
    	if(item == null) {
    		result.put("retCode", 202);
        	result.put("msg", "That item doesn't exist");
        	
        	return JSONResult(request, result);
    	}
    	else if(item.getAccountId() != loginAccount.getId()) {
    		result.put("retCode", 203);
        	result.put("msg", "That item isn't yours");
        	
        	return JSONResult(request, result);
    	}
    	
    	if(type == HotDAO.HOTS_ST_UP) {
    		item.setStatus(HotDAO.HOTS_ST_UP);	
    		
    		itemDao.update(item);
    	}
    	else if(type == HotDAO.HOTS_ST_DOWN) {
    		item.setStatus(HotDAO.HOTS_ST_DOWN);
    		
    		itemDao.update(item);
    	}
    	else if(type == Constants.OP_ID_DEL) {
    		itemDao.delete(Integer.valueOf(strItemId));
    	}
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView upItem(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	return updowndelItem(request, response, session, HotDAO.HOTS_ST_UP);
    }
    
    private ModelAndView downItem(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	return updowndelItem(request, response, session, HotDAO.HOTS_ST_DOWN);
    }

    private ModelAndView deleteItem(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	return updowndelItem(request, response, session, Constants.OP_ID_DEL);
    }
    
    private ModelAndView updowndelService(HttpServletRequest request, HttpServletResponse response, HttpSession session, int type) throws Exception {
    	
    	String strServiceId = this.getBlankParameter(request, "serviceId", "");
    	
    	if(strServiceId.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("msg", "serviceId is empty");
        	
        	return JSONResult(request, result);
    	}
    	
    	Service service = serviceDao.get(Integer.valueOf(strServiceId));
    	
    	if(service == null) {
    		result.put("retCode", 202);
        	result.put("msg", "That service doesn't exist");
        	
        	return JSONResult(request, result);
    	}
    	else if(service.getAccountId() != loginAccount.getId()) {
    		result.put("retCode", 203);
        	result.put("msg", "That service isn't yours");
        	
        	return JSONResult(request, result);
    	}
    	
    	if(type == HotDAO.HOTS_ST_UP) {
    		service.setStatus(HotDAO.HOTS_ST_UP);
    		
    		serviceDao.update(service);
    	}
    	else if(type == HotDAO.HOTS_ST_DOWN) {
    		service.setStatus(HotDAO.HOTS_ST_DOWN);
    		
    		serviceDao.update(service);
    	}
    	else if(type == Constants.OP_ID_DEL) {
    		
    		serviceDao.delete(Integer.valueOf(strServiceId));
    	}
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView upService(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	return updowndelService(request, response, session, HotDAO.HOTS_ST_UP);
    }
    
    private ModelAndView downService(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	return updowndelService(request, response, session, HotDAO.HOTS_ST_DOWN);
    }

    private ModelAndView deleteService(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	return updowndelService(request, response, session, Constants.OP_ID_DEL);
    }
    
    private ModelAndView leaveOpinion(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strContent = this.getBlankParameter(request, "content", "");
    	
    	if(strContent.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("msg", "请输入内容");
        	
        	return JSONResult(request, result);
    	}
    	
    	Opinion opinion = new Opinion();
    	
    	opinion.setAccountId(loginAccount.getId());
    	opinion.setContent(strContent);
    	
    	opinionDao.insert(opinion);
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView inviteFriend(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	ReqCode reqCode = new ReqCode();
    	
    	String strReqCode = "";
    	boolean find = false;
    	
    	while(!find) {
    		strReqCode = CommonUtil.genReqCode();
    		
    		if(0 == reqCodeDao.count(null, "req_code=" + strReqCode)) {
    			find = true;
    		}
    	}
    	
    	reqCode.setReqCode(strReqCode);
    	reqCode.setSender(loginAccount.getId());
    	reqCode.setStatus(ReqCodeDAO.STATUS_NOT_USED);
    	
    	reqCodeDao.insert(reqCode);
    	
    	result.put("retCode", 200);
    	result.put("reqCode", strReqCode);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView onPurchase(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strProductId = this.getBlankParameter(request, "productId", "");
    	
    	if(strProductId.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("msg", "productId is empty");
        	
        	return JSONResult(request, result);
    	}
    	
    	ClickHistory clickHistory = new ClickHistory();
    	
    	clickHistory.setOwnerId(loginAccount.getId());
    	clickHistory.setType(ClickHistoryDAO.HISTORY_TYPE_BUY);
    	clickHistory.setProductId(Integer.valueOf(strProductId));
    	
    	clickHistoryDao.insert(clickHistory);
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView elect(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strEstimateId = this.getBlankParameter(request, "estimateId", "");
    	String strVal = this.getBlankParameter(request, "val", "");
    	
    	if(strEstimateId.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("msg", "estimateId is empty");
        	
        	return JSONResult(request, result);
    	}
    	if(!strVal.equals("0") && !strVal.equals("1")) {
    		result.put("retCode", 202);
        	result.put("msg", "val is invalid");
        	
        	return JSONResult(request, result);
    	}
    	
    	Elect elect = electDao.getDetail("kind=" + ElectDAO.ELECT_KIND_ESTIMATE + " and estimate_id=" + strEstimateId + " and owner=" + loginAccount.getId());
    	
    	if(strVal.equals("0")) {
    		if(elect != null) {
        		electDao.delete(elect);
        	}
    	}
    	else {
    		if(elect == null) {
    			
    			Estimate estimate = estimateDao.get(Integer.valueOf(strEstimateId));
    			
    			elect = new Elect();
    			
    			elect.setKind(ElectDAO.ELECT_KIND_ESTIMATE);
    			if(estimate.getType() == EstimateDAO.ESTIMATE_TYPE_PERSONAL_OR_ENTER) {
    				elect.setAccountId(estimate.getAccountId());	
    			}
    			else if(estimate.getType() == EstimateDAO.ESTIMATE_TYPE_PERSONAL_OR_ENTER) {
    				elect.setHotId(estimate.getHotId());
    			}
    			elect.setEstimateId(estimate.getId());
    			elect.setOwner(loginAccount.getId());
    			
    			electDao.insert(elect);
    		}
    	}
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView onContact(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strAccountId = this.getBlankParameter(request, "accountId", "");
    	
    	if(strAccountId.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("msg", "accountId is empty");
        	
        	return JSONResult(request, result);
    	}
    	
    	ClickHistory clickHistory = new ClickHistory();
    	
    	clickHistory.setOwnerId(loginAccount.getId());
    	clickHistory.setType(ClickHistoryDAO.HISTORY_TYPE_CONTACT);
    	clickHistory.setProductId(Integer.valueOf(strAccountId));
    	
    	clickHistoryDao.insert(clickHistory);
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView onShare(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strKind = this.getBlankParameter(request, "kind", "");
    	String strId = this.getBlankParameter(request, "id", "");
    	
    	if(!strKind.equals(String.valueOf(ClickHistoryDAO.HISTORY_SHARE_KIND_PRODUCT))
    			&& !strKind.equals(String.valueOf(ClickHistoryDAO.HISTORY_SHARE_KIND_ITEM))
    			&& !strKind.equals(String.valueOf(ClickHistoryDAO.HISTORY_SHARE_KIND_SERVICE))
    			&& !strKind.equals(String.valueOf(ClickHistoryDAO.HISTORY_SHARE_KIND_PERSONAL))
    			&& !strKind.equals(String.valueOf(ClickHistoryDAO.HISTORY_SHARE_KIND_ENTER))
    			&& !strKind.equals(String.valueOf(ClickHistoryDAO.HISTORY_SHARE_KIND_REPOET))) {
    		result.put("retCode", 201);
        	result.put("msg", "kind is invalid");
        	
        	return JSONResult(request, result);
    	}
    	if(strId.isEmpty()) {
    		result.put("retCode", 202);
        	result.put("msg", "id is empty");
        	
        	return JSONResult(request, result);
    	}
    	
    	ClickHistory clickHistory = new ClickHistory();
    	
    	clickHistory.setOwnerId(loginAccount.getId());
    	clickHistory.setType(Integer.valueOf(strKind));
    	
    	switch(Integer.valueOf(strKind)) {
    	
	    	case ClickHistoryDAO.HISTORY_SHARE_KIND_PRODUCT :
	    		
	    		clickHistory.setProductId(Integer.valueOf(strId));
	    		break;
	    	case ClickHistoryDAO.HISTORY_SHARE_KIND_ITEM :
	    		
	    		clickHistory.setItemId(Integer.valueOf(strId));
	    		break;
	    	case ClickHistoryDAO.HISTORY_SHARE_KIND_SERVICE :
	    		
	    		clickHistory.setServiceId(Integer.valueOf(strId));
	    		break;
	    	case ClickHistoryDAO.HISTORY_SHARE_KIND_PERSONAL :
	    	case ClickHistoryDAO.HISTORY_SHARE_KIND_ENTER :
	    		
	    		clickHistory.setAccountId(Integer.valueOf(strId));
	    		break;
    		default:
    			break;
    	}
    	
    	clickHistoryDao.insert(clickHistory);
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView setFavourite(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strKind = this.getBlankParameter(request, "kind", "");
    	String strId = this.getBlankParameter(request, "id", "");
    	String strVal = this.getBlankParameter(request, "val", "1");
    	
    	if(!strKind.equals(String.valueOf(FavouriteDAO.FAVOURITE_KIND_PRODUCT))
    			&& !strKind.equals(String.valueOf(FavouriteDAO.FAVOURITE_KIND_HOT))) {
    		result.put("retCode", 201);
        	result.put("msg", "kind is invalid");
        	
        	return JSONResult(request, result);
    	}
    	if(strId.isEmpty()) {
    		result.put("retCode", 202);
        	result.put("msg", "id is empty");
        	
        	return JSONResult(request, result);
    	}
    	
    	String strWhere = "owner=" + loginAccount.getId() + " and kind=" + strKind;
    	
    	if(strKind.equals(String.valueOf(FavouriteDAO.FAVOURITE_KIND_PRODUCT))) {
    		strWhere += " and product_id=" + strId;
    	}
    	else if(strKind.equals(String.valueOf(FavouriteDAO.FAVOURITE_KIND_PRODUCT))) {
    		strWhere += " and hot_id=" + strId;
    	}
    	
    	Favourite favourite = favouriteDao.getDetail(strWhere);
    	
    	if(strVal.equals("1")) {
    		if(favourite == null) {
    			
    			favourite = new Favourite();
    	    	
    	    	favourite.setKind(Integer.valueOf(strKind));
    	    	favourite.setOwner(loginAccount.getId());
    	    	if(strKind.equals(String.valueOf(FavouriteDAO.FAVOURITE_KIND_PRODUCT))) {
    	    		favourite.setProductId(Integer.valueOf(strId));
    	    	}
    	    	else if(strKind.equals(String.valueOf(FavouriteDAO.FAVOURITE_KIND_HOT))) {
    	    		favourite.setHotId(Integer.valueOf(strId));
    	    	}
    	    	
    	    	favouriteDao.insert(favourite);
        	}	
    	}
    	else if(strVal.equals("0")) {
    		if(favourite != null) {
    			favouriteDao.delete(favourite);	
    		}
    	}
    	else {
    		result.put("retCode", 202);
    		result.put("msg", "val is invalid");
    		
    		return JSONResult(request, result);
    	}
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getNoticeList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strStart = this.getBlankParameter(request, "start", "0");
    	String strLength = this.getBlankParameter(request, "length", "-1");
    	
    	String strWhere = "type=" + NoticeDAO.NOTICE_TYPE_USER + " and account_id=" + loginAccount.getId();
    	
    	JSONObject filter = new JSONObject();
    	filter.put("start", Integer.parseInt(strStart));
    	filter.put("length", Integer.parseInt(strLength));
    	
    	List<Notice> noticeList = noticeDao.search(filter, strWhere, "write_time desc");
    	int moreCnt = noticeDao.count(null, strWhere) - (Integer.valueOf(strStart) + noticeList.size());
    	
    	result.put("retCode", 200);
    	result.put("data", noticeList);
    	result.put("moreCnt", moreCnt);
    	
    	return JSONResult(request, result);
    }
    
    private ModelAndView getNoticeCount(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strWhere = "type=" + NoticeDAO.NOTICE_TYPE_USER + " and account_id=" + loginAccount.getId();
    	
    	int noticeCnt = noticeDao.count(null, strWhere);
    	
    	result.put("retCode", 200);
    	result.put("noticeCnt", noticeCnt);
    	
    	return JSONResult(request, result);
    }
}