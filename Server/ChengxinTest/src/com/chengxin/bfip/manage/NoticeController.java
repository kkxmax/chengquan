package com.chengxin.bfip.manage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.chengxin.bfip.model.Notice;
import com.chengxin.bfip.model.NoticeDAO;
import com.chengxin.common.BaseController;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;

/**
 * 
 * @author Administrator
 */
public class NoticeController extends BaseController {

	private NoticeDAO noticeDao = null;

	public void setNoticeDao(NoticeDAO value) {
		this.noticeDao = value;
	}

	public NoticeController() throws Exception {

	}

	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		if(!SessionUtil.isLogined(request)) {
            return JavascriptUtil.MessageMove(request, response, "", "login.html");
        }
		if(!this.checkManagePermission(request, "notice.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
    	request.setAttribute("user_info", SessionUtil.getSessionVar(request, "USER_INFO"));
		request.setAttribute("cur_page", "notice.html");
		request.setAttribute("title", new String[] { "系统消息"});
		request.setAttribute("breadcrumbs", new KeyValueString[] { 
				new KeyValueString("系统消息", "notice.html") 
		});

		String action = this.getBlankParameter(request, "pAct", "");

		if (action.equals("") || action.equals("index")) {
			return this.index(request, response, session);
		} else if (action.equals("search")) {
			return this.search(request, response, session);
		} else if (action.equals("read")) {
			return this.read(request, response, session);
		} else if (action.equals("remove")) {
			return this.remove(request, response, session);
		} else if (action.equals("getNewNoticeCnt")) {
			return this.getNewNoticeCnt(request, response, session);
		}

		return null;
	}

	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		return new ModelAndView("manage/notice/index");
	}

	public ModelAndView search(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();
		JSONObject filterParamObject = new JSONObject();

		String filterParam = this.getBlankParameter(request, "filter[filter_param]", "");
		String start = this.getBlankParameter(request, "start", "");
		String length = this.getBlankParameter(request, "length", "");
		String orderColIndex = this.getBlankParameter(request, "order[0][column]", "");
		String orderColName = this.getBlankParameter(request, "columns[" + orderColIndex + "][name]", "");
		String orderDir = this.getBlankParameter(request, "order[0][dir]", "asc");

		if (!filterParam.isEmpty()) {
			filterParam = HtmlUtils.htmlUnescape(filterParam);
			filterParamObject = JSONObject.fromObject(filterParam);
		}
		filterParamObject.put("start", start);
		filterParamObject.put("length", length);
		filterParamObject.put("order_col", orderColName);
		filterParamObject.put("order_dir", orderDir);
		
		String strWhere = "type=" + NoticeDAO.NOTICE_TYPE_ADMIN;

		List<Notice> itemList = noticeDao.search(filterParamObject, strWhere);
		int count = noticeDao.count(filterParamObject, strWhere);

		ArrayList<String[]> data = new ArrayList<String[]>();

		for (int i = 0; i < itemList.size(); i++) {
			Notice row = itemList.get(i);

			String opHtml = "";
			if(row.getStatus() == NoticeDAO.NOTICE_ST_NEW) {
				opHtml = "<a href='javascript:read(" + row.getId() + ")' class='btn btn-xs red'><i class='fa fa-check'></i> 阅读</a>";	
			}

			String[] dataItem = new String[] { 
					"<input type='checkbox' name='sel_ids[]' notice_id=" + row.getId() + ">",
					String.valueOf(i + 1),
					row.getKindName(),
					row.getMsgContent(),
					row.getStatusName(),
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

	public ModelAndView read(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		Notice record = noticeDao.get(Integer.valueOf(id));
		
		record.setStatus(NoticeDAO.NOTICE_ST_READ);
		
		noticeDao.update(record);

		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

	public ModelAndView remove(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String delIds = this.getBlankParameter(request, "del_ids", "");
		
		if(delIds.isEmpty()) {
			result.put("retcode", 201);
			result.put("msg", "请选要删除的消息");
		}
		else {
			String[] ids = delIds.split(",");
			
			for(String id : ids) {
				noticeDao.delete(Integer.valueOf(id));	
			}

			result.put("retcode", 200);
			result.put("msg", "删除成功");
		}
		
		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}
	
	public ModelAndView getNewNoticeCnt(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		int newNoticeCnt = noticeDao.count(null, "type= " + NoticeDAO.NOTICE_TYPE_ADMIN + " and status = " + NoticeDAO.NOTICE_ST_NEW);
		
		result.put("retcode", 200);
		result.put("newNoticeCnt", newNoticeCnt);

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}
}