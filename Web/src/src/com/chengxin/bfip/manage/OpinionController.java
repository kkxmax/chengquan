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
import com.chengxin.bfip.model.Opinion;
import com.chengxin.bfip.model.OpinionDAO;
import com.chengxin.common.BaseController;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;

/**
 * 
 * @author Administrator
 */
public class OpinionController extends BaseController {

	private OpinionDAO memberDao = null;

	public void setMemberDao(OpinionDAO value) {
		this.memberDao = value;
	}

	public OpinionController() throws Exception {

	}

	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		if (!SessionUtil.isLogined(request)) {
			return JavascriptUtil.MessageMove(request, response, "",
					"login.html");
		}
		if(!this.checkManagePermission(request, "opinion.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
		request.setAttribute("user_info",
				SessionUtil.getSessionVar(request, "USER_INFO"));

		request.setAttribute("cur_page", "opinion.html");
		request.setAttribute("title", new String[] { "意见反馈", "意见反馈" });
		request.setAttribute("breadcrumbs",
				new KeyValueString[] { new KeyValueString("意见反馈",
						"opinion.html") });

		String action = this.getBlankParameter(request, "pAct", "");

		if (action.equals("") || action.equals("index")) {
			return this.index(request, response, session);
		} else if (action.equals("search")) {
			return this.search(request, response, session);
		} else if (action.equals("management")) {
			return this.management(request, response, session);
		} 
		
		return null;
	}

	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		request.setAttribute("C_OPINION_STATUS_NAME", Constants.C_OPINION_STATUS_NAME);
		return new ModelAndView("manage/opinion/index");
	}

	public ModelAndView search(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject filterParamObject = new JSONObject();

		String filterParam = this.getBlankParameter(request,
				"filter[filter_param]", "");
		String start = this.getBlankParameter(request, "start", "");
		String length = this.getBlankParameter(request, "length", "");
		String orderColIndex = this.getBlankParameter(request,
				"order[0][column]", "");
		String orderColName = this.getBlankParameter(request, "columns["
				+ orderColIndex + "][name]", "");
		String orderDir = this.getBlankParameter(request, "order[0][dir]",
				"asc");

		if (!filterParam.isEmpty()) {
			filterParam = HtmlUtils.htmlUnescape(filterParam);
			filterParamObject = JSONObject.fromObject(filterParam);
		}
		filterParamObject.put("start", start);
		filterParamObject.put("length", length);
		filterParamObject.put("order_col", orderColName);
		filterParamObject.put("order_dir", orderDir);

		String extraWhere = "";
		List<Opinion> OpinionList = memberDao.search(filterParamObject,
				extraWhere);
		int count = memberDao.count(filterParamObject, extraWhere);

		ArrayList<String[]> data = new ArrayList<String[]>();

		for (int i = 0; i < OpinionList.size(); i++) {
			Opinion row = OpinionList.get(i);
			String opHtml = " ";

			if(row.getStatus() == 1){
				opHtml = "<a href='javascript:;management("+ String.valueOf(row.getId()) +");"
						+ "' class='btn btn-xs green'><i class='fa fa-edit'></i> 处理</a>";
			}
			String[] dataItem = new String[] {
					row.getMobile(), 
					row.getContent(), 
					DateTimeUtil.dateFormat(row.getWriteTime()), 
					row.getStatusName(),
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


	public ModelAndView management(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		Opinion record = memberDao.get(Integer.valueOf(id));
		record.setStatus(2);
		memberDao.update(record);

		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}
}