/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chengxin.common;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.chengxin.bfip.manage.SessionUtil;
import com.chengxin.bfip.model.User;

public class BaseController extends MultiActionController {

    protected CachedObjectService cachedObjectService = null;
    protected AppSettings appSettings = new AppSettings();

    public void setCachedObjectService(CachedObjectService value) {
        this.cachedObjectService = value;
    }

    public BaseController() throws Exception {
    }

    protected void initSettings(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
    }

    protected String getSetting(String settingName) throws Exception {
        return cachedObjectService.getProperty(CachedObjectService.C_APP_SETTINGS_NAME, settingName);
    }

    public String getBlankParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);

        if (value == null) {
            return defaultValue;
        } else if (value.length() == 0) {
            return defaultValue;
        } else {
            return SecureUtil.XSSFilter(value);
        }
    }

    public String getBlankAttribute(HttpSession session, String name, String defaultValue) {
        Object value = session.getAttribute(name);

        if (value == null) {
            return defaultValue;
        } else if (value.toString().length() == 0) {
            return defaultValue;
        } else {
            return SecureUtil.XSSFilter(value);
        }
    }

    protected String getBlankParameter(HttpServletRequest request, String name,
            String defaultValue, Map<String, String> parameterMap) {
        String value = this.getBlankParameter(request, name, defaultValue);

        parameterMap.put(name, value);

        return value;
    }

    protected Map getParameters(HttpServletRequest request) {
        Enumeration parameters = request.getParameterNames();
        HashMap map = new HashMap();

        while (parameters.hasMoreElements()) {
            String name = parameters.nextElement().toString();

            map.put(name, this.getBlankParameter(request, name, ""));
        }

        return map;
    }
    
    public boolean checkManagePermission(HttpServletRequest request, String page) {
    	User user = (User) SessionUtil.getSessionVar(request, "USER_INFO");

    	if(page.equals("personal_account.html") && user.getFuncPersonal() == 0) {
    		return false;    		
    	}
    	if(page.equals("enterprise_account.html") && user.getFuncEnter() == 0) {
    		return false;    		
    	}
    	if(page.equals("product.html") && user.getFuncProduct() == 0) {
    		return false;    		
    	}
    	if(page.equals("item.html") && user.getFuncItem() == 0) {
    		return false;    		
    	}
    	if(page.equals("service.html") && user.getFuncItem() == 0) {
    		return false;    		
    	}
    	if(page.equals("carousel.html") && user.getFuncCarousel() == 0) {
    		return false;    		
    	}
    	if(page.equals("video.html") && user.getFuncVideo() == 0) {
    		return false;    		
    	}
    	if(page.equals("hots.html") && user.getFuncHot() == 0) {
    		return false;    		
    	}
    	if(page.equals("estimate.html") && user.getFuncComment() == 0) {
    		return false;    		
    	}
    	if(page.equals("error.html") && user.getFuncError() == 0) {
    		return false;    		
    	}
    	if((page.equals("link_statis.html") || (page.equals("item_statis.html")) || (page.equals("etc_statis.html"))
    			 || (page.equals("buy_statis.html")) || (page.equals("request_statis.html"))) && user.getFuncStatistic() == 0) {
    		return false;    		
    	}
    	if(page.equals("fenlei.html") && user.getFuncFenlei() == 0) {
    		return false;    		
    	}
    	if(page.equals("pleixing.html") && user.getFuncChanpin() == 0) {
    		return false;    		
    	}
    	if(page.equals("xyleixing.html") && user.getFuncXingye() == 0) {
    		return false;    		
    	}
    	if(page.equals("opinion.html") && user.getFuncOpinion() == 0) {
    		return false;    		
    	}
    	if((page.equals("user.html") || (page.equals("role.html"))) && user.getFuncSystem() == 0) {
    		return false;    		
    	}
    	return true;
    }
}
