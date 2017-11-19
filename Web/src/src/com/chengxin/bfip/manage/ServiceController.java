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
import com.chengxin.bfip.model.AccountDAO;
import com.chengxin.bfip.model.Service;
import com.chengxin.bfip.model.ServiceDAO;
import com.chengxin.common.BaseController;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;

/**
 * 
 * @author Administrator
 */
public class ServiceController extends BaseController {

	private ServiceDAO memberDao = null;

	public void setMemberDao(ServiceDAO value) {
		this.memberDao = value;
	}

	public ServiceController() throws Exception {

	}

	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		if (!SessionUtil.isLogined(request)) {
			return JavascriptUtil.MessageMove(request, response, "",
					"login.html");
		}
		if(!this.checkManagePermission(request, "service.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
		request.setAttribute("user_info",
				SessionUtil.getSessionVar(request, "USER_INFO"));

		request.setAttribute("cur_page", "service");
		request.setAttribute("title", new String[] { "服务管理", "服务管理" });
		request.setAttribute("breadcrumbs",
				new KeyValueString[] { new KeyValueString("服务管理",
						"service.html") });

		String action = this.getBlankParameter(request, "pAct", "");

		if (action.equals("") || action.equals("index")) {
			return this.index(request, response, session);
		} else if (action.equals("search")) {
			return this.search(request, response, session);
		} else if (action.equals("viewDetail")) {
			return this.viewDetail(request, response, session);
		} else if (action.equals("delete")) {
			return this.delete_record(request, response, session);
		} 
		
		return null;
	}

	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		request.setAttribute("C_SERVICE_BOOKTYPE_NAME", Constants.C_ACCOUNT_ACCOUNT_TYPE);
		return new ModelAndView("manage/service/index");
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
		List<Service> ServiceList = memberDao.search(filterParamObject,
				extraWhere);
		int count = memberDao.count(filterParamObject, extraWhere);

		ArrayList<String[]> data = new ArrayList<String[]>();

		for (int i = 0; i < ServiceList.size(); i++) {
			Service row = ServiceList.get(i);

			String opHtml = "<a href='service.html?pAct=viewDetail&id=" + row.getId() + "' class='btn btn-xs purple' data-target='#global-modal' data-toggle='modal'><i class='fa fa-edit'></i> 查看</a>";
			opHtml += "<a href='javascript:;delete_record("+ row.getId() +");' class='btn btn-xs default'><i class='fa fa-trash-o'></i>删除</a>";
			String[] dataItem = new String[] {
					row.getCode(), 
					row.getAccountMobile(), 
					row.getName(),
					row.getContactName(), 
					row.getContactMobile(),
					row.getContactWeixin(), 
					row.getAkind() == AccountDAO.ACCOUNT_TYPE_PERSONAL ? row.getRealname() : row.getEnterName(),
					row.getAkindName(), 
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

	public ModelAndView viewDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		String id = this.getBlankParameter(request, "id", "");

		Service record = memberDao.getDetail(Integer.valueOf(id));

		request.setAttribute("record", record);

		return new ModelAndView("manage/service/detail");
	}

	public ModelAndView delete_record(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		Service record = memberDao.get(Integer.valueOf(id));

		memberDao.delete(record);

		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");


	}

}