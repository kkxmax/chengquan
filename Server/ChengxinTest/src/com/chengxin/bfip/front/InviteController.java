package com.chengxin.bfip.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import com.chengxin.bfip.Constants;
import com.chengxin.bfip.model.AccountDAO;
import com.chengxin.common.BaseController;

/**
 *
 * @author Administrator
 */
public class InviteController extends BaseController {
    
    public InviteController() throws Exception {
    	
    }

    public ModelAndView init(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
        String action = this.getBlankParameter(request, "pAct", "");
                
        if (action.equals("") || action.equals("index")) {
            return this.index(request, response, session);
        } 
        
        return null;
    }
    
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String reqCode = this.getBlankParameter(request, "reqcode", "");
    	
    	String[] reqCodeArr = reqCode.split("");
    	request.setAttribute("reqCode", reqCode);
    	request.setAttribute("reqCodeArr", reqCodeArr);
    	
    	return new ModelAndView("front/invite/index");
    }
   
}