package com.chengxin.bfip.manage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.chengxin.bfip.Constants;
import com.chengxin.bfip.model.Role;
import com.chengxin.bfip.model.RoleDAO;
import com.chengxin.bfip.model.User;
import com.chengxin.common.BaseController;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;
import com.chengxin.common.SecureUtil;

/**
 * 
 * @author Administrator
 */
public class RoleController extends BaseController {

	private RoleDAO memberDao = null;

	public void setMemberDao(RoleDAO value) {
		this.memberDao = value;
	}

	public RoleController() throws Exception {

	}

	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		if (!SessionUtil.isLogined(request)) {
			return JavascriptUtil.MessageMove(request, response, "",
					"login.html");
		}
		if(!this.checkManagePermission(request, "role.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
		request.setAttribute("user_info",
				SessionUtil.getSessionVar(request, "USER_INFO"));

		request.setAttribute("cur_page", "role.html");
		request.setAttribute("title", new String[] { "系统管理", "角色管理" });
		request.setAttribute("breadcrumbs",
				new KeyValueString[] { 
				new KeyValueString("系统管理","user.html"),
				new KeyValueString("角色管理","role.html"),
		});

		String action = this.getBlankParameter(request, "pAct", "");

		if (action.equals("") || action.equals("index")) {
			return this.index(request, response, session);
		} else if (action.equals("search")) {
			return this.search(request, response, session);
		} else if (action.equals("viewDetail")) {
			return this.viewDetail(request, response, session);
		} else if (action.equals("delete")) {
			return this.delete_record(request, response, session);
		} else if (action.equals("editDo")) {
			return this.editDo(request, response, session);
		}else if (action.equals("add")) {
			return this.add(request, response, session);
		}else if (action.equals("addDo")) {
			return this.addDo(request, response, session);
		}

		return null;
	}

	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		request.setAttribute("C_SERVICE_BOOKTYPE_NAME", Constants.C_ACCOUNT_ACCOUNT_TYPE);
		return new ModelAndView("manage/role/index");
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
		List<Role> RoleList = memberDao.search(filterParamObject,
				extraWhere);
		int count = memberDao.count(filterParamObject, extraWhere);

		ArrayList<String[]> data = new ArrayList<String[]>();

		for (int i = 0; i < RoleList.size(); i++) {
			Role row = RoleList.get(i);
			String settingHtml = "";
			settingHtml += "<a href='role.html?pAct=viewDetail&id="
					+ String.valueOf(row.getId())
					+ "' class='btn btn-xs green' data-target='#global-modal' data-toggle='modal'> <i class='fa fa-setting'></i>操作设置</a>";
			String opHtml = "";
			//			boolean status = memberDao.getStatus(String.valueOf(row.getId()));
			//			if(status == true){
			//				opHtml += "<a href='javascript:;delete_record("+ String.valueOf(row.getId()) +");' class='btn btn-xs default'><i class='fa fa-trash-o'></i>删除</a>";
			//			}
			opHtml += "<a href='javascript:;delete_record("+ String.valueOf(row.getId()) +");' class='btn btn-xs default'><i class='fa fa-trash-o'></i>删除</a>";
			String[] dataItem = new String[] {
					String.valueOf(i+1),
					row.getTitle(), 
					settingHtml,
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

		Role record = memberDao.getDetail(Integer.valueOf(id));

		request.setAttribute("record", record);

		return new ModelAndView("manage/role/detail");
	}

	public ModelAndView delete_record(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		Role record = memberDao.get(Integer.valueOf(id));

		memberDao.delete(record);

		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");


	}

	public ModelAndView editDo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");
		String title = this.getBlankParameter(request, "title", "");
		String funcPersonal = this.getBlankParameter(request, "funcPersonal", "");
		String funcEnter = this.getBlankParameter(request, "funcEnter", "");
		String funcChanpin = this.getBlankParameter(request, "funcChanpin", "");
		String funcItem = this.getBlankParameter(request, "funcItem", "");
		String funcCarousel = this.getBlankParameter(request, "funcCarousel", "");
		String funcVideo = this.getBlankParameter(request, "funcVideo", "");
		String funcHot = this.getBlankParameter(request, "funcHot", "");
		String funcError = this.getBlankParameter(request, "funcError", "");
		String funcSystem = this.getBlankParameter(request, "funcSystem", "");
		String funcComment = this.getBlankParameter(request, "funcComment", "");
		String funcXingye = this.getBlankParameter(request, "funcXingye", "");
		String funcOpinion = this.getBlankParameter(request, "funcOpinion", "");
		String funcStatistic = this.getBlankParameter(request, "funcStatistic", "");
		String funcFenlei = this.getBlankParameter(request, "funcFenlei", "");
		String funcProduct = this.getBlankParameter(request, "funcProduct", "");

		if(funcPersonal == ""){
			funcPersonal = "0";
		}else{
			funcPersonal = "1";
		}
		if(funcEnter == ""){
			funcEnter = "0";
		}else{
			funcEnter = "1";
		}
		if(funcChanpin == ""){
			funcChanpin = "0";
		}else{
			funcChanpin = "1";
		}
		if(funcItem == ""){
			funcItem = "0";
		}else{
			funcItem = "1";
		}
		if(funcCarousel == ""){
			funcCarousel = "0";
		}else{
			funcCarousel = "1";
		}

		if(funcVideo == ""){
			funcVideo = "0";
		}else{
			funcVideo = "1";
		}
		if(funcHot == ""){
			funcHot = "0";
		}else{
			funcHot = "1";
		}
		if(funcError == ""){
			funcError = "0";
		}else{
			funcError = "1";
		}
		if(funcSystem == ""){
			funcSystem = "0";
		}else{
			funcSystem = "1";
		}
		if(funcComment == ""){
			funcComment = "0";
		}else{
			funcComment = "1";
		}
		if(funcXingye == ""){
			funcXingye = "0";
		}else{
			funcXingye = "1";
		}
		if(funcOpinion == ""){
			funcOpinion = "0";
		}else{
			funcOpinion = "1";
		}
		if(funcStatistic == ""){
			funcStatistic = "0";
		}else{
			funcStatistic = "1";
		}

		if(funcProduct == ""){
			funcProduct = "0";
		}else{
			funcProduct = "1";
		}

		if(funcFenlei == ""){
			funcFenlei = "0";
		}else{
			funcFenlei = "1";
		}
		Role object;
		object = new Role();
		object.setId(Integer.valueOf(id));
		object.setTitle(title);
		object.setFuncPersonal(Integer.valueOf(funcPersonal));
		object.setFuncEnter(Integer.valueOf(funcEnter));
		object.setFuncChanpin(Integer.valueOf(funcChanpin));
		object.setFuncItem(Integer.valueOf(funcItem));
		object.setFuncCarousel(Integer.valueOf(funcCarousel));
		object.setFuncVideo(Integer.valueOf(funcVideo));
		object.setFuncHot(Integer.valueOf(funcHot));
		object.setFuncError(Integer.valueOf(funcError));
		object.setFuncSystem(Integer.valueOf(funcSystem));
		object.setFuncComment(Integer.valueOf(funcComment));
		object.setFuncXingye(Integer.valueOf(funcXingye));
		object.setFuncOpinion(Integer.valueOf(funcOpinion));
		object.setFuncStatistic(Integer.valueOf(funcStatistic));
		object.setFuncFenlei(Integer.valueOf(funcFenlei));
		object.setFuncProduct(Integer.valueOf(funcProduct));


		memberDao.update(object);
		result.put("retcode", 200);
		result.put("msg", "更新成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		return new ModelAndView("manage/role/add_modal");
	}

	public ModelAndView addDo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String title = this.getBlankParameter(request, "title", "");
		String extraWhere = "and title = '" + title + "'";
		if(memberDao.find(extraWhere) == true){
			Role object = new Role();

			object.setTitle(title);

			memberDao.insert(object);

			result.put("retcode", 200);
			result.put("msg", "新增成功");
		}else{
			result.put("retcode", 201);
			result.put("msg", "角色名称不能相同");
		}

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

}