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
import com.chengxin.bfip.model.Video;
import com.chengxin.bfip.model.VideoDAO;
import com.chengxin.common.AppSettings;
import com.chengxin.common.BaseController;
import com.chengxin.common.BinaryFormUtil;
import com.chengxin.common.DateTimeUtil;
import com.chengxin.common.File;
import com.chengxin.common.JavascriptUtil;
import com.chengxin.common.KeyValueString;

public class VideoController extends BaseController {

	private VideoDAO memberDao = null;

	public void setMemberDao(VideoDAO value) {
		this.memberDao = value;
	}

	public VideoController() throws Exception {
	}

	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		if (!SessionUtil.isLogined(request)) {
			return JavascriptUtil.MessageMove(request, response, "", "login.html");
		}
		if(!this.checkManagePermission(request, "video.html")) {
			return JavascriptUtil.MessageMove(request, response, "您没有权限", "");
		}
		request.setAttribute("user_info", SessionUtil.getSessionVar(request, "USER_INFO"));

		request.setAttribute("cur_page", "video.html");
		request.setAttribute("title", new String[] { "视频管理", "视频管理" });
		request.setAttribute("breadcrumbs",
				new KeyValueString[] { new KeyValueString("视频管理", "video.html") });

		String action = this.getBlankParameter(request, "pAct", "");

		if (action.equals("") || action.equals("index")) {
			return this.index(request, response, session);
		} else if (action.equals("search")) {
			return this.search(request, response, session);
		} else if (action.equals("add")) {
			return this.add(request, response, session);
		} else if (action.equals("edit")) {
			return this.edit(request, response, session);
		} else if (action.equals("upload")) {
			BinaryFormUtil formUtil = new BinaryFormUtil(
					CommonUtil.getRepositoryRealPath(request) + Constants.CAROUSEL_MEDIA_PATH,
					CommonUtil.getRepositoryRealPath(request) + Constants.UPLOAD_TEMP_PATH,
					this.getSetting(AppSettings.C_COMMON_FILE_PATH_SEP));

			formUtil.initForm(request, response, this.getSetting(AppSettings.C_DEFAULT_ENCODING));

			return this.upload(formUtil, request, response);
		} else if (action.equals("delete")) {
			return this.delete(request, response, session);
		} 
		
		return null;
	}

	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		request.setAttribute("C_SERVICE_BOOKTYPE_NAME", Constants.C_ACCOUNT_ACCOUNT_TYPE);
		return new ModelAndView("manage/video/index");
	}

	public ModelAndView add(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		return new ModelAndView("manage/video/add");
	}

	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		String id = this.getBlankParameter(request, "id", "");

		Video record = memberDao.getDetail(Integer.valueOf(id));

		request.setAttribute("record", record);

		return new ModelAndView("manage/video/edit");
	}

	public ModelAndView upload(BinaryFormUtil formUtil, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(formUtil.getString("id", "0"));
		String title = formUtil.getString("title", "");
		String comment = formUtil.getString("comment", "");

		File videoFile = formUtil.getFile("video_file_url");

		//if(video_title.equals("")) {
		//	return JavascriptUtil.MessageMove(request, response, "video_title", "");
		//}
		Video video = memberDao.getDetail(Integer.valueOf(id));
		if (video == null)
			video = new Video();
		video.setId(id);
		video.setTitle(title);
		video.setComment(comment);

		if (id != 0) {
			if (videoFile != null) {
				String videoPath = CommonUtil.getRepositoryRealPath(request) + "\\.." + video.getVideoUrl().replace("/", "\\");
				java.io.File mediaFile = new java.io.File(videoPath);
				if (mediaFile.exists()) {
					mediaFile.delete();
				}

				video.setVideoName(videoFile.getPhysicalName());
				video.setVideoSize(videoFile.getSize());
				video.setVideoUrl(Constants.CAROUSEL_MEDIA_URL + "/" + videoFile.getPhysicalPath() + "/" + videoFile.getPhysicalName());
			}
		} else {
			if (videoFile != null) {
				video.setVideoName(videoFile.getPhysicalName());
				video.setVideoSize(videoFile.getSize());
				video.setVideoUrl(Constants.CAROUSEL_MEDIA_URL + "/" + videoFile.getPhysicalPath() + "/" + videoFile.getPhysicalName());
			} else {
				video.setVideoName("");
				video.setVideoSize(0);
				video.setVideoUrl("");
			}
		}

		if (id != 0)
			memberDao.update(video);
		else 
			memberDao.insert(video);

		this.cachedObjectService.deleteProgram();

		JSONObject result = new JSONObject();
		result.put("flag", true);
		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
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

		String extraWhere = "";
		List<Video> VideoList = memberDao.search(filterParamObject, extraWhere);
		int count = memberDao.count(filterParamObject, extraWhere);

		ArrayList<String[]> data = new ArrayList<String[]>();

		for (int i = 0; i < VideoList.size(); i++) {
			Video row = VideoList.get(i);

			String opHtml = "", status = "关系中";
			//opHtml += "<a href='javascript:viewRecord(" + String.valueOf(row.getId()) + ")' class='btn btn-xs yellow'><i class='fa fa-file-o'></i> 查看</a>";
			if (row.getCarouselId() == 0) {
				opHtml += "<a href='javascript:editRecord(" + String.valueOf(row.getId()) + ")' class='btn btn-xs green'><i class='fa fa-edit'></i> 编辑</a>";
				opHtml += "<a href='javascript:deleteRecord(" + String.valueOf(row.getId()) + ")' class='btn btn-xs red'><i class='fa fa-trash'></i> 删除</a>";
				status = "未联系";
			}

			String[] dataItem = new String[] {
					row.getTitle(), 
					DateTimeUtil.dateFormat(row.getWriteTime(), "yyyy-MM-dd HH:mm"), 
					status,
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

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject result = new JSONObject();

		String id = this.getBlankParameter(request, "id", "");

		Video record = memberDao.get(Integer.valueOf(id));

		memberDao.delete(record);
		String videoPath = CommonUtil.getRepositoryRealPath(request) + "\\.." + record.getVideoUrl().replace("/", "\\");
		java.io.File imgFile = new java.io.File(videoPath);
		if (imgFile.exists()) {
			imgFile.delete();
		}

		result.put("retcode", 200);
		result.put("msg", "操作成功");

		request.setAttribute("JSON", result);

		return new ModelAndView("json_result");
	}

}