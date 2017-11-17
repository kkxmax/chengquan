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
import com.chengxin.common.KeyValueString;
import com.chengxin.common.SecureUtil;

/**
 *
 * @author Administrator
 */
public class ProfileController extends BaseController {

    private UserDAO userDao = null;
    
    public void setUserDao(UserDAO value) {this.userDao = value;}
    
    public ProfileController() throws Exception {
    	
    }

    public ModelAndView init(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	if(!SessionUtil.isLogined(request)) {
            return JavascriptUtil.MessageMove(request, response, "", "login.html");
        }
    	request.setAttribute("user_info", SessionUtil.getSessionVar(request, "USER_INFO"));
    	request.setAttribute("cur_page", "profile.html");
    	request.setAttribute("title", new String[] {"修改密码"});
    	request.setAttribute("breadcrumbs", new KeyValueString[] {
    			new KeyValueString("修改密码", "profile.html")
    	});
    	
        String action = this.getBlankParameter(request, "pAct", "");
                
        if (action.equals("") || action.equals("index")) {
            return this.index(request, response, session);
        } else if(action.equals("changePwdDo")) {
        	return this.changePwdDo(request, response, session);
        }
        
        return null;
    }
    
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	return new ModelAndView("manage/profile/index");
    }
    
    public ModelAndView changePwdDo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	JSONObject result = new JSONObject();
    	
    	String oldPass = this.getBlankParameter(request, "oldpass", "");
    	String newPass = this.getBlankParameter(request, "newpass", "");
    	String confirmPass = this.getBlankParameter(request, "confirmpass", "");
    	
    	User record = userDao.get(Integer.valueOf(SessionUtil.getLoginId(request)));
    	
    	if(!record.getPassword().equals(SecureUtil.getMD5(oldPass))) {
    		result.put("retcode", 201);
        	result.put("msg", "旧密码错误");
    	}
    	else if(newPass.equals(oldPass)) {
    		result.put("retcode", 202);
        	result.put("msg", "新密码和旧密码不能一致");
    	}
    	else if(!newPass.equals(confirmPass)) {
    		result.put("retcode", 203);
        	result.put("msg", "两次密码不一致");
    	}
    	else {
    		record.setPassword(SecureUtil.getMD5(newPass));
        	
        	userDao.update(record);
        	
        	result.put("retcode", 200);
        	result.put("msg", "修改密码成功");	
    	}
    	
    	request.setAttribute("JSON", result);
    	
    	return new ModelAndView("json_result");
    }

}