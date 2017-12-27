package com.chengxin.bfip.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import com.chengxin.bfip.model.User;
import com.chengxin.bfip.model.UserDAO;
import com.chengxin.common.BaseController;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.SecureUtil;

/**
 *
 * @author Administrator
 */
public class LoginController extends BaseController {
    
    private UserDAO userDao = null;
    
    public void setUserDao(UserDAO value) {this.userDao = value;}
    
    public LoginController() throws Exception {
    	
    }

    public ModelAndView init(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	request.setAttribute("cur_page", "login.html");
    	
        String action = this.getBlankParameter(request, "pAct", "");
                
        if (action.equals("") || action.equals("index")) {
            return this.index(request, response, session);
        } else if (action.equals("loginDo")) {
            return this.loginDo(request, response, session);
        } else if (action.equals("logoutDo")) {
            return this.logoutDo(request, response, session);
        } 
        
        return null;
    }
    
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	return new ModelAndView("manage/login/index");
    }

    public ModelAndView loginDo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	JSONObject result = new JSONObject();
    	
    	String username = this.getBlankParameter(request, "username", "");
    	String password = this.getBlankParameter(request, "password", "");
    	
    	User record = userDao.getDetail("username = '" + username + "'");
    	if(record != null) {
    		String tmp = SecureUtil.getMD5(password);
    		if(record.getPassword().equals(SecureUtil.getMD5(password))) {
    			
    			SessionUtil.setLoginId(request, record.getId());
    			SessionUtil.setLoginUsername(request, record.getUsername());
    			SessionUtil.setSessionVar(request, "USER_INFO", record);
    			
    			result.put("retcode", 200);
    		}
    		else {
    			result.put("retcode", 202);
    	    	result.put("msg", "密码不正确");
    		}
    	}
    	else {
    		result.put("retcode", 201);
	    	result.put("msg", "该账号不存在");
    	}
    	
    	request.setAttribute("JSON", result);
    	
    	return new ModelAndView("json_result");
    }
    
    public ModelAndView logoutDo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        SessionUtil.clear(request);
        
        return JavascriptUtil.MessageMove(request, response, "", "personal_account.html");
    } 
    
}