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
import com.chengxin.bfip.model.Hots;
import com.chengxin.bfip.model.HotsDAO;
import com.chengxin.bfip.model.Xyleixing;
import com.chengxin.bfip.model.XyleixingDAO;
import com.chengxin.common.BaseController;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;

/**
 * 
 * @author Administrator
 */
public class HotsController extends BaseController {

	private HotsDAO hotDao = null;
	private XyleixingDAO xyleixingDao = null;

	public void setXyleixingDao(XyleixingDAO xyleixingDao) {
		this.xyleixingDao = xyleixingDao;
	}

	public void sethotDao(HotsDAO value) {
		this.hotDao = value;
	}

	public HotsController() throws Exception {

	}

	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		if (!SessionUtil.isLogined(request)) {
			return JavascriptUtil.MessageMove(request, response, "", "login.html");
		}
		if(!this.checkManagePermission(request, "hots.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
		request.setAttribute("user_info", SessionUtil.getSessionVar(request, "USER_INFO"));
		request.setAttribute("cur_page", "hots.html");
		request.setAttribute("title", new String[] { "热点管理", "热点管理" });
		request.setAttribute("breadcrumbs",
				new KeyValueString[] { new KeyValueString("热点管理",
						"hots.html") });

		String action = this.getBlankParameter(request, "pAct", "");

		if (action.equals("") || action.equals("index")) {
			return this.index(request, response, session);
		} else if (action.equals("search")) {
			return this.search(request, response, session);
		} else if (action.equals("viewDetail")) {
			return this.viewDetail(request, response, session);
		} else if (action.equals("delete")) {
			return this.delete_record(request, response, session);
		} else if (action.equals("edit")) {
			return this.edit(request, response, session);
		} else if (action.equals("editDo")) {
			return this.editDo(request, response, session);
		} else if (action.equals("getLevel2Xyleixing")) {
			return this.getLevel2Xyleixing(request, response, session);
		} 

		return null;
	}

	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		
		List<Xyleixing> leixings = xyleixingDao.search(null, "upper_id IS NULL");
		
		request.setAttribute("leixings", leixings);
		request.setAttribute("C_HOTS_ST_NAME", Constants.C_HOTS_ST_NAME);

		return new ModelAndView("manage/hots/index");
	}

	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		String id = this.getBlankParameter(request, "id", "");
		
		Hots record = null;
		if(!id.isEmpty()) {
			record = hotDao.getDetail(Integer.valueOf(id));
		}
		
		List<Xyleixing> leixings = xyleixingDao.search(null, "upper_id IS NULL");
		request.setAttribute("leixings", leixings);
		request.setAttribute("record", record);		
		
		return new ModelAndView("manage/hots/edit");
	}

	public ModelAndView editDo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");
		String title = this.getBlankParameter(request, "title", "");
		String kind = this.getBlankParameter(request, "kind", "");
		String content = this.getBlankParameter(request, "content", "");

		Hots hot;
		if(id.isEmpty()) {
			hot = new Hots();	
		}
		else {
			hot = hotDao.get(Integer.valueOf(id));
		}
		
		hot.setTitle(title);
		hot.setKind(Integer.valueOf(kind));
		hot.setContent(content);

		if(id.isEmpty()) {
			hotDao.insert(hot);
			result.put("retcode", 200);
			result.put("msg", "新增成功");
		}
		else {
			hotDao.update(hot);
			result.put("retcode", 200);
			result.put("msg", "更新成功");
		}

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
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
		List<Hots> HotsList = hotDao.search(filterParamObject,
				extraWhere);
		int count = hotDao.count(filterParamObject, extraWhere);

		ArrayList<String[]> data = new ArrayList<String[]>();

		for (int i = 0; i < HotsList.size(); i++) {
			Hots row = HotsList.get(i);

			String opHtml = "<a href='hots.html?pAct=viewDetail&id="
					+ row.getId()
					+ "' class='btn btn-xs purple' data-target='#global-modal' data-toggle='modal'><i class='fa fa-edit'></i> 查看</a>";
			opHtml += "<a href='hots.html?pAct=edit&id=" + row.getId() + "' class='btn btn-xs purple'><i class='fa fa-edit'></i> 编辑</a>";
			opHtml += "<a href='javascript:;delete_record("+ String.valueOf(row.getId()) +");' class='btn btn-xs default'><i class='fa fa-trash-o'></i> 删除</a>";
			String[] dataItem = new String[] {
					row.getTitle(), 
					row.getKindName(),
					DateTimeUtil.dateFormat(row.getWriteTime()),
					String.valueOf(row.getVisitCnt()),
					String.valueOf(row.getCommentCnt()),
					String.valueOf(row.getRemarkCnt()),
					String.valueOf(row.getShareCnt()),
					DateTimeUtil.dateFormat(row.getStName()),
					DateTimeUtil.dateFormat(row.getUpTime()), 
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

		Hots record = hotDao.getDetail(Integer.valueOf(id));

		request.setAttribute("record", record);

		return new ModelAndView("manage/hots/detail");
	}

	public ModelAndView delete_record(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		Hots record = hotDao.get(Integer.valueOf(id));

		hotDao.delete(record);

		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}
	
	public ModelAndView getLevel2Xyleixing(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String level1leixing = this.getBlankParameter(request, "level1leixing", "");

		String where = "";
		if(level1leixing.isEmpty()) {
			where = "upper_id IS NOT NULL";
		}
		else {
			where = "upper_id=" + level1leixing;
		}
		List<Xyleixing> records = xyleixingDao.search(null, where);

		result.put("retcode", 200);
		result.put("records", records);

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

}