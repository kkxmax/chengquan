package com.chengxin.bfip.manage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.chengxin.bfip.Constants;
import com.chengxin.bfip.model.Account;
import com.chengxin.bfip.model.AccountDAO;
import com.chengxin.common.BaseController;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;

/**
 *
 * @author Administrator
 */
public class PersonalAccountController extends BaseController {
    
    private AccountDAO memberDao = null;
    
    public void setMemberDao(AccountDAO value) {this.memberDao = value;}
    
    public PersonalAccountController() throws Exception {
    	
    }

    public ModelAndView init(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	if(!SessionUtil.isLogined(request)) {
            return JavascriptUtil.MessageMove(request, response, "", "login.html");
        }
    	if(!this.checkManagePermission(request, "personal_account.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
    	request.setAttribute("user_info", SessionUtil.getSessionVar(request, "USER_INFO"));
    	request.setAttribute("cur_page", "personal_account.html");
    	request.setAttribute("title", new String[] {"用户管理", "个人账号"});
    	request.setAttribute("breadcrumbs", new KeyValueString[] {
    			new KeyValueString("用户管理", "personal_account.html"),
    			new KeyValueString("个人账号", "personal_account.html")
    	});
    	
        String action = this.getBlankParameter(request, "pAct", "");
                
        if (action.equals("") || action.equals("index")) {
            return this.index(request, response, session);
        } else if (action.equals("search")) {
            return this.search(request, response, session);
        } else if (action.equals("showTestModal")) {
            return this.showTestModal(request, response, session);
        } else if (action.equals("viewDetail")) {
            return this.viewDetail(request, response, session);
        } else if (action.equals("changeBanStatus")) {
            return this.changeBanStatus(request, response, session);
        } else if (action.equals("changeTestStatus")) {
            return this.changeTestStatus(request, response, session);
        } 
        
        return null;
    }
    
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	request.setAttribute("C_ACCOUNT_TEST_STATUS", Constants.C_ACCOUNT_TEST_STATUS);
    	request.setAttribute("C_BAN_STATUS", Constants.C_BAN_STATUS);
    	
    	return new ModelAndView("manage/personal_account/index");
    }
    
    public ModelAndView search(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

    	JSONObject result = new JSONObject();
    	JSONObject filterParamObject = new JSONObject();
    	
        String filterParam = this.getBlankParameter(request, "filter[filter_param]", "");
        String start = this.getBlankParameter(request, "start", "");
        String length = this.getBlankParameter(request, "length", "");
        String orderColIndex = this.getBlankParameter(request, "order[0][column]", "");
        String orderColName = this.getBlankParameter(request, "columns[" + orderColIndex + "][name]", "");
        String orderDir = this.getBlankParameter(request, "order[0][dir]", "asc");
        
        if(!filterParam.isEmpty()) {
        	filterParam = HtmlUtils.htmlUnescape(filterParam);
            filterParamObject = JSONObject.fromObject(filterParam);	
        }
        filterParamObject.put("start", start);
        filterParamObject.put("length", length);
        filterParamObject.put("order_col", orderColName);
        filterParamObject.put("order_dir", orderDir);
        
        String extraWhere = "akind = " + AccountDAO.ACCOUNT_TYPE_PERSONAL;
        List<Account> accountList = memberDao.search(filterParamObject, extraWhere);
        int count = memberDao.count(filterParamObject, extraWhere);
        
        ArrayList<String[]> data = new ArrayList<String[]>();
        
        for(int i=0; i<accountList.size(); i++) {
        	Account row = accountList.get(i); 

        	String opHtml = "<a href='personal_account.html?pAct=viewDetail&id=" + String.valueOf(row.getId()) 
    				+ "' class='btn btn-xs purple' data-target='#global-modal' data-toggle='modal'><i class='fa fa-edit'></i> 查看</a>";
        	if(row.getTestStatus() == AccountDAO.TEST_ST_READY || row.getTestStatus() == AccountDAO.TEST_ST_UNPASSED) {
        		opHtml += "<a href='personal_account.html?pAct=showTestModal&id=" + String.valueOf(row.getId()) 
        				+ "' class='btn btn-xs purple' data-target='#global-modal' data-toggle='modal'><i class='fa fa-edit'></i> 审核</a>";
        	}
        	if(row.getBanStatus() == AccountDAO.BAN_ST_BANED) {
        		opHtml += "<a href='javascript:changeBanStatus(" + String.valueOf(row.getId()) + ", " + AccountDAO.BAN_ST_UNBANED + ")' "
        				+ "class='btn btn-xs red'><i class='fa fa-edit'></i> 取消禁用</a>";
        	}
        	if(row.getBanStatus() == AccountDAO.BAN_ST_UNBANED) {
        		opHtml += "<a href='javascript:changeBanStatus(" + String.valueOf(row.getId()) + ", " + AccountDAO.BAN_ST_BANED + ")' "
        				+ "class='btn btn-xs red'><i class='fa fa-edit'></i> 禁用</a>";
        	}
        	
        	String[] dataItem = new String[] {
        			String.valueOf(i+1),
        			"<img src='" + Constants.C_UPLOAD_PATH + row.getLogo() + "' alt='头像图片' width='45px' height='45px'>",
        			DateTimeUtil.dateFormat(row.getWriteTime()),
        			row.getMobile(),
        			row.getEnterName(),
        			row.getCode(),
        			"0%",
        			String.valueOf(row.getElectCnt()),
        			row.getTestStatusName(),
        			row.getBanStatusName(),
        			opHtml
        	};
        	data.add(dataItem);
        }
        result.put("recordsTotal", count);
        result.put("recordsFiltered", count);
        result.put("data", data);
        
        request.setAttribute("JSON", result);
        
        return new ModelAndView("json_result");
    } 
    
    public ModelAndView viewDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	JSONObject result = new JSONObject();
    	
    	String id = this.getBlankParameter(request, "id", "");

    	Account record = memberDao.getDetail(Integer.valueOf(id));
    	
    	request.setAttribute("record", record);
    	
    	return new ModelAndView("manage/personal_account/detail");
    }

    public ModelAndView showTestModal(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	JSONObject result = new JSONObject();
    	
    	String id = this.getBlankParameter(request, "id", "");

    	Account record = memberDao.getDetail(Integer.valueOf(id));
    	
    	request.setAttribute("record", record);
    	
    	return new ModelAndView("manage/personal_account/test_modal");
    }

    public ModelAndView changeBanStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	JSONObject result = new JSONObject();
    	
    	String id = this.getBlankParameter(request, "id", "");
    	String targetStatus = this.getBlankParameter(request, "targetStatus", "");
    	
    	Account record = memberDao.get(Integer.valueOf(id));
    	
    	record.setBanStatus(Integer.valueOf(targetStatus));
    	
    	memberDao.update(record);
    	
    	result.put("retcode", 200);
    	result.put("msg", "操作成功");
    	
    	request.setAttribute("JSON", result);
    	
    	return new ModelAndView("json_result");
    }
    
    public ModelAndView changeTestStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	JSONObject result = new JSONObject();
    	
    	String id = this.getBlankParameter(request, "id", "");
    	String targetStatus = this.getBlankParameter(request, "targetStatus", "");
    	
    	Account record = memberDao.get(Integer.valueOf(id));
    	
    	record.setTestStatus(Integer.valueOf(targetStatus));
    	
    	memberDao.update(record);
    	
    	result.put("retcode", 200);
    	result.put("msg", "操作成功");
    	
    	request.setAttribute("JSON", result);
    	
    	return new ModelAndView("json_result");
    }
}