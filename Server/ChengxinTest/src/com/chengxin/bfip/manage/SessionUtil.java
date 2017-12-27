/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chengxin.bfip.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.chengxin.bfip.model.User;
import com.chengxin.common.CommonType;

/**
 *
 * @author Administrator
 */
public class SessionUtil {
    public static void clear(HttpServletRequest request) {
        HttpSession session = request.getSession();

        session.setAttribute("MANAGE_LOGIN_ID", null);
        session.setAttribute("MANAGE_LOGIN_USERNAME", null);
    }

    public static boolean isLogined(HttpServletRequest request) {
    	User user = (User) SessionUtil.getSessionVar(request, "USER_INFO");
    	if(user == null) { return false;}
        if(getLoginId(request) == 0) { return false;}
        else {return true;}
    }
    
    public static void setLoginId(HttpServletRequest request, int loginId) {
        HttpSession session = request.getSession();
        
        session.setAttribute("MANAGE_LOGIN_ID", loginId);
    }

    public static int getLoginId(HttpServletRequest request) {
        HttpSession session = request.getSession();

        Object result = session.getAttribute("MANAGE_LOGIN_ID");

        if(result == null) {return 0;}

        return Integer.valueOf(result.toString());
    }
    
    public static void setLoginUsername(HttpServletRequest request, String loginUsername) {
        HttpSession session = request.getSession();
        
        session.setAttribute("MANAGE_LOGIN_USERNAME", loginUsername);
    }

    public static String getLoginUsername(HttpServletRequest request) {
        HttpSession session = request.getSession();

        Object result = session.getAttribute("MANAGE_LOGIN_USERNAME");

        if(result == null) {return CommonType.C_STRING_NULL;}

        return (String)result;
    }
    
    public static void setSessionVar(HttpServletRequest request, String key, Object value) {
    	HttpSession session = request.getSession();
        
        session.setAttribute(key, value);
    }
    
    public static Object getSessionVar(HttpServletRequest request, String key) {
        HttpSession session = request.getSession();

        Object result = session.getAttribute(key);

        return result;
    }
}

