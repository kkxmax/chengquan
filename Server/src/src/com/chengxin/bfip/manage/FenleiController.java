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
import com.chengxin.bfip.model.Fenlei;
import com.chengxin.bfip.model.FenleiDAO;
import com.chengxin.common.BaseController;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;

/**
 * 
 * @author Administrator
 */
public class FenleiController extends BaseController {

	private FenleiDAO fenleiDao = null;

	public void setfenleiDao(FenleiDAO value) {
		this.fenleiDao = value;
	}

	public FenleiController() throws Exception {

	}

	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		if(!SessionUtil.isLogined(request)) {
            return JavascriptUtil.MessageMove(request, response, "", "login.html");
        }
		if(!this.checkManagePermission(request, "fenlei.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
    	request.setAttribute("user_info", SessionUtil.getSessionVar(request, "USER_INFO"));
		request.setAttribute("cur_page", "fenlei.html");
		request.setAttribute("title", new String[] { "分类管理"});
		request.setAttribute(
				"breadcrumbs",
				new KeyValueString[] { new KeyValueString("分类管理", "fenlei.html") });

		String action = this.getBlankParameter(request, "pAct", "");

		if (action.equals("") || action.equals("index")) {
			return this.index(request, response, session);
		} else if (action.equals("search")) {
			return this.search(request, response, session);
		} else if (action.equals("remove")) {
			return this.remove(request, response, session);
		} else if (action.equals("edit")) {
			return this.edit(request, response, session);
		} else if (action.equals("editDo")) {
			return this.editDo(request, response, session);
		} 

		return null;
	}

	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		request.setAttribute("C_FENLEI_LEIXING", Constants.C_FENLEI_LEIXING);

		return new ModelAndView("manage/fenlei/index");
	}
	
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		String id = this.getBlankParameter(request, "id", "");
		
		Fenlei record = null;
		if(!id.isEmpty()) {
			record = fenleiDao.getDetail(Integer.valueOf(id));
		}
		
		request.setAttribute("C_FENLEI_LEIXING", Constants.C_FENLEI_LEIXING);
		request.setAttribute("record", record);
		
		return new ModelAndView("manage/fenlei/edit_modal");
	}

	public ModelAndView editDo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");
		String fenlei = this.getBlankParameter(request, "fenlei", "");
		String leixing = this.getBlankParameter(request, "leixing", "");

		Fenlei object;
		if(id.isEmpty()) {
			object = new Fenlei();	
		}
		else {
			object = fenleiDao.get(Integer.valueOf(id));
		}
		
		object.setTitle(fenlei);
		object.setLeixing(Integer.valueOf(leixing));

		if(id.isEmpty()) {
			fenleiDao.insert(object);
			result.put("retcode", 200);
			result.put("msg", "新增成功");
		}
		else {
			fenleiDao.update(object);
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

		List<Fenlei> itemList = fenleiDao.search(filterParamObject);
		int count = fenleiDao.count(filterParamObject);

		ArrayList<String[]> data = new ArrayList<String[]>();

		for (int i = 0; i < itemList.size(); i++) {
			Fenlei row = itemList.get(i);

			String opHtml = "<a href='fenlei.html?pAct=edit&id=" + row.getId() + "' class='btn btn-xs purple' data-target='#global-modal' data-toggle='modal'><i class='fa fa-edit'></i> 编辑</a>";
			opHtml += "<a href='javascript:;remove("+ row.getId() +");' class='btn btn-xs default'><i class='fa fa-trash-o'></i> 删除</a>";

			String[] dataItem = new String[] { 
					DateTimeUtil.dateFormat(row.getWriteTime()),
					String.valueOf(i + 1),
					row.getTitle(),
					row.getLeixingName(),
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

	public ModelAndView remove(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		fenleiDao.delete(Integer.valueOf(id));

		result.put("retcode", 200);
		result.put("msg", "彻底删除成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}
}