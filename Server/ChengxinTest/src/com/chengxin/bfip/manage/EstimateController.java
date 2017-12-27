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
import com.chengxin.bfip.model.Estimate;
import com.chengxin.bfip.model.EstimateDAO;
import com.chengxin.common.BaseController;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;

/**
 * 
 * @author Administrator
 */
public class EstimateController extends BaseController {

	private EstimateDAO memberDao = null;

	public EstimateDAO getMemberDao() {
		return memberDao;
	}

	public void setMemberDao(EstimateDAO value) {
		this.memberDao = value;
	}

	public EstimateController() throws Exception {

	}

	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		if (!SessionUtil.isLogined(request)) {
			return JavascriptUtil.MessageMove(request, response, "",
					"login.html");
		}
		if(!this.checkManagePermission(request, "estimate.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
		request.setAttribute("user_info",
				SessionUtil.getSessionVar(request, "USER_INFO"));
		request.setAttribute("cur_page", "estimate.html");
		request.setAttribute("title", new String[] { "评价管理", "评价管理" });
		request.setAttribute("breadcrumbs",
				new KeyValueString[] { new KeyValueString("评价管理",
						"estimate.html") });

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

		request.setAttribute("C_ESTIMATE_KIND_NAME", Constants.C_ESTIMATE_KIND_NAME);
		request.setAttribute("C_ESTIMATE_METHOD_NAME", Constants.C_ESTIMATE_METHOD_NAME);
		//		String where = " upper_id is null ";
		//		List<Xyleixings> leixingList = leixingDao.search(null , where);
		//		ArrayList<String[]> data = new ArrayList<String[]>();
		//		for (int i = 0; i < leixingList.size(); i++) {
		//			Xyleixings row = leixingList.get(i);
		//			String[] dataItem = new String[] {
		//					String.valueOf(row.getId()),
		//					row.getTitle() 
		//			};
		//			data.add(dataItem);
		//		}
		//		request.setAttribute("C_Estimate_KIND_NAME",  data);
		return new ModelAndView("manage/estimate/index");
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

		String extraWhere = "(upper_id is null or upper_id=0)";
		List<Estimate> EstimateList = memberDao.search(filterParamObject,
				extraWhere);
		int count = memberDao.count(filterParamObject, extraWhere);

		ArrayList<String[]> data = new ArrayList<String[]>();

		for (int i = 0; i < EstimateList.size(); i++) {
			Estimate row = EstimateList.get(i);

			String opHtml = "<a href='estimate.html?pAct=viewDetail&id="
					+ String.valueOf(row.getId())
					+ "' class='btn btn-xs purple'><i class='fa fa-edit'></i> 查看</a>";
			String[] dataItem = new String[] {
					row.getOwnerMobile(), 
					row.getOwnerAkind() == AccountDAO.ACCOUNT_TYPE_PERSONAL ? row.getOwnerRealname() : row.getOwnerEnterName(),
					row.getContent(),
					row.getKindName(),
					row.getMethodName(),
					row.getTargetAccountAkind() == AccountDAO.ACCOUNT_TYPE_PERSONAL ? row.getTargetAccountRealname() : row.getTargetAccountEnterName(),
					DateTimeUtil.dateFormat(row.getWriteTime()), 
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

		Estimate record = memberDao.getDetail(Integer.valueOf(id));

		request.setAttribute("record", record);

		return new ModelAndView("manage/estimate/detail");
	}

	public ModelAndView delete_record(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		Estimate record = memberDao.get(Integer.valueOf(id));

		memberDao.delete(record);

		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");


	}

}