package com.chengxin.bfip.manage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import com.chengxin.bfip.Constants;
import com.chengxin.bfip.model.ClickHistory;
import com.chengxin.bfip.model.ClickHistoryDAO;
import com.chengxin.common.BaseController;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;

/**
 * 
 * @author Administrator
 */
public class EtcStatisController extends BaseController {

	private ClickHistoryDAO memberDao = null;

	public void setMemberDao(ClickHistoryDAO value) {
		this.memberDao = value;
	}

	public EtcStatisController() throws Exception {

	}

	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		if (!SessionUtil.isLogined(request)) {
			return JavascriptUtil.MessageMove(request, response, "",
					"login.html");
		}
		if(!this.checkManagePermission(request, "etc_statis.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
		request.setAttribute("user_info",
				SessionUtil.getSessionVar(request, "USER_INFO"));

		request.setAttribute("cur_page", "etc_statis.html");
		request.setAttribute("title", new String[] { "数据统计", "其他分享" });
		request.setAttribute("breadcrumbs",new KeyValueString[] { 
				new KeyValueString("数据统计", "link_statis.html"),
    			new KeyValueString("其他分享", "etc_statis.html")});

		String action = this.getBlankParameter(request, "pAct", "");

		if (action.equals("") || action.equals("index")) {
			return this.index(request, response, session);
		} else if(action.equals("statis_search")){
			return this.statis_search(request, response, session);
		}
		
		return null;
	}

	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception 
			{
		
		request.setAttribute("C_SERVICE_BOOKTYPE_NAME", Constants.C_ACCOUNT_ACCOUNT_TYPE);
		return new ModelAndView("manage/etc_statis/index");
	}
	
	public ModelAndView statis_search(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		
		JSONObject result = new JSONObject();

		String from = this.getBlankParameter(request, "from", "");
		String to = this.getBlankParameter(request, "to", "");

//		String where = " ";
		List<ClickHistory> records = memberDao.etc_statis(ClickHistoryDAO.HISTORY_TYPE_SHARE , from , to);

		result.put("retcode", 200);
		result.put("records", records);

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
		
		
	}

}