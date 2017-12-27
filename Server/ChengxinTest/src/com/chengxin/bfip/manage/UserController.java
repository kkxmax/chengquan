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

import com.chengxin.bfip.model.User;
import com.chengxin.bfip.model.UserDAO;
import com.chengxin.common.BaseController;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;
import com.chengxin.common.SecureUtil;

/**
 *
 * @author Administrator
 */
public class UserController extends BaseController {

	private UserDAO memberDao = null;

	public void setMemberDao(UserDAO value) {this.memberDao = value;}

	public UserController() throws Exception {

	}

	public ModelAndView init(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		if(!SessionUtil.isLogined(request)) {
			return JavascriptUtil.MessageMove(request, response, "", "login.html");
		}
		if(!this.checkManagePermission(request, "user.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
		request.setAttribute("user_info", SessionUtil.getSessionVar(request, "USER_INFO"));
		request.setAttribute("cur_page", "user.html");
		request.setAttribute("title", new String[] {"系统管理", "人员管理"});
		request.setAttribute("breadcrumbs", new KeyValueString[] {
				new KeyValueString("系统管理", "user.html"),
				new KeyValueString("人员管理", "user.html")
		});

		String action = this.getBlankParameter(request, "pAct", "");

		if (action.equals("") || action.equals("index")) {
			return this.index(request, response, session);
		} else if (action.equals("search")) {
			return this.search(request, response, session);
		} else if (action.equals("edit")) {
			return this.edit(request, response, session);
		} else if (action.equals("editDo")) {
			return this.editDo(request, response, session);
		} else if (action.equals("add")) {
			return this.add(request, response, session);
		} else if (action.equals("addDo")) {
			return this.addDo(request, response, session);
		}  else if (action.equals("delete")) {
			return this.delete_record(request, response, session);
		} else if (action.equals("format")) {
			return this.formatPassword(request, response, session);
		} else if (action.equals("reset")) {
			return this.reset(request, response, session);
		} else if (action.equals("resetPassword")) {
			return this.resetPassword(request, response, session);
		} 

		return null;
	}

	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		return new ModelAndView("manage/user/index");
	}

	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

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
		List<User> UserList = memberDao.search(filterParamObject,
				extraWhere);
		int count = memberDao.count(filterParamObject, extraWhere);

		ArrayList<String[]> data = new ArrayList<String[]>();

		for (int i = 0; i < UserList.size(); i++) {
			User row = UserList.get(i);

			String opHtml = "<a href='user.html?pAct=edit&id=" + row.getId() + "' class='btn btn-xs purple' data-target='#global-modal' data-toggle='modal'><i class='fa fa-edit'></i> 编辑</a>";
			if(row.getId() != 1){
				opHtml += "<a href='javascript:;delete_record("+ String.valueOf(row.getId()) +");' class='btn btn-xs default'><i class='fa fa-trash-o'></i>删除</a>";
			}
//			opHtml += "<a href='javascript:;formatPassword("+ String.valueOf(row.getId()) +");' class='btn btn-xs default'><i class='fa fa-refresh'></i>重置密码</a>";
//			opHtml += "<a onclick='onChangePwd(this)' href='user.html?pAct=reset&id=" + row.getId() + "' class='btn btn-xs green' data-target='#global-modal' data-toggle='modal'><i class='fa fa-refresh'></i>重置密码</a>";
			opHtml += "<a onclick='onChangePwd(this, " + row.getId() + ")' class='btn btn-xs green' ><i class='fa fa-refresh'></i>重置密码</a>";
			String[] dataItem = new String[] {
					row.getUsername(), 
					row.getRealname(), 
					row.getTitle(), 
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

	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		String id = this.getBlankParameter(request, "id", "");

		User record = null;
		if(!id.isEmpty()) {
			record = memberDao.getDetail(Integer.valueOf(id));
		}

		List<User> role_id = memberDao.getAllRoles();

		request.setAttribute("role_ids", role_id);
		request.setAttribute("record", record);

		return new ModelAndView("manage/user/edit_modal");
	}

	public ModelAndView editDo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");
		String username = this.getBlankParameter(request, "username", "");
		String realname = this.getBlankParameter(request, "realname", "");
		String role_id = this.getBlankParameter(request, "role_id", "");

		User object;
		if(id.isEmpty()) {
			object = new User();	
		}
		else {
			object = memberDao.get(Integer.valueOf(id));
		}

		object.setUsername(username);
		object.setRealname(realname);
		object.setRoleId(Integer.valueOf(role_id));


		memberDao.update(object);
		result.put("retcode", 200);
		result.put("msg", "更新成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		List<User> role_id = memberDao.getAllRoles();

		request.setAttribute("role_ids", role_id);

		return new ModelAndView("manage/user/add_modal");
	}

	public ModelAndView addDo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");
		String username = this.getBlankParameter(request, "username", "");
		String realname = this.getBlankParameter(request, "realname", "");
		String password = this.getBlankParameter(request, "password", "");
		String confirm_password = this.getBlankParameter(request, "repassword", "");
		String role_id = this.getBlankParameter(request, "role_id", "");

		User object = new User();

		if(password.equals(confirm_password)){
			object.setUsername(username);
			object.setRealname(realname);
			object.setPassword(SecureUtil.getMD5(password));
			object.setRoleId(Integer.valueOf(role_id));
			object.setWriteTime(new Date());

			memberDao.insert(object);

			result.put("retcode", 200);
			result.put("msg", "新增成功");

		}else{
			result.put("retcode", 201);
			result.put("msg", "两次密码不一致");
		}
		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

	public ModelAndView delete_record(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		User record = memberDao.get(Integer.valueOf(id));

		memberDao.delete(record);

		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

	public ModelAndView formatPassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		User record = memberDao.get(Integer.valueOf(id));
		record.setPassword(SecureUtil.getMD5("123456"));
		memberDao.update(record);

		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

	public ModelAndView reset(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		String id = this.getBlankParameter(request, "id", "");

		User record = memberDao.getDetail(Integer.valueOf(id));
		request.setAttribute("record", record);
		return new ModelAndView("manage/user/reset_modal");
	}

	public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");
		String password = this.getBlankParameter(request, "password", "");
		String confirmPassword = this.getBlankParameter(request, "confirmPassword", "");

		if(password.equals(confirmPassword)){
			User record = memberDao.get(Integer.valueOf(id));
			record.setPassword(SecureUtil.getMD5(password));
			memberDao.update(record);

			result.put("retcode", 200);
			result.put("msg", "密码重置成功");

		}else{
			result.put("retcode", 201);
			result.put("msg", "两次密码不一致");
		}
		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}
}