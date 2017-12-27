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
public class RedianController extends BaseController {
    
    private AccountDAO accountDao = null;
    
    public void setAccountDao(AccountDAO value) {this.accountDao = value;}
    
    public RedianController() throws Exception {
    	
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
    	
    	request.setAttribute("reqCode", reqCode);
    	
    	return new ModelAndView("front/redian/index");
    }
   
}