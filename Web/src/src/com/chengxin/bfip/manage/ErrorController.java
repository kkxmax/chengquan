package com.chengxin.bfip.manage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.chengxin.bfip.CommonUtil;
import com.chengxin.bfip.Constants;
import com.chengxin.bfip.model.Account;
import com.chengxin.bfip.model.AccountDAO;
import com.chengxin.bfip.model.ErrorDAO;
import com.chengxin.bfip.model.Errors;
import com.chengxin.bfip.model.EstimateDAO;
import com.chengxin.bfip.model.MarkLogDAO;
import com.chengxin.bfip.model.NoticeDAO;
import com.chengxin.common.BaseController;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;

/**
 * 
 * @author Administrator
 */
public class ErrorController extends BaseController {

	private ErrorDAO memberDao = null;
	private NoticeDAO noticeDao = null;
	private MarkLogDAO markLogDao = null;

	public void setNoticeDao(NoticeDAO value) {this.noticeDao = value;}
	public void setMarkLogDao(MarkLogDAO value) {this.markLogDao = value;}
	public void setMemberDao(ErrorDAO value) {this.memberDao = value;}

	public ErrorController() throws Exception {

	}

	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		if (!SessionUtil.isLogined(request)) {
			return JavascriptUtil.MessageMove(request, response, "",
					"login.html");
		}
		if(!this.checkManagePermission(request, "error.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
		request.setAttribute("user_info",
				SessionUtil.getSessionVar(request, "USER_INFO"));
		request.setAttribute("cur_page", "error.html");
		request.setAttribute("title", new String[] { "纠错管理", "纠错管理" });
		request.setAttribute("breadcrumbs",
				new KeyValueString[] { new KeyValueString("纠错管理",
						"error.html") });

		String action = this.getBlankParameter(request, "pAct", "");

		if (action.equals("") || action.equals("index")) {
			return this.index(request, response, session);
		} else if (action.equals("search")) {
			return this.search(request, response, session);
		} else if (action.equals("viewDetail")) {
			return this.viewDetail(request, response, session);
		} else if (action.equals("showTestModal")) {
            return this.showTestModal(request, response, session);
        } else if (action.equals("changeTestStatus")) {
			return this.changeTestStatus(request, response, session);
		} 

		return null;
	}

	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		request.setAttribute("C_ERROR_STATUS_NAME", Constants.C_ACCOUNT_TEST_STATUS);
		//		request.setAttribute("C_error_METHOD_NAME", Constants.C_error_METHOD_NAME);
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
		//		request.setAttribute("C_Errors_KIND_NAME",  data);
		return new ModelAndView("manage/error/index");
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
		List<Errors> ErrorsList = memberDao.search(filterParamObject, extraWhere);
		int count = memberDao.count(filterParamObject, extraWhere);

		ArrayList<String[]> data = new ArrayList<String[]>();

		for (int i = 0; i < ErrorsList.size(); i++) {
			Errors row = ErrorsList.get(i);

			String opHtml = "<a href='error.html?pAct=viewDetail&id=" + row.getId() + "' class='btn btn-xs purple' data-target='#global-modal' data-toggle='modal'><i class='fa fa-edit'></i> 查看</a>";
			if(row.getStatus() == ErrorDAO.ERROR_ST_PROCESSING) {
				opHtml += "<a href='error.html?pAct=showTestModal&id=" + row.getId() + "' class='btn btn-xs purple' data-target='#global-modal' data-toggle='modal'><i class='fa fa-edit'></i> 审核</a>";	
			}
			
			String[] dataItem = new String[] {
					row.getOwnerMobile(), 
					row.getOwnerAkind() == AccountDAO.ACCOUNT_TYPE_PERSONAL ? row.getOwnerRealname() : row.getOwnerEnterName(),
					row.getEstimateeAkind() == AccountDAO.ACCOUNT_TYPE_PERSONAL ? row.getEstimateeRealname() : row.getEstimateeEnterName(),
					row.getEstimaterAkind() == AccountDAO.ACCOUNT_TYPE_PERSONAL ? row.getEstimaterRealname() : row.getEstimaterEnterName(),
					row.getEstimateContent(),
					row.getReason(),
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

	public ModelAndView viewDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		String id = this.getBlankParameter(request, "id", "");

		Errors record = memberDao.getDetail(Integer.valueOf(id));

		request.setAttribute("record", record);

		return new ModelAndView("manage/error/detail");
	}
	
	public ModelAndView showTestModal(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
    	
    	String id = this.getBlankParameter(request, "id", "");

    	Errors record = memberDao.getDetail(Integer.valueOf(id));
    	
    	request.setAttribute("record", record);
    	
    	return new ModelAndView("manage/error/test_modal");
    }

	public ModelAndView changeTestStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");
		String targetStatus = this.getBlankParameter(request, "targetStatus", String.valueOf(ErrorDAO.ERROR_ST_PASS));

		Errors record = memberDao.getDetail("id=" + id);
		record.setStatus(Integer.valueOf(targetStatus));
		memberDao.update(record);
		
		if(targetStatus.equals(String.valueOf(ErrorDAO.ERROR_ST_PASS))) {
			
			CommonUtil.insertMarkLog(markLogDao, record.getOwnerId(), MarkLogDAO.LOG_KIND_CORRECT_GIVE, null, null, record.getKind(), MarkLogDAO.PMARK_CORRECT_GIVE, null, null);
			CommonUtil.insertMarkLog(markLogDao, record.getEstimaterId(), MarkLogDAO.LOG_KIND_CORRECT_RECEIVE, null, null, record.getKind(), null
					, record.getKind() == ErrorDAO.ERROR_KIND_OVER ? MarkLogDAO.NMARK_CORRECT_RECEIVE_OVER : MarkLogDAO.NMARK_CORRECT_RECEIVE_FALSE
					, record.getOwnerId());
			
			CommonUtil.insertNotice(noticeDao, NoticeDAO.NOTICE_TYPE_USER, record.getOwnerId(), NoticeDAO.NOTICE_KIND_CORRECTION
					, NoticeDAO.NOTICE_SUBKIND_CORRECTION_GIVE
					, CommonUtil.getNoticeMsgTitle(NoticeDAO.NOTICE_KIND_CORRECTION)
					, CommonUtil.getNoticeMsgContent(NoticeDAO.NOTICE_KIND_CORRECTION, NoticeDAO.NOTICE_SUBKIND_CORRECTION_GIVE
							, ErrorDAO.ERROR_ST_PASS
							, MarkLogDAO.PMARK_CORRECT_GIVE
					)
					, null, null, record.getId());
			CommonUtil.insertNotice(noticeDao, NoticeDAO.NOTICE_TYPE_USER, record.getEstimaterId(), NoticeDAO.NOTICE_KIND_CORRECTION
					, NoticeDAO.NOTICE_SUBKIND_CORRECTION_RECEIVE
					, CommonUtil.getNoticeMsgTitle(NoticeDAO.NOTICE_KIND_CORRECTION)
					, CommonUtil.getNoticeMsgContent(NoticeDAO.NOTICE_KIND_CORRECTION, NoticeDAO.NOTICE_SUBKIND_CORRECTION_RECEIVE
							, record.getKind()
							, null
					)
					, null, null, record.getId());
		}
		else if(targetStatus.equals(String.valueOf(ErrorDAO.ERROR_ST_NOPASS))) {
			CommonUtil.insertNotice(noticeDao, NoticeDAO.NOTICE_TYPE_USER, record.getOwnerId(), NoticeDAO.NOTICE_KIND_CORRECTION
					, NoticeDAO.NOTICE_SUBKIND_CORRECTION_GIVE
					, CommonUtil.getNoticeMsgTitle(NoticeDAO.NOTICE_KIND_CORRECTION)
					, CommonUtil.getNoticeMsgContent(NoticeDAO.NOTICE_KIND_CORRECTION, NoticeDAO.NOTICE_SUBKIND_CORRECTION_GIVE
							, ErrorDAO.ERROR_ST_NOPASS
							, null
					)
					, null, null, record.getId());
		}
		
		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");


	}

}