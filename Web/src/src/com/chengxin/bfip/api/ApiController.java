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

import com.chengxin.bfip.CommonUtil;
import com.chengxin.bfip.Constants;
import com.chengxin.bfip.model.Account;
import com.chengxin.bfip.model.AccountDAO;
import com.chengxin.bfip.model.Carousel;
import com.chengxin.bfip.model.CarouselDAO;
import com.chengxin.bfip.model.City;
import com.chengxin.bfip.model.CityDAO;
import com.chengxin.bfip.model.ClickHistoryDAO;
import com.chengxin.bfip.model.ElectDAO;
import com.chengxin.bfip.model.ErrorsDAO;
import com.chengxin.bfip.model.EstimatesDAO;
import com.chengxin.bfip.model.FenleiDAO;
import com.chengxin.bfip.model.HotsDAO;
import com.chengxin.bfip.model.InterestDAO;
import com.chengxin.bfip.model.ItemDAO;
import com.chengxin.bfip.model.NoticeDAO;
import com.chengxin.bfip.model.OpinionsDAO;
import com.chengxin.bfip.model.PleixingDAO;
import com.chengxin.bfip.model.ProductsDAO;
import com.chengxin.bfip.model.Province;
import com.chengxin.bfip.model.ProvinceDAO;
import com.chengxin.bfip.model.ReqCode;
import com.chengxin.bfip.model.ReqCodeDAO;
import com.chengxin.bfip.model.RoleDAO;
import com.chengxin.bfip.model.ServicesDAO;
import com.chengxin.bfip.model.UserDAO;
import com.chengxin.bfip.model.VerifyCode;
import com.chengxin.bfip.model.VerifyCodeDAO;
import com.chengxin.bfip.model.VideosDAO;
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

	private static boolean MODE_DEVEL = true;
	private String strLoginMobile = "19135411631";
	private Account loginAccount = null;
	private JSONObject result = null;
	
    public ApiController() throws Exception {
		super();
	}

	private AccountDAO accountDao = null;
    private CarouselDAO carouselDao = null;
    private ClickHistoryDAO clickHistoryDao = null;
    private ErrorsDAO errorDao = null;
    private EstimatesDAO estimateDao = null;
    private FenleiDAO fenleiDao = null;
    private HotsDAO hotDao = null;
    private ItemDAO itemDao = null;
    private NoticeDAO noticeDao = null;
    private OpinionsDAO opinionDao = null;
    private PleixingDAO pleixingDao = null;
    private ProductsDAO productDao = null;
    private RoleDAO roleDao = null;
    private ServicesDAO serviceDao = null;
    private UserDAO userDao = null;
    private VideosDAO videoDao = null;
    private XyleixingDAO xyleixingDao = null;
    private ProvinceDAO provinceDao = null;
    private CityDAO cityDao = null;
    private ReqCodeDAO reqCodeDao = null;
    private InterestDAO interestDao = null;
    private ElectDAO electDao = null;
    private VerifyCodeDAO verifyCodeDao = null;
    
    public void setAccountDao(AccountDAO value) {this.accountDao = value;}
    public void setCarouselDao(CarouselDAO value) {this.carouselDao = value;}
    public void setClickHistoryDao(ClickHistoryDAO value) {this.clickHistoryDao = value;}
    public void setErrorDao(ErrorsDAO value) {this.errorDao = value;}
    public void setEstimateDao(EstimatesDAO value) {this.estimateDao = value;}
    public void setFenleiDao(FenleiDAO value) {this.fenleiDao = value;}
    public void setHotDao(HotsDAO value) {this.hotDao = value;}
    public void setItemDao(ItemDAO value) {this.itemDao = value;}
    public void setNoticeDao(NoticeDAO value) {this.noticeDao = value;}
    public void setOpinionDao(OpinionsDAO value) {this.opinionDao = value;}
    public void setPleixingDao(PleixingDAO value) {this.pleixingDao = value;}
    public void setProductDao(ProductsDAO value) {this.productDao = value;}
    public void setRoleDao(RoleDAO value) {this.roleDao = value;}
    public void setServiceDao(ServicesDAO value) {this.serviceDao = value;}
    public void setUserDao(UserDAO value) {this.userDao = value;}
    public void setVideoDao(VideosDAO value) {this.videoDao = value;}
    public void setXyleixingDao(XyleixingDAO value) {this.xyleixingDao = value;}
    public void setProvinceDao(ProvinceDAO value) {this.provinceDao = value;}
    public void setCityDao(CityDAO value) {this.cityDao = value;}
    public void setReqCodeDao(ReqCodeDAO value) {this.reqCodeDao = value;}
    public void setInterestDao(InterestDAO value) {this.interestDao = value;}
    public void setElectDao(ElectDAO value) {this.electDao = value;}
    public void setVerifyCodeDao(VerifyCodeDAO value) {this.verifyCodeDao = value;}

    public ModelAndView init(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
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
        	if(!strAction.equals("getVerifyCode") && !strAction.equals("register") && !strAction.equals("login") && !strAction.equals("resetPassword")) {
            	
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
            }
        }
        
        if (strAction.equals("register")) {
            return this.register(request, response, session);
        } else if(strAction.equals("getVerifyCode")) {
        	return this.getVerifyCode(request, response, session);
        } else if(strAction.equals("login")) {
        	return this.login(request, response, session);
        } else if(strAction.equals("resetPassword")) {
        	return this.resetPassword(request, response, session);
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
    
    public ModelAndView register(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
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
    		reqCode = reqCodeDao.getDetail("req_code='" + strReqCode + "'");
    		if(reqCode == null) {
    			result.put("retCode", 208);
        		result.put("msg", "该验证码不存在");
        		
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
    	account.setReqCodeId(reqCode.getId());
    	
    	accountDao.insert(account);
    	
    	account = accountDao.getDetail("mobile=" + strMobile);
    	
    	if(reqCode != null) {
    		reqCode.setStatus(ReqCodeDAO.STATUS_USED);
    		reqCode.setWriteTime(new Date());
    		reqCodeDao.update(reqCode);
    	}
    	
    	result.put("retCode", 200);
    	
    	return JSONResult(request, result);
    }
    
    public ModelAndView getVerifyCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String strMobile = this.getBlankParameter(request, "mobile", "");
    	
    	if(strMobile.isEmpty()) {
    		result.put("retCode", 201);
        	result.put("verifyCode", "手机不能为空");
        	
        	return JSONResult(request, result);
    	}
    	
    	String strVerifyCode = String.valueOf((new Random().nextInt(899999) + 100000));
    	
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
    
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
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
		
    	String strToken = SecureUtil.getSHA1(String.valueOf((new Random().nextInt(899999) + 100000)));
    	
    	record.setToken(strToken);
    	accountDao.update(record);
    	
    	result.put("retCode", 200);
    	result.put("token", strToken);
    	result.put("userInfo", record);
    	
    	return JSONResult(request, result);
    }
    
    public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
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
    

}